package com.likefirst.btos.ui.home


import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.fragment.app.commit
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.likefirst.btos.R
import com.likefirst.btos.databinding.FragmentHomeBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.ui.posting.DiaryActivity
import com.likefirst.btos.utils.dateToString
import com.likefirst.btos.utils.getLastPostingDate
import java.time.LocalTime
import java.util.*


public class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
   var isMailboxOpen =false

    override fun initAfterBinding() {

        val mActivity = activity as MainActivity

        initFlowerPot()
      
        binding.homeNotificationBtn.setOnClickListener {
            if(!mActivity.mailOpenStatus())mActivity.notifyDrawerHandler("open")

        }

        binding.homeMailBtn.setOnClickListener {

            mActivity.isMailOpen = true
            mActivity.notifyDrawerHandler("lock")

            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.home_mailbox_layout, MailViewActivity(), "mailbox")
                .addToBackStack(null)
                .show(MailViewActivity())
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
        val animationView: LottieAnimationView = binding.lottieAnimation
        val lastPostingDate = getLastPostingDate()
        Log.d("lastPostingDate", lastPostingDate.toString())
        val mCalendar = GregorianCalendar.getInstance()
        val currentMillis = mCalendar.timeInMillis
        mCalendar.set(Calendar.YEAR, 2000)
        val lastMillis = mCalendar.timeInMillis
        val diffPostingDate = (currentMillis - lastMillis) / 1000 / (24*60*60)
        if (diffPostingDate >= 5){
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
    }

    fun initSadPot(animationView: LottieAnimationView){
        animationView.setAnimation("alocasia_sad_3.json")
        //Google Admob 구현
        MobileAds.initialize(requireContext())

        // 테스트 기기 추가
        // TODO: 실제로 앱 배포할 때에는 테스트 기기 추가하는 코드를 지워야 한다.
        val testDeviceIds = arrayListOf("1FA90365DB7395FC489D988564B3F2D7")
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(testDeviceIds)
                .build()
        )
        animationView.setOnClickListener {
            loadInterstitialAd()
        }
    }

    fun loadInterstitialAd(){
        val mRewardedVideoAd = RewardedAd(requireContext(), "ca-app-pub-3940256099942544/5224354917")
        val adLoadCallback = object: RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                // Ad successfully loaded.
                Log.d("rewardLoadSuccess", "yeeeeeeeeeeee")
            }
            override fun onRewardedAdFailedToLoad(adError: LoadAdError) {
                // Ad failed to load.
                Log.e("rewardLoadError", adError.toString())
            }
        }
        mRewardedVideoAd.loadAd(AdRequest.Builder().build(), adLoadCallback)

        if (mRewardedVideoAd.isLoaded) {
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
                    Log.d("reward", reward.amount.toString())
                    Log.d("reward", reward.type.toString())
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
}