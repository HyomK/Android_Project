package com.likefirst.btos.utils

import android.content.Context
import android.content.SharedPreferences
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import com.likefirst.btos.data.entities.BasicHistory
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.PageInfo
import com.likefirst.btos.data.remote.history.service.HistoryService
import com.likefirst.btos.data.remote.history.view.HistoryDiaryandLetterView
import com.likefirst.btos.ui.history.HistoryBasicRecyclerViewAdapter
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
) : MutableLiveData<T>() ,HistoryDiaryandLetterView{

    private var disposable: Disposable? = null
    private val historyService  = HistoryService()
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
                    //loadHistoryList(1, recyclerViewAdapter, userDatabase)
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    override fun onInactive() {
        super.onInactive()
        disposable?.dispose()
    }

    override fun onHistoryDiaryLoading() {
        TODO("Not yet implemented")
    }

    override fun onHistoryDiarySuccess(
        response: BasicHistory<Content>,
        pageInfo: PageInfo,
        recyclerViewAdapter: HistoryBasicRecyclerViewAdapter,
    ) {
        TODO("Not yet implemented")
    }

    override fun onHistoryDiaryFailure(
        code: Int,
        message: String,
        recyclerViewAdapter: HistoryBasicRecyclerViewAdapter,
    ) {
        TODO("Not yet implemented")
    }

}