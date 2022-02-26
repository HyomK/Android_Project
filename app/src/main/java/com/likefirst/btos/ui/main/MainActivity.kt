package com.likefirst.btos.ui.main


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.firebase.NotificationDTO
import com.likefirst.btos.data.local.NotificationDatabase
import com.likefirst.btos.data.remote.notify.response.NoticeDetailResponse
import com.likefirst.btos.databinding.ActivityMainBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.archive.ArchiveFragment
import com.likefirst.btos.ui.history.HistoryFragment
import com.likefirst.btos.ui.home.HomeFragment
import com.likefirst.btos.ui.home.MailViewActivity
import com.likefirst.btos.ui.profile.ProfileFragment
import com.likefirst.btos.ui.profile.setting.NoticeActivity
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.likefirst.btos.data.entities.DiaryViewerInfo
import com.likefirst.btos.data.remote.notify.response.Alarm
import com.likefirst.btos.data.remote.notify.response.AlarmInfo
import com.likefirst.btos.data.remote.notify.service.AlarmService
import com.likefirst.btos.data.remote.notify.view.*
import com.likefirst.btos.data.remote.posting.response.MailInfoResponse
import com.likefirst.btos.data.remote.posting.service.DiaryService
import com.likefirst.btos.data.remote.posting.service.MailLetterService
import com.likefirst.btos.data.remote.posting.service.MailReplyService
import com.likefirst.btos.data.remote.posting.view.MailDiaryView
import com.likefirst.btos.data.remote.posting.view.MailLetterView
import com.likefirst.btos.data.remote.posting.view.MailReplyView
import com.likefirst.btos.ui.history.HistoryUpdateFragment
import com.likefirst.btos.ui.posting.DiaryViewerActivity
import com.likefirst.btos.ui.posting.MailReplyActivity
import com.likefirst.btos.utils.Model.LiveSharedPreferences
import com.likefirst.btos.utils.ViewModel.SharedNotifyModel
import com.likefirst.btos.utils.getUserIdx


class MainActivity: BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),AlarmInfoView,AlarmListView,MailDiaryView, MailLetterView, MailReplyView{

    private var auth : FirebaseAuth? = null

