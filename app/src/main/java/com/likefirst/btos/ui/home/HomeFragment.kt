package com.likefirst.btos.ui.home


import android.content.Intent
import android.os.Build
import android.service.autofill.UserData
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.UserIsSad
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.users.service.UpdateUserService
import com.likefirst.btos.data.remote.users.view.UpdateIsSadView
import com.likefirst.btos.databinding.FragmentHomeBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.ui.posting.DiaryActivity
import com.likefirst.btos.utils.dateToString
import com.likefirst.btos.utils.getLastPostingDate
import com.likefirst.btos.utils.saveLastPostingDate
import java.time.LocalTime
import java.util.*
import kotlin.collections.HashMap


public class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate), UpdateIsSadView {
    var isMailboxOpen =false

    override fun initAfterBinding() {

        val mActivity = activity as MainActivity
        val updateUserService = UpdateUserService()
        updateUserService.setUpdateIsSadView(this)

        initFlowerPot()
      
        binding.homeNotificationBtn.setOnClickListener {
            if(!mActivity.mailOpenStatus())mActivity.notifyDrawerHandler("open")

        }

        binding.homeMailBtn.setOnClickListener {

            mActivity.isMailOpen = true
            mActivity.notifyDrawerHandler("lock")

            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.home_mailbox_layout, MailboxFragment(), "mailbox")
                .addToBackStack(null)
                .show(MailboxFragment())
                .commit()
            val item =  requireActivity().supportFragmentManager.fragments
            Log.d("homeSTACK","homeitem  ${item.toString()} }")
        }

        binding.homeWriteBtn.setOnClickListener {
            val date = dateToString(Date())
            val intent = Intent(requireContext(), DiaryActivity::class.java)
            intent.putExtra("diaryDate", date)
            startActivity(intent)
        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        Log.d("home","onhidden ${isHidden } mailopen ${isMailboxOpen }")
        val mActivity = activity as MainActivity

        if(isHidden || mActivity.isMailOpen ){
            requireActivity().supportFragmentManager.commit {
                requireActivity().supportFragmentManager.findFragmentByTag("mailbox")?.let { remove(it) }
                requireActivity().supportFragmentManager.findFragmentByTag("writemail")?.let { remove(it) }
                requireActivity().supportFragmentManager.findFragmentByTag("viewmail")?.let { remove(it) }
            }
            mActivity.isMailOpen=false
            mActivity.notifyDrawerHandler("lock")
        }else{
            mActivity.notifyDrawerHandler("unlock")
            binding.homeNotificationBtn.isClickable =true

        }
    }

    fun initFlowerPot(){
        val userDB = UserDatabase.getInstance(requireContext())?.userDao()
        val animationView: LottieAnimationView = binding.lottieAnimation
        val lastPostingDate = getLastPostingDate()
        val mCalendar = GregorianCalendar.getInstance()
        val currentMillis = mCalendar.timeInMillis
        mCalendar.time = lastPostingDate
        val lastMillis = mCalendar.timeInMillis
        val diffPostingDate = (currentMillis - lastMillis) / 1000 / (24*60*60)
        if (userDB!!.getIsSad() || diffPostingDate >= 5){
            userDB.updateIsSad(true)
            initSadPot(animationView)
        } else {
            initHappyPot(animationView)
        }
        // TODO: 서버 반영해서 유저가 선택한 화분에 따라서 표시되게 변경, 현재는 더미데이터일 뿐임
    }

    fun initHappyPot(animationView: LottieAnimationView){
        animationView.setAnimation("alocasia_3.json")
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.repeatMode = LottieDrawable.RESTART
        animationView.playAnimation()
        animationView.setOnClickListener {

        }
    }

