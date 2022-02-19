package com.likefirst.btos.ui.main


import android.accounts.Account
import android.accounts.AccountManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.Preference
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationBarView
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.firebase.MessageDTO
import com.likefirst.btos.data.entities.firebase.NotificationDTO
import com.likefirst.btos.data.local.FCMDatabase
import com.likefirst.btos.data.local.NotificationDatabase
import com.likefirst.btos.data.remote.notify.response.NoticeDetailResponse
import com.likefirst.btos.data.remote.notify.service.FCMService
import com.likefirst.btos.data.remote.notify.service.NoticeService
import com.likefirst.btos.data.remote.notify.view.NoticeAPIView
import com.likefirst.btos.data.remote.notify.view.SharedNotifyModel
import com.likefirst.btos.databinding.ActivityMainBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.archive.ArchiveFragment
import com.likefirst.btos.ui.history.HistoryFragment
import com.likefirst.btos.ui.home.HomeFragment
import com.likefirst.btos.ui.home.MailViewActivity
import com.likefirst.btos.ui.home.MailboxFragment
import com.likefirst.btos.ui.profile.ProfileFragment
import com.likefirst.btos.ui.profile.setting.NoticeActivity
import com.likefirst.btos.utils.toArrayList
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import com.likefirst.btos.data.entities.DiaryViewerInfo
import com.likefirst.btos.data.remote.notify.response.Alarm
import com.likefirst.btos.data.remote.notify.response.AlarmInfo
import com.likefirst.btos.data.remote.notify.service.AlarmService
import com.likefirst.btos.data.remote.notify.view.*
import com.likefirst.btos.data.remote.posting.response.MailDiaryResponse
import com.likefirst.btos.data.remote.posting.response.MailLetterResponse
import com.likefirst.btos.data.remote.posting.response.MailReplyResponse
import com.likefirst.btos.data.remote.posting.service.DiaryService
import com.likefirst.btos.data.remote.posting.service.MailLetterService
import com.likefirst.btos.data.remote.posting.service.MailReplyService
import com.likefirst.btos.data.remote.posting.service.MailboxService
import com.likefirst.btos.data.remote.posting.view.MailDiaryView
import com.likefirst.btos.data.remote.posting.view.MailLetterView
import com.likefirst.btos.data.remote.posting.view.MailReplyView
import com.likefirst.btos.ui.posting.DiaryViewerActivity
import com.likefirst.btos.ui.posting.MailReplyActivity
import com.likefirst.btos.utils.LiveSharedPreferences
import com.likefirst.btos.utils.getUserIdx


class MainActivity: BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),AlarmInfoView,AlarmListView,MailDiaryView, MailLetterView, MailReplyView{

    private var auth : FirebaseAuth? = null

    private val homeFragment = HomeFragment()
    private val archiveFragment = ArchiveFragment()
    private val historyFragment = HistoryFragment()
    private val profileFragment= ProfileFragment()

    var isDrawerOpen =true
    var isMailOpen=false

    lateinit var noticeList :ArrayList<NoticeDetailResponse>
    var prevNoticeSize : Int =0
    lateinit var  sharedNotifyModel: SharedNotifyModel
    lateinit var alarmService: AlarmService
    lateinit var diaryService : DiaryService
    lateinit var letterService : MailLetterService
    lateinit var replyService:MailReplyService

    interface onBackPressedListener {
        fun onBackPressed();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        sharedNotifyModel= ViewModelProvider(this).get(SharedNotifyModel::class.java)
        val spf = getSharedPreferences("notification", MODE_PRIVATE) // 기존에 있던 데이터
        val liveSharedPreference = LiveSharedPreferences(spf)
        liveSharedPreference
            .getString("messageList", "undefine")
            .observe(this, Observer<String> { result ->
                if(result!="undefine"){
                    sharedNotifyModel.setMsgLiveData(true)
                    sharedNotifyModel.setNoticeLiveData(true)
                }else{
                    sharedNotifyModel.setMsgLiveData(false)
                }
            })
    }