    private val homeFragment = HomeFragment()
    private val archiveFragment = ArchiveFragment()
    private val historyFragment = HistoryFragment()
    private val historyUpdateFragment = HistoryUpdateFragment()
    private val profileFragment= ProfileFragment()
    private var backPressedMillis : Long = 0

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
        setNotificationIcon()

    }

    fun setNotificationIcon(){
        sharedNotifyModel= ViewModelProvider(this).get(SharedNotifyModel::class.java)
        val isNewUser = intent.getBooleanExtra("isNewUser",false)
        Log.e("isNewUser",isNewUser.toString())
        if(isNewUser){
            sharedNotifyModel.setMsgLiveData(true)
            sharedNotifyModel.setNoticeLiveData(false)
            intent.removeExtra("isNewUser")
            return

        }
        val spf = getSharedPreferences("notification", MODE_PRIVATE) // 기존에 있던 데이터
        val liveSharedPreference = LiveSharedPreferences(spf)
        liveSharedPreference.getString("newNotification", "undefine")
            .observe(this, Observer<String> { result ->
                if( result!="undefine"){
                    sharedNotifyModel.setNoticeLiveData(true)
                }else{
                    sharedNotifyModel.setNoticeLiveData(false)
                }
            })
        liveSharedPreference.getString("newMail", "undefine")
            .observe(this, Observer<String> { result ->
                if( result!="undefine"){
                    sharedNotifyModel.setMsgLiveData(true)
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
                val spf = getSharedPreferences("notification", MODE_PRIVATE)
                spf.edit().putString("newNotification","undefine").apply()
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
                && intent.getIntExtra("diaryStateFlag", -1) == DiaryViewerActivity.UPDATE
                && intent.getIntExtra("position", -1) >= 0){
                val intentDataset = intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo")!!
                val position = intent.getIntExtra("position", -1)
                val mArchiveFragment: ArchiveFragment = supportFragmentManager.findFragmentByTag("archive") as ArchiveFragment
                mArchiveFragment.listPage.mAdapter.updateList(position, intentDataset.doneLists.size, intentDataset.emotionIdx, intentDataset.contents)
            }
            // 달력에서 일기 수정이 일어난 경우 (리스트 새로 갱신, 달력 현재 페이지 갱신)
            else if (intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo") != null
                && intent.getIntExtra("diaryStateFlag", -1) == DiaryViewerActivity.UPDATE
                && intent.getIntExtra("position", -1) == -1){
                    if(archiveFragment.isAdded){
                        reLoadArchiveList()
                        reLoadArchiveCalendar()
                    }
            }
            // 일기가 작성된 경우 (리스트 새로 갱신, 달력 현재 페이지 갱신)
            else if (intent.getParcelableExtra<DiaryViewerInfo>("diaryInfo") != null
                && intent.getIntExtra("diaryStateFlag", -1) == DiaryViewerActivity.CREATE){
                if(archiveFragment.isAdded){
                    reLoadArchiveList()
                    reLoadArchiveCalendar()
                }
            }
            else if (intent.getIntExtra("diaryStateFlag", -1) == DiaryViewerActivity.DELETE){
                //TODO: 삭제 로직 구현(리스트 케이스 추가해야함)
                if(archiveFragment.isAdded){
                    reLoadArchiveList()
                    reLoadArchiveCalendar()
                }
            }
        }
        super.onNewIntent(intent)
    }

    fun reLoadArchiveList(){
        val mArchiveFragment: ArchiveFragment = supportFragmentManager.findFragmentByTag("archive") as ArchiveFragment
        mArchiveFragment.listPage.reLoadDiaryList(mArchiveFragment.listPage.mAdapter, HashMap())
    }

    fun reLoadArchiveCalendar(){
        val mArchiveFragment: ArchiveFragment = supportFragmentManager.findFragmentByTag("archive") as ArchiveFragment
        var viewMode = 0
        val radioGroup = findViewById<RadioGroup>(R.id.archive_calendar_rg)
        when (radioGroup.checkedRadioButtonId){         // 라디오버튼에 따라서 viewMode 변경
            R.id.archive_calendar_done_list_rb -> viewMode = 0
            R.id.archive_calendar_emotion_rb -> viewMode = 1
        }
        mArchiveFragment.calendarPage.initCalendar(viewMode, true)
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
            if(System.currentTimeMillis() > backPressedMillis + 2000){
                backPressedMillis = System.currentTimeMillis()
                Snackbar.make(binding.frLayout, "진짜 갈꺼야...?", Snackbar.LENGTH_SHORT).show()
                return
            } else {
                finish()
            }
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
                    .hide(historyUpdateFragment)
                    .commitNow()
            } else {
                supportFragmentManager.beginTransaction()
                    .hide(archiveFragment)
                    .hide(profileFragment)
                    .hide(historyUpdateFragment)
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

    override fun onDiarySuccess(resp: MailInfoResponse) {
        val diary = DiaryViewerInfo(resp.senderNickName,resp.emotionIdx,resp.sendAt,resp.content!!,true,resp.doneList!!)
        val intent = Intent(this@MainActivity,DiaryViewerActivity::class.java)
        intent.putExtra("diaryInfo",diary)
        startActivity(intent)
    }

    override fun onDiaryFailure(code: Int, message: String) {
        Log.e("Alarm_Diary","${code} -> ${message}")
    }

    override fun onLetterLoading() {

    }

    override fun onLetterSuccess(letter:MailInfoResponse) {
         val bundle = bundleOf("mail" to  letter)
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

    override fun onReplySuccess(reply:MailInfoResponse) {
        val bundle = bundleOf("mail" to  reply)
        val intent = Intent(this@MainActivity,MailViewActivity::class.java)
        intent.putExtra("MailView",bundle)
        startActivity(intent)
    }

    override fun onReplyFailure(code: Int, message: String) {
        Log.e("Alarm_Reply","${code} -> ${message}")
    }


}