    fun initSadPot(animationView: LottieAnimationView){
        animationView.setAnimation("alocasia_sad_3.json")
        //Google Admob 구현
        MobileAds.initialize(requireContext())
        // 테스트 기기 추가
        // TODO: 실제로 앱 배포할 때에는 테스트 기기 추가하는 코드를 지워야 합니다.
        val testDeviceIds = arrayListOf("1FA90365DB7395FC489D988564B3F2D7")
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(testDeviceIds)
                .build()
        )
        val mRewardedVideoAd = RewardedAd(requireContext(), "ca-app-pub-3940256099942544/5224354917")
        val adLoadCallback = object: RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                // Ad successfully loaded.
                Log.d("rewardLoadSuccess", "Reward Loading Successed!!!")
            }
            override fun onRewardedAdFailedToLoad(adError: LoadAdError) {
                // Ad failed to load.
                Log.e("rewardLoadError", adError.toString())
            }
        }
        mRewardedVideoAd.loadAd(AdRequest.Builder().build(), adLoadCallback)
        animationView.setOnClickListener {
            showUpdateSadPotDialog(mRewardedVideoAd)
        }
    }

    fun loadInterstitialAd(mRewardedVideoAd : RewardedAd){
        if (mRewardedVideoAd.isLoaded) {
            val userDB = UserDatabase.getInstance(requireContext())!!.userDao()
            val activityContext = context as MainActivity
            val adCallback = object: RewardedAdCallback() {
                override fun onRewardedAdOpened() {
                    // Ad opened.
                }
                override fun onRewardedAdClosed() {
                    // Ad closed.
                }
                override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                    // User earned reward.
                    val updateUserService = UpdateUserService()
                    updateUserService.setUpdateIsSadView(this@HomeFragment)
                    updateUserService.updateIsSad(userDB.getUser().userIdx!!, UserIsSad(false))
                }
                override fun onRewardedAdFailedToShow(adError: AdError) {
                    // Ad failed to display.
                }
            }
            mRewardedVideoAd.show(activityContext, adCallback)
        }
        else {
            Log.d("GoogleAd Not Loadded", "The rewarded ad wasn't loaded yet.")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        setWindowImage()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        val mActivity = activity as MainActivity
        setWindowImage()
        binding.homeNotificationBtn.isClickable =true
        isMailboxOpen=false
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun setWindowImage(){
        val current : LocalTime = LocalTime.now()
        val now = current.hour
        Log.d("window", now.toString())

        if(now in 6..18){
            binding.windowIv.setImageResource(R.drawable.window_morning)
        }else if(now in 18..20) {
            binding.windowIv.setImageResource(R.drawable.window_afternoon)
        }else{
            binding.windowIv.setImageResource(R.drawable.window_night)
        }
    }

    fun showUpdateSadPotDialog(mRewardedVideoAd: RewardedAd){
        val dialog = CustomDialogFragment()
        val data = arrayOf("취소", "확인")
        dialog.arguments= bundleOf(
            "bodyContext" to "시무룩이 상태에서는 화분이 더이상 성장하지 않습니다. 시무룩이 상태를 해제할까요? (광고영상이 하나 재생돼요!)",
            "btnData" to data
        )
        dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
            override fun onButton1Clicked() {

            }
            override fun onButton2Clicked() {
                loadInterstitialAd(mRewardedVideoAd)
            }
        })
        dialog.show(this.parentFragmentManager, "showUpdateSadPotDialog")
    }

    fun showAdLoadFailedDialog(){
        val dialog = CustomDialogFragment()
        val data = arrayOf("확인")
        dialog.arguments= bundleOf(
            "bodyContext" to "아직 광고가 생성되지 않았어요. 조금뒤에 다시 시도해주세요!!",
            "btnData" to data
        )
        dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
            override fun onButton1Clicked() {

            }
            override fun onButton2Clicked() {

            }
        })
        dialog.show(this.parentFragmentManager, "showAdLoadFailedDialog")
    }

    override fun onUpdateLoading() {
        // TODO: 로딩애니메이션 구현
    }

    override fun onUpdateSuccess(isSad : UserIsSad) {
        val userDB = UserDatabase.getInstance(requireContext())?.userDao()
        userDB!!.updateIsSad(isSad.isSad!!)
        saveLastPostingDate(Date())
        initFlowerPot()
    }

    override fun onUpdateFailure(code: Int, message: String) {
        // TODO: 에러처리
    }
}