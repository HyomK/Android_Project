package com.likefirst.btos.data.entities.firebase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDTO(
    var uId : String? = null,
    var userId : String? = null,
    var imageUri : String? = null,
    var score : Int = 0,
    var sharing : Int = 0,
    var area : String? = null,
    var token : String? = null
) : Parcelable