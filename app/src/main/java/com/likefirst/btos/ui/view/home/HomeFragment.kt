package com.likefirst.btos.ui.view.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.snackbar.Snackbar
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.UserIsSad
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.module.Plant
import com.likefirst.btos.data.remote.notify.viewmodel.NotifyViewModel
import com.likefirst.btos.data.remote.users.service.UpdateUserService
import com.likefirst.btos.data.remote.users.view.UpdateIsSadView
import com.likefirst.btos.databinding.FragmentHomeBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.view.main.CustomDialogFragment
import com.likefirst.btos.ui.view.main.MainActivity
import com.likefirst.btos.ui.view.posting.DiaryActivity
import com.likefirst.btos.ui.view.posting.MailWriteActivity
import com.likefirst.btos.ui.view.splash.LoginActivity
import com.likefirst.btos.utils.dateToString
import com.likefirst.btos.utils.getLastPostingDate
import com.likefirst.btos.utils.saveLastPostingDate
import com.likefirst.btos.utils.*
import com.likefirst.btos.ui.viewModel.PlantInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime
import java.util.*
import kotlin.system.exitProcess


@AndroidEntryPoint
public class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate), UpdateIsSadView {
    var isMailboxOpen =false
    lateinit var   notifyViewModel : NotifyViewModel
    val plantModel by viewModels<PlantInfoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notifyViewModel= ViewModelProvider(requireActivity()).get(NotifyViewModel::class.java)
        notifyViewModel.getMsgLiveData().observe(viewLifecycleOwner,Observer<Boolean>{
            if(it) binding.homeMailBtn.setImageResource(R.drawable.mailbox_new)
            else binding.homeMailBtn.setImageResource(R.drawable.mailbox)
        })
        notifyViewModel.getNoticeLiveData().observe(viewLifecycleOwner,Observer<Boolean>{
            if(it) binding.homeNotificationBtn.setImageResource(R.drawable.notification_new)
            else binding.homeNotificationBtn.setImageResource(R.drawable.notification)
        })
        val plantName= requireContext().resources.getStringArray(R.array.plantEng)
        plantModel.getCurrentPlantInfo().observe(viewLifecycleOwner,Observer{
                it-> run {
              updatePot(binding.lottieAnimation,plantName[it.plantIdx-1],it.currentLevel)
           }
        })
    }
    override fun initAfterBinding() {
        val mActivity = activity as MainActivity
        val updateUserService = UpdateUserService()
        binding.homeNotificationBtn.setOnClickListener {
            if(!mActivity.mailOpenStatus())mActivity.notifyDrawerHandler("open")
        }
        binding.homeMailBtn.setOnClickListener {
            notifyViewModel.setMsgLiveData(false)
            removeMessage()
            mActivity.isMailOpen = true
            mActivity.notifyDrawerHandler("lock")
            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.home_mailbox_layout, MailboxFragment(), "mailbox")
                .addToBackStack(null)
                .show(MailboxFragment())
                .commit()
        }
        binding.homeSendMailBtn.setOnClickListener {
            val intent = Intent(requireContext(), MailWriteActivity::class.java)
            startActivity(intent)
        }

        binding.homeWriteBtn.bringToFront()
        binding.homeWriteBtn.setOnClickListener {
            val date = dateToString(Date())
            val intent = Intent(requireContext(), DiaryActivity::class.java)
            intent.putExtra("diaryDate", date)
            startActivity(intent)
        }

        updateUserService.setUpdateIsSadView(this) // 처리 순서 변경
        initFlowerPot()


        if(arguments!=null && requireArguments().getBoolean("isNewUser", false)){
            playGuideAnim()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        Log.d("home","onhidden ${isHidden } mailopen ${isMailboxOpen }")
        val mActivity = activity as MainActivity

        if(isHidden || mActivity.isMailOpen ){
            requireActivity().supportFragmentManager.commit {
                requireActivity().supportFragmentManager.findFragmentByTag("mailbox")?.let { remove(it) }
            }
            mActivity.isMailOpen=false
            mActivity.notifyDrawerHandler("lock")
        }else{
            mActivity.notifyDrawerHandler("unlock")
            binding.homeNotificationBtn.isClickable =true

        }
    }

    fun playGuideAnim(){
        val animFadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
        val animFadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        val animFadeIn3000 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_delay3000)
        binding.onBoardingShadowLayout.apply {
            bringToFront()
            visibility = View.VISIBLE
            binding.onBoardingShadowLayout.isClickable = true
        }
        binding.onBoardingLine1.visibility = View.VISIBLE
        binding.onBoardingLine1.startAnimation(animFadeIn)
        binding.onBoardingLine2.visibility = View.VISIBLE
        binding.onBoardingLine2.startAnimation(animFadeIn)
        binding.onBoardingLine3.visibility = View.VISIBLE
        binding.onBoardingLine3.startAnimation(animFadeIn)
        binding.onBoardingText1.visibility = View.VISIBLE
        binding.onBoardingText1.startAnimation(animFadeIn)
        binding.onBoardingText2.visibility = View.VISIBLE
        binding.onBoardingText2.startAnimation(animFadeIn)
        binding.onBoardingText3.visibility = View.VISIBLE
        binding.onBoardingText3.startAnimation(animFadeIn)
        binding.onBoardingText4.visibility = View.VISIBLE
        binding.onBoardingText4.startAnimation(animFadeIn3000)

        val handler= Handler(Looper.getMainLooper())
        handler.postDelayed({
            binding.onBoardingShadowLayout.setOnClickListener {
                binding.onBoardingShadowLayout.visibility = View.GONE
                binding.onBoardingShadowLayout.startAnimation(animFadeOut)
                binding.onBoardingLine1.visibility = View.GONE
                binding.onBoardingLine1.startAnimation(animFadeOut)
                binding.onBoardingLine2.visibility = View.GONE
                binding.onBoardingLine2.startAnimation(animFadeOut)
                binding.onBoardingLine3.visibility = View.GONE
                binding.onBoardingLine3.startAnimation(animFadeOut)
                binding.onBoardingText1.visibility = View.GONE
                binding.onBoardingText1.startAnimation(animFadeOut)
                binding.onBoardingText2.visibility = View.GONE
                binding.onBoardingText2.startAnimation(animFadeOut)
                binding.onBoardingText3.visibility = View.GONE
                binding.onBoardingText3.startAnimation(animFadeOut)
                binding.onBoardingText4.visibility = View.GONE
                binding.onBoardingText4.startAnimation(animFadeOut)
                binding.onBoardingShadowLayout.isClickable = false
            }
        },3000)
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
            // 시무룩이 상태로 RoomDB, 서버의 유저정보 업데이트
            val updateUserService = UpdateUserService()
            updateUserService.setUpdateIsSadView(this)
            updateUserService.updateIsSad(getUserIdx(), UserIsSad(true))
            userDB.updateIsSad(true)
            initSadPot(animationView)
        } else {
            initHappyPot(animationView)
        }
    }

    fun updatePot(animationView: LottieAnimationView, plantName : String, currentLevel : Int){

        val userDB = UserDatabase.getInstance(requireContext())?.userDao()
        var plantStatus = ""
        if(userDB!!.getIsSad()) plantStatus="sad_"
        Log.d("updatePot"," ${plantName}_ ${ plantStatus }_${currentLevel}")
        animationView.setAnimation("${plantName}/${plantName}_${plantStatus}${currentLevel}.json")
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.repeatMode = LottieDrawable.RESTART
        animationView.playAnimation()
        animationView.setOnClickListener {
        }
    }

    fun initHappyPot(animationView: LottieAnimationView) {
        val currentPlant = getCurrentPlant()
        val plantIndex = requireContext().resources.getStringArray(R.array.plantEng)
        val plantName =plantIndex[currentPlant.plantIdx-1]
        animationView.setAnimation("${plantName}/${plantName }_${currentPlant.currentLevel}.json")
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.repeatMode = LottieDrawable.RESTART
        animationView.playAnimation()
        animationView.setOnClickListener {
        }
    }

    fun initSadPot(animationView: LottieAnimationView){
        val currentPlant = getCurrentPlant()
        val plantIndex = requireContext().resources.getStringArray(R.array.plantEng)
        val plantName =plantIndex[currentPlant.plantIdx-1]
        animationView.setAnimation( "${plantName}/${plantName}_sad_${currentPlant.currentLevel}.json")

        MobileAds.initialize(requireContext())
//        val testDeviceIds = arrayListOf("1FA90365DB7395FC489D988564B3F2D7")
        MobileAds.setRequestConfiguration(
              RequestConfiguration.Builder()
//             .setTestDeviceIds(testDeviceIds)
           .build()
           )

          val mRewardedVideoAd = RewardedAd(requireContext(), "ca-app-pub-3439488559531418/3923063443")
    // 테스트 기기 추가
    // TODO: 실제로 앱 배포할 때에는 테스트 기기 추가하는 코드를 지워야 합니다.
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
        //Google Admob 구현


    }
    fun getCurrentPlant(): Plant{
        val currentPlant = plantModel.getSelectedPlant()
        return currentPlant!!
    }

    fun loadInterstitialAd(mRewardedVideoAd : RewardedAd){
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
                    val updateUserService = UpdateUserService()
                    updateUserService.setUpdateIsSadView(this@HomeFragment)
                    updateUserService.updateIsSad(getUserIdx(), UserIsSad(false))
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

    fun getUserIdx() : Int{
        val userDB = UserDatabase.getInstance(requireContext())?.userDao()
        UserDatabase.getInstance(requireContext())!!.userDao()
        Log.d("User", userDB?.getUser().toString())
        val userIdx = userDB!!.getUser().userIdx!!
        return userIdx
    }

    override fun onUpdateLoading() {
        // TODO: 로딩애니메이션 구현
    }

    override fun onUpdateSuccess(isSad : UserIsSad) {
        val userDB = UserDatabase.getInstance(requireContext())?.userDao()
        userDB!!.updateIsSad(isSad.sad!!)
        // 시무룩이 상태로 전환 시에만 lastPostingDate 초기화
        if (isSad.sad!!){
            return
        } else {
            saveLastPostingDate(Date())
            initFlowerPot()
        }
    }

    override fun onUpdateFailure(code: Int, message: String) {
        when (code){
            2002, 2003 -> {
                    // 로그아웃
                    val dialog = CustomDialogFragment()
                    val data = arrayOf("확인")
                    dialog.arguments= bundleOf(
                        "bodyContext" to  "유효하지 않은 회원정보입니다. 다시 로그인 해주세요.",
                        "btnData" to data
                    )
                    dialog.setButtonClickListener(object : CustomDialogFragment.OnButtonClickListener {
                        override fun onButton1Clicked() {
                            val gso = getGSO()
                            val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
                            googleSignInClient.signOut()
                            removeJwt()
                            val intent = Intent(requireContext(), LoginActivity::class.java)
                            startActivity(intent)
                            exitProcess(0)
                        }

                        override fun onButton2Clicked() {

                        }
                    })
                    dialog.show(this.parentFragmentManager, "logoutDialog")
            }
            4000, 5016 -> Snackbar.make(requireView(), "데이터베이스 연결에 실패하였습니다. 다시 시도해 주세요.", Snackbar.LENGTH_SHORT).show()
        }
    }
}