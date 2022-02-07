package com.likefirst.btos.data.entities.firebase

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageDTO(
    @SerializedName("title")
    var title : String? = null,
    @SerializedName("body")
    var body: String?=null ,
    @SerializedName("emailID")
    var emailID : String?=null,
    @SerializedName("type")
    var type : String?=null,
    @SerializedName("timestamp")
    var timestamp : String? = null,
    @SerializedName("fromToken")
    var fromToken : String? = null,
    @SerializedName("fromUser")
    var fromUser : String? = null

):Parcelable

