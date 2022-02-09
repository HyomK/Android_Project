package com.likefirst.btos.ui.profile.setting

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.UserAge
import com.likefirst.btos.data.entities.UserOther
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.users.response.BlackList
import com.likefirst.btos.data.remote.users.response.BlockUser
import com.likefirst.btos.data.remote.users.service.BlackListService
import com.likefirst.btos.data.remote.users.service.SettingUserService
import com.likefirst.btos.data.remote.users.view.BlackListView
import com.likefirst.btos.data.remote.users.view.SetBlockView
import com.likefirst.btos.data.remote.users.view.SetSettingUserView
import com.likefirst.btos.data.remote.users.view.UnblockView
import com.likefirst.btos.databinding.FragmentSetNotificationBinding
import com.likefirst.btos.ui.BaseFragment
import com.likefirst.btos.ui.main.CustomDialogFragment
import com.likefirst.btos.ui.main.EditDialogFragment
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.utils.toArrayList

class SetNotificationFragment:BaseFragment<FragmentSetNotificationBinding>(FragmentSetNotificationBinding::inflate)
    ,MainActivity.onBackPressedListener, SetSettingUserView ,BlackListView ,UnblockView{

    private var isbtn1Touched : Boolean = false
    private var isbtn2Touched : Boolean = false
    private var btn1 : Boolean = false
    private var btn2 : Boolean = false

    override fun initAfterBinding() {
        val userDatabase = UserDatabase.getInstance(requireContext())!!
        btn1 = userDatabase.userDao().getRecOthers()!!
        btn2 = userDatabase.userDao().getRecSimilarAge()!!
        val settingService = SettingUserService()

        val blackListService= BlackListService()
        blackListService.setBlackListView(this)
        val userIdx = UserDatabase.getInstance(requireContext())!!.userDao().getUserIdx()!!
        blackListService.getBlackList(userIdx)


        //initToggle
        Log.e("NOTIFICATION Other Age",btn1.toString()+btn2.toString())
        initToggle(btn1,binding.setNotifyBtn,  binding.setNotifyToggleIv)
        initToggle( btn2 ,binding.setNotifyAgeBtn,  binding.setNotifyAgeToggleIv)

        binding.setNotifyBtn.setOnClickListener {
            isbtn1Touched = !isbtn1Touched
            btn1= pushToggleSwitcher( btn1 ,binding.setNotifyBtn,  binding.setNotifyToggleIv)
        }

        binding.setNotifyAgeBtn.setOnClickListener {
            isbtn2Touched = !isbtn2Touched
            btn2= pushToggleSwitcher( btn2 ,binding.setNotifyAgeBtn,  binding.setNotifyAgeToggleIv)
        }

        binding.setNotifyToolbar.toolbarBackIc.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.setNotifyDoneBtn.setOnClickListener {
            if(isbtn1Touched){
                //다른사람수신설정
                settingService.setSettingUserView(this)
                settingService.setNotificationOther(userDatabase.userDao().getUserIdx(), UserOther(btn1))
            }
            if(isbtn2Touched){
                //비슷한 연령대 수신 설정
                settingService.setSettingUserView(this)
                settingService.setNotificationAge(userDatabase.userDao().getUserIdx(), UserAge(btn2))
            }
            if(!isbtn1Touched && !isbtn2Touched){
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }


    fun pushToggleSwitcher(status: Boolean , btn : ImageView, bg:ImageView):Boolean {
        if (status) {
            btn.setImageResource(R.drawable.ic_toggle_false)
            bg.visibility = View.INVISIBLE
        } else {
            bg.visibility= View.VISIBLE
           // bg.setImageResource(R.drawable.select_toggle)
            btn.setImageResource(R.drawable.ic_toggle_true)
        }
        return !status
    }

    fun initBlackList(list : ArrayList<BlockUser>){
        val arraylist = arrayListOf<String>("user1","user2","user3")
        val adapter = BlackListRVAdapter(list)
        val blackListService= BlackListService()
        blackListService.setBlackListView(this)
        blackListService.setUnBlockView(this)
        val userIdx = UserDatabase.getInstance(requireContext())!!.userDao().getUserIdx()!!

        adapter.setOnListener(object:BlackListRVAdapter.BlackListListener {
            override fun removeUser(user: BlockUser,pos: Int) {

                val dialog = CustomDialogFragment()
                val btn = arrayOf("취소", "차단해제")
                dialog.arguments = bundleOf(
                    "bodyContext" to "차단을 해제하시겠습니까?",
                    "btnData" to btn
                )
                // 버튼 클릭 이벤트 설정
                dialog.setButtonClickListener(object : CustomDialogFragment.OnButtonClickListener {
                    override fun onButton1Clicked() {}
                    override fun onButton2Clicked() {
                        blackListService.unBlock(user.blockIdx)
                        adapter.removeItem(pos)
                    }
                })
                dialog.show(requireActivity().supportFragmentManager, "notification")
            }
        })
        binding.setNotifyRv.adapter=adapter

    }

    fun initToggle(status: Boolean , btn : ImageView, bg:ImageView) {
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
        binding.setNotifyLoadingPb.visibility = View.VISIBLE
    }

    override fun onSetSettingUserViewSuccess(result: String) {
        binding.setNotifyLoadingPb.visibility = View.GONE
        val userDatabase = UserDatabase.getInstance(requireContext())!!
        if (isbtn1Touched) {
            userDatabase.userDao().updateRecOthers(btn1)
            if (!isbtn2Touched) {
                settingDialog(requireActivity(), this@SetNotificationFragment)
            }else{
                isbtn1Touched = false
            }
        }
        else if (isbtn2Touched) {
            userDatabase.userDao().updateRecSimilarAge(btn2)
            settingDialog(requireActivity(),this@SetNotificationFragment)
        }
    }

    override fun onSetSettingUserViewFailure(code: Int, message: String) {
        binding.setNotifyLoadingPb.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onUnBlockViewSuccess(result: String) {

    }

    override fun onUnBlockViewFailure(code: Int, message: String) {
        TODO("Not yet implemented")
    }

    override fun onGetBlockListViewSuccess(result: ArrayList<BlackList>) {

        val BlockUserList :List<BlockUser> = result.map{
            i->BlockUser(i.blockIdx,i.blockedUserIdx,"user")
        }

        initBlackList(BlockUserList.toArrayList())
    }

    override fun onGetBlockListViewFailure(code: Int, message: String) {
        Log.e("Blacklsit", "조회 실패")
    }

}