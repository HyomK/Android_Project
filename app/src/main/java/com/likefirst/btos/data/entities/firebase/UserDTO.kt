package com.likefirst.btos.data.entities.firebase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDTO(
    var userEmailID : String? = null,
    var fcmToken : String? = null,
) : Parcelable