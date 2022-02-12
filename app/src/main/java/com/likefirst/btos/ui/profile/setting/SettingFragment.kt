package com.likefirst.btos.ui.profile.setting

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.UserLeave
import com.likefirst.btos.data.entities.UserPush
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.users.service.SettingUserService
import com.likefirst.btos.data.remote.users.view.SetSettingUserView
import com.likefirst.btos.databinding.FragmentSettingBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.main.EditDialogFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.utils.getGSO
import com.likefirst.btos.utils.removeJwt
import kotlin.system.exitProcess

class SettingFragment:BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate)
    , MainActivity.onBackPressedListener, SetSettingUserView {

    val settingService = SettingUserService()
    var isDeleted : Boolean = false
    var isPush : Boolean = false

    override fun initAfterBinding() {
        val userDatabase = UserDatabase.getInstance(requireContext())!!
        var btnPush = userDatabase.userDao().getPushAlarm()!!

        binding.settingToolbar.toolbarBackIc.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.settingName.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fr_layout, SetNameFragment(),"setName")
                .show(SetNameFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.settingBirth.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fr_layout, SetBirthFragment(),"setBirth")
                .show(SetBirthFragment())
                .addToBackStack(null)
                .commit()

        }
        binding.settingFont.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fr_layout, SetFontFragment(),"setFont")
                .show(SetFontFragment())
                .addToBackStack(null)
                .commit()

        }
        binding.settingAppinfo.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fr_layout, AppInfoFragment(),"setAppinfo")
                .show(AppInfoFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.settingLogout.setOnClickListener {
            val dialog = CustomDialogFragment()
            val btn= arrayOf("취소","로그아웃")
            dialog.arguments= bundleOf(
                "bodyContext" to "로그아웃 하시겠습니까?\n 그동안 데이터는 연동되었던 계정에 보관됩니다",
                "btnData" to btn
            )
            // 버튼 클릭 이벤트 설정
            dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener {
                override fun onButton1Clicked(){
                }
                override fun onButton2Clicked() {
                    val gso = getGSO()
                    val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                    googleSignInClient.signOut()
                    removeJwt()
                    userDatabase.userDao().delete(userDatabase.userDao().getUser())

                    //해당 앱의 루트 액티비티를 종료시킨다.
                    val mActivity = activity as MainActivity
                     if(Build.VERSION.SDK_INT >= 16){
                         mActivity.finishAffinity()
                    }else {
                         ActivityCompat.finishAffinity(mActivity)
                     }
                    System.runFinalization() //현재 작업중인 쓰레드가 다 종료되면, 종료 시키라는 명령어
                    exitProcess(0) // 현재 액티비티를 종료시킨다.
                }
            })
            dialog.show(requireActivity().supportFragmentManager, "CustomDialog")

        }
        binding.settingNotification.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fr_layout, SetNotificationFragment(),"setNotify")
                .show(SetNotificationFragment())
                .addToBackStack(null)
                .commit()
        }

        //initPushButton
        initToggle(btnPush, binding.settingToggleIv,  binding.settingToggleSelector)
        isPush = btnPush
        binding.settingPush.setOnClickListener {
            btnPush=pushToggleSwitcher(btnPush)
            isPush = btnPush
            settingService.setSettingUserView(this)
            settingService.setPushAlarm(userDatabase.userDao().getUserIdx(), UserPush(btnPush))
        }
        binding.settingSecession.setOnClickListener {
            checkSecession()
        }
    }

    fun checkSecession(){
        val secessionDialog = CustomDialogFragment()
        val btn= arrayOf("취소","탈퇴")
        secessionDialog.arguments= bundleOf(
            "bodyContext" to "정말 계정을 삭제하시겠습니까?\n삭제한 데이터는 다시 찾을 수 없습니다.",
            "btnData" to btn
        )
        secessionDialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener {
            override fun onButton1Clicked(){
            }
            override fun onButton2Clicked() {

                val dialog =EditDialogFragment()
                val btn= arrayOf("취소","탈퇴")
                dialog.arguments= bundleOf(
                    "bodyContext" to "정말 계정삭제를 원하신다면 현재 닉네임을 입력해주세요\n이후 데이터는 모두 사라집니다",
                    "btnData" to btn
                )
                // 버튼 클릭 이벤트 설정
                dialog.setButtonClickListener(object: EditDialogFragment.OnButtonClickListener {
                    override fun onButton1Clicked(){}

                    override fun onButton2Clicked(){
                    }
                    override fun onEditHandler(name:String) {
                        val dialog = CustomDialogFragment()
                        val btn= arrayOf("확인")
                        val userDatabase = UserDatabase.getInstance(requireContext())!!
                        if(name == userDatabase.userDao().getNickName()){ // username dummy
                            dialog.arguments= bundleOf(
                                "bodyContext" to "탈퇴 되었습니다",
                                "btnData" to btn
                            )
                            isDeleted = true
                            settingService.setSettingUserView(this@SettingFragment)
                            settingService.leave(userDatabase.userDao().getUserIdx(), UserLeave("deleted"))
                        }else{
                            dialog.arguments= bundleOf(
                                "bodyContext" to "닉네임이 일치하지 않습니다",
                                "btnData" to btn
                            )
                        }
                        dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener {
                            override fun onButton1Clicked(){
                                if(isDeleted){
                                    val gso = getGSO()
                                    val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                                    googleSignInClient.signOut()
                                    removeJwt()
                                    userDatabase.userDao().delete(userDatabase.userDao().getUser())
                                    //해당 앱의 루트 액티비티를 종료시킨다.
                                    val mActivity = activity as MainActivity
                                    if(Build.VERSION.SDK_INT >= 16){
                                        mActivity.finishAffinity()
                                    }else {
                                        ActivityCompat.finishAffinity(mActivity)
                                    }
                                    System.runFinalization() //현재 작업중인 쓰레드가 다 종료되면, 종료 시키라는 명령어
                                    exitProcess(0) // 현재 액티비티를 종료시킨다.
                                }
                            }
                            override fun onButton2Clicked() {
                            }
                        })
                        dialog.show(requireActivity().supportFragmentManager, "CustomDialog")
                    }
                })
                dialog.show(requireActivity().supportFragmentManager, "EditDialog")
            }
        })
        secessionDialog.show(requireActivity().supportFragmentManager, "CustomDialog")
    }


    fun pushToggleSwitcher(status: Boolean):Boolean {
        if (status) {
            binding.settingToggleIv.setImageResource(R.drawable.ic_toggle_false)
            binding.settingToggleSelector.visibility = View.INVISIBLE
        } else {
            binding.settingToggleIv.setImageResource(R.drawable.ic_toggle_true)
            binding.settingToggleSelector.visibility = View.VISIBLE
        }
        return !status
    }

    fun initToggle(status: Boolean, btn : ImageView, bg: ImageView) {
        if (status) {
            bg.visibility= View.VISIBLE
            // bg.setImageResource(R.drawable.select_toggle)
            btn.setImageResource(R.drawable.ic_toggle_true)
        } else {
            btn.setImageResource(R.drawable.ic_toggle_false)
            bg.visibility = View.INVISIBLE
        }
    }

    override fun onBackPressed() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onSetSettingUserViewLoading() {
        binding.setLoadingPb.visibility = View.VISIBLE
    }

    override fun onSetSettingUserViewSuccess(result: String) {

        if(isDeleted){
            val gso = getGSO()
            val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
            googleSignInClient.signOut()
            removeJwt()
        }else{
            val userDatabase = UserDatabase.getInstance(requireContext())!!
            binding.setLoadingPb.visibility = View.GONE
            userDatabase.userDao().updatePushAlarm(isPush)

            val dialog = CustomDialogFragment()
            val data = arrayOf("확인")
            dialog.arguments= bundleOf(
                "bodyContext" to "성공적으로 변경되었습니다.",
                "btnData" to data
            )
            dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener{
                override fun onButton1Clicked() {
                }
                override fun onButton2Clicked() {
                }
            })
            dialog.show(this.parentFragmentManager, "settingSuccess")

        }
    }

    override fun onSetSettingUserViewFailure(code: Int, message: String) {
        binding.setLoadingPb.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}