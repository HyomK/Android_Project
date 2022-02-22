package com.likefirst.btos.utils.ViewModel

import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import com.likefirst.btos.utils.Model.LiveSearchData
import io.reactivex.subjects.PublishSubject
import org.w3c.dom.Text

class LiveSearchModel constructor(private var searchText: String?, private val context:Context) :TextWatcher {
    private val publisher = PublishSubject.create<String>()

    private val updates = publisher.doOnSubscribe {

    }.doOnDispose {
        if (!publisher.hasObservers()) {

        }
    }

    fun getString(key: String?, defaultValue: String): LiveSearchData<String> {
        var search =""
        if(searchText!=null)search=searchText!!
        return LiveSearchData<String>(updates,this,search,"",context)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
        Log.e("TEXT밖",text.toString())
        if(text != ""){
            searchText = text.toString()
            Log.e("SEARCH", searchText!!)

        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }
}




