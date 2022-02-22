package com.likefirst.btos.utils.Model

import android.content.Context
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.likefirst.btos.data.entities.BasicHistory
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.PageInfo
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.history.service.LiveHistoryService
import com.likefirst.btos.data.remote.history.view.LiveHistoryDiaryandLetterView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class LiveSearchData<T>  constructor (
    private val updates: Observable<String>,
    private val textWatcher: TextWatcher,
    private val key: String,
    private val defaultValue: T,
    private val context : Context
) : MutableLiveData<T>() , LiveHistoryDiaryandLetterView {

    private var disposable: Disposable? = null
    private val historyService  = LiveHistoryService()
    private val userDatabase = UserDatabase.getInstance(context)!!

    var Liveresult= ArrayList<Content>()
    var LivePageInfo = PageInfo()

    fun initService(){
        historyService.sethistoryDiaryView(this)
    }

    override fun onActive() {
        super.onActive()

        value = defaultValue

        disposable = updates
            .filter { t -> t == key }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<String>() {
                override fun onComplete() {
                }

                override fun onNext(t: String) {
                    loadHistoryList(1, userDatabase)
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    override fun onInactive() {
        super.onInactive()
        disposable?.dispose()
    }
    fun loadHistoryList(pageNum : Int, userDatabase : UserDatabase){
        historyService.sethistoryDiaryView(this)
        Log.e("HISTORYBASIC",
            userDatabase.userDao().getUserIdx().toString() + pageNum.toString() + "filtering")
        historyService.diaryletter(userDatabase.userDao().getUserIdx(), pageNum, "filtering", key)
    }
    override fun onHistoryDiaryLoading() {
        TODO("Not yet implemented")
    }

    override fun onHistoryDiarySuccess(
        response: BasicHistory<Content>,
        pageInfo: PageInfo,
    ) {
        Liveresult = response.list
        LivePageInfo = pageInfo
    }

    override fun onHistoryDiaryFailure(
        code: Int,
        message: String,
    ) {
        TODO("Not yet implemented")
    }

}




