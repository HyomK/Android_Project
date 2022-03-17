package com.likefirst.btos.data.remote.viewer.service

import android.util.Log
import com.likefirst.btos.ApplicationClass.Companion.retrofit
import com.likefirst.btos.data.remote.viewer.response.ArchiveList
import com.likefirst.btos.data.remote.viewer.view.ArchiveListView
import com.likefirst.btos.presentation.view.archive.ArchiveListRVAdapter
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class ArchiveListService {
    private lateinit var archiveListView: ArchiveListView
    // Retrofit 객체 생성
    val archiveListService = retrofit.create(RetrofitInterface::class.java)

    fun setArchiveListView(archiveListView: ArchiveListView){
        this.archiveListView = archiveListView
    }

    fun getList(userIdx : Int, pageNum : Int, query : HashMap<String, String>, adapter : ArchiveListRVAdapter){
        archiveListView.onArchiveListLoading()

        archiveListService.getArchiveList(userIdx, pageNum, query).enqueue(object : Callback<ArchiveList>{
            override fun onResponse(call: Call<ArchiveList>, response: Response<ArchiveList>) {
                val resp = response.body()!!
                Log.d("resp", resp.toString())
                Log.d("query", query.toString())
                Log.d("pageNum", pageNum.toString())
                when (resp.code){
                    1000 -> archiveListView.onArchiveListSuccess(resp.result, resp.pageInfo, adapter)
                    else -> archiveListView.onArchiveListFailure(resp.code)
                }
            }

            override fun onFailure(call: Call<ArchiveList>, t: Throwable) {
                Log.d("ArchiveListService / getList Failure", t.toString())
                archiveListView.onArchiveListFailure(400)
            }
        })
    }
}