   override fun initAfterBinding() {
        binding.mainBnv.itemIconTintList = null
        initAlarm()
        alarmService.getAlarmList(getUserIdx())

        binding.mainLayout.addDrawerListener(object:DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {
                alarmService.getAlarmList(getUserIdx())
                sharedNotifyModel.setNoticeLiveData(false)
            }
            override fun onDrawerClosed(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {}
        })

        supportFragmentManager.beginTransaction()
            .replace(R.id.fr_layout, homeFragment, "home")
            .setReorderingAllowed(true)
            .commitNowAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { it ->BottomNavView().onNavigationItemSelected(it) }

    }

    fun initAlarm(){
        alarmService = AlarmService()
        alarmService.setAlarmInfoView(this)
        alarmService.setAlarmListView(this)

        diaryService = DiaryService()
        diaryService.setDiaryView(this)

        letterService = MailLetterService()
        letterService.setLetterView(this)

        replyService = MailReplyService()
        replyService.setReplyView(this)

    }

    inner class BottomNavView :NavigationBarView.OnItemSelectedListener {
        override fun onNavigationItemSelected(it: MenuItem): Boolean {
            when (it.itemId) {
                R.id.homeFragment -> {
                    isDrawerOpen=true
                    if (homeFragment.isAdded) {
                        supportFragmentManager.beginTransaction()
                            .hide(archiveFragment)
                            .hide(historyFragment)
                            .hide(profileFragment)
                            .show(homeFragment)
                            .setReorderingAllowed(true)
                            .commitNowAllowingStateLoss()
                        Log.d("homeclick", "added")
                    } else {
                        supportFragmentManager.beginTransaction()
                            .hide(archiveFragment)
                            .hide(profileFragment)
                            .hide(historyFragment)
                            .add(R.id.fr_layout, homeFragment, "home")
                            .show(homeFragment)
                            .setReorderingAllowed(true)
                            .commitAllowingStateLoss()
                        Log.d("homeClick", "noadded")
                    }
                    return true
                }
                R.id.historyFragment ->{
                    isDrawerOpen=false
                    val editor= getSharedPreferences("HistoryBackPos", AppCompatActivity.MODE_PRIVATE).edit()
                    editor.clear()
                    editor.commit()
                    if(historyFragment.isAdded){
                        supportFragmentManager.beginTransaction()
                            .hide(archiveFragment)
                            .hide(homeFragment)
                            .hide(profileFragment)
                            .show(historyFragment)
                            .setReorderingAllowed(true)
                            .commitNowAllowingStateLoss()
                        Log.d("historyClick", "added")
                    }else{
                        supportFragmentManager.beginTransaction()
                            .hide(homeFragment)
                            .hide(archiveFragment)
                            .hide(profileFragment)
                            .add(R.id.fr_layout, historyFragment, "history")
                            .show(historyFragment)
                            .setReorderingAllowed(true)
                            .commitAllowingStateLoss()
                        Log.d("historyClick", "noadded")
                    }
                    return true
                }

                R.id.archiveFragment -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.fr_layout, archiveFragment)
//                        .setReorderingAllowed(true)
//                        .commitNowAllowingStateLoss()
                    isDrawerOpen=false
                    if (archiveFragment.isAdded) {
                        supportFragmentManager.beginTransaction()
                            .hide(homeFragment)
                            .hide(historyFragment)
                            .hide(profileFragment)
                            .show(archiveFragment)
                            .setReorderingAllowed(true)
                            .commitNowAllowingStateLoss()
                        Log.d("archiveClick", "added")
                    } else {
                        supportFragmentManager.beginTransaction()
                            .hide(homeFragment)
                            .hide(profileFragment)
                            .hide(historyFragment)
                            .add(R.id.fr_layout, archiveFragment, "archive")
                            .show(archiveFragment)
                            .setReorderingAllowed(true)
                            .commitAllowingStateLoss()
                        Log.d("archiveClick", "noadded")
                    }
                    return true
                }
                R.id.profileFragment -> {
                    isDrawerOpen=false
                    if (profileFragment.isAdded) {
                        supportFragmentManager.beginTransaction()
                            .hide(homeFragment)
                            .hide(historyFragment)
                            .hide(archiveFragment)
                            .show(profileFragment)
                            .setReorderingAllowed(true)
                            .commitNowAllowingStateLoss()
                        Log.d("profileClick", "added")
                    } else {
                        supportFragmentManager.beginTransaction()
                            .hide(homeFragment)
                            .hide(archiveFragment)
                            .hide(historyFragment)
                            .add(R.id.fr_layout, profileFragment, "profile")
                            .show(profileFragment)
                            .setReorderingAllowed(true)
                            .commitAllowingStateLoss()
                        Log.d("profileClick", "noadded")
                    }
                    return true
                }
            }
            return false
        }
    }

    override fun onNewIntent(intent: Intent?) {

        if (intent != null){
            // 리스트에서 일기 수정이 일어난 경우 (현재 보이는 리스트 즉시 업데이트)
            if(intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo") != null
                && intent.getBooleanExtra("isDiaryUpdated", false) && intent.getIntExtra("position", -1) >= 0){
                val intentDataset = intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo")!!
                val position = intent.getIntExtra("position", -1)
                val mArchiveFragment: ArchiveFragment = supportFragmentManager.findFragmentById(R.id.fr_layout) as ArchiveFragment
                mArchiveFragment.listPage.mAdapter.updateList(position, intentDataset.doneLists.size, intentDataset.emotionIdx, intentDataset.contents)
            }
            // 달력에서 일기 수정이 일어난 경우 (리스트 새로 갱신)
            else if (intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo") != null
                && intent.getBooleanExtra("isDiaryUpdated", false) && intent.getIntExtra("position", -1) == -1){
                val mArchiveFragment: ArchiveFragment = supportFragmentManager.findFragmentById(R.id.fr_layout) as ArchiveFragment
                mArchiveFragment.listPage.reLoadDiaryList(mArchiveFragment.listPage.mAdapter, HashMap())
            }
            // 일기가 작성된 경우 (리스트 새로 갱신, 달력 현재 페이지 갱신)
            else if (intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo") != null
                && !intent.getBooleanExtra("isDiaryUpdated", false)){
                val mArchiveFragment: ArchiveFragment = supportFragmentManager.findFragmentById(R.id.fr_layout) as ArchiveFragment
                mArchiveFragment.listPage.reLoadDiaryList(mArchiveFragment.listPage.mAdapter, HashMap())
                var viewMode = 0
                val radioGroup = findViewById<RadioGroup>(R.id.archive_calendar_rg)
                when (radioGroup.checkedRadioButtonId){         // 라디오버튼에 따라서 viewMode 변경
                    R.id.archive_calendar_done_list_rb -> viewMode = 0
                    R.id.archive_calendar_emotion_rb -> viewMode = 1
                }
//                ArchiveCalendarFragment.pageIndexFlag = true
                mArchiveFragment.calendarPage.initCalendar(viewMode, true)
//                ArchiveCalendarFragment.pageIndexFlag = false
            }
        }

        super.onNewIntent(intent)
    }

    fun mailOpenStatus():Boolean{
        return isMailOpen
    }

    fun notifyDrawerHandler(Option : String){
        when(Option){
            "open"->{
                Log.d("Draw","open")
                binding.mainLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
                binding.mainLayout.openDrawer((GravityCompat.START))

            }
            "unlock"->{
                Log.d("Draw","unlock")
                binding.mainLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
            }
            "lock"->{
                Log.d("Draw","lock")
                binding.mainLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
    }


    override fun onBackPressed() {
        if(homeFragment.isVisible && !isMailOpen){
            finish()
        } else {

            val fragmentList = supportFragmentManager.fragments
            for (fragment in fragmentList) {
                if (fragment is onBackPressedListener) {
                    (fragment as onBackPressedListener).onBackPressed()
                    return
                }
            }

            if(homeFragment.isAdded){
                supportFragmentManager.beginTransaction()
                    .show(homeFragment)
                    .hide(archiveFragment)
                    .hide(profileFragment)
                    .hide(historyFragment)
                    .commitNow()
            } else {
                supportFragmentManager.beginTransaction()
                    .hide(archiveFragment)
                    .hide(profileFragment)
                    .hide(historyFragment)
                    .add(R.id.fr_layout, homeFragment)
                    .commitNow()
            }
            binding.mainBnv.menu.findItem(R.id.homeFragment).isChecked = true
        }
    }

    fun initAlarmAdapter(alarmList : ArrayList<Alarm>){
        val adapter = AlarmRVAdapter(alarmList)
        adapter.setMyItemCLickLister(object:AlarmRVAdapter.AlarmItemClickListener{
            override fun onClickItem(alarm: Alarm, position: Int) {
                val notificationDatabase = NotificationDatabase.getInstance(this@MainActivity)!!
                notificationDatabase.NotificationDao().setIsChecked(alarm.alarmIdx)
                alarmService.getAlarmInfo(alarm.alarmIdx, getUserIdx())
                binding.mainLayout.closeDrawers()
                adapter.remove(position)
            }
        })
        binding.sidebarNotifyRv.adapter=adapter
    }



    override fun onRestart() {
        super.onRestart()
        binding.mainLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
    }

    override fun onGetAlarmListSuccess(result: ArrayList<Alarm>) {
        val notificationDatabase = NotificationDatabase.getInstance(this)!!
        result.map { i->run{
            notificationDatabase.NotificationDao().insert(NotificationDTO(i.alarmIdx,i.content,i.createdAt,false))
        } }
        initAlarmAdapter(result)
    }

    override fun onGetAlarmListFailure(code: Int, message: String) {
        Log.e("AlarmList-Fail","${code} -> ${message}")
    }

    override fun onGetAlarmInfoViewSuccess(item : AlarmInfo) {

        when(item.alarmType){
            "notice"-> {
                startNextActivity(NoticeActivity::class.java)
            }
            "letter"->{
                letterService.loadLetter(getUserIdx(),"letter",item.reqParamIdx)
            }
            "diary"->{
                diaryService.loadDiary(getUserIdx(),"diary",item.reqParamIdx)
            }
            "reply"->{
                replyService.loadReply(getUserIdx(),"reply",item.reqParamIdx)
            }

        }
    }

    override fun onGetAlarmInfoFailure(code: Int, message: String) {
        Log.e("AlarmInfo-Fail","${code} -> ${message}")
    }

    override fun onDiaryLoading() {

    }

    override fun onDiarySuccess(resp: MailDiaryResponse) {
        val doneList :List<String> = resp.mail.doneList.map{donelist ->donelist.content}
        val diary = DiaryViewerInfo(resp.senderNickName,resp.mail.emotionIdx,resp.mail.diaryDate,resp.mail.content,true,doneList.toArrayList())
        val intent = Intent(this@MainActivity,DiaryViewerActivity::class.java)
        intent.putExtra("diaryInfo",diary)
        startActivity(intent)
    }

    override fun onDiaryFailure(code: Int, message: String) {
        Log.e("Alarm_Diary","${code} -> ${message}")
    }

    override fun onLetterLoading() {

    }

    override fun onLetterSuccess(letter: MailLetterResponse) {
         val bundle = bundleOf("letter" to  letter , "date" to "임시 데이터")
         val intent = Intent(this@MainActivity,MailViewActivity::class.java)
         intent.putExtra("MailView",bundle)
         startActivity(intent)
    }

    override fun onLetterFailure(code: Int, message: String) {
        Log.e("Alarm_Letter","${code} -> ${message}")
    }

    override fun onReplyLoading() {
        TODO("Not yet implemented")
    }

    override fun onReplySuccess(reply: MailReplyResponse) {
        val bundle =bundleOf(
            "date" to "임시 데이터",
            "reply" to reply
        )
        val intent = Intent(this, MailReplyActivity::class.java)
        intent.putExtra("MailReply",bundle)
        startActivity(intent)
    }

    override fun onReplyFailure(code: Int, message: String) {
        Log.e("Alarm_Reply","${code} -> ${message}")
    }


}