package com.likefirst.btos.data.remote.users.view

import com.likefirst.btos.data.remote.users.response.BlackList
import com.likefirst.btos.data.remote.users.response.BlockUser

interface BlackListView {
    fun onGetBlockListViewSuccess(result : ArrayList<BlockUser>)
    fun onGetBlockListViewFailure(code : Int, message : String)
};


interface SetBlockView {
    fun onSetBlockViewSuccess(result :  Int)
    fun onSetBlockViewFailure(code : Int, message : String)
};


interface UnblockView {
    fun onUnBlockViewSuccess(result : String)
    fun onUnBlockViewFailure(code : Int, message : String)
}
