package com.likefirst.btos.data.entities.firebase

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "NotificationTable" ,primaryKeys = arrayOf("timestamp", "type","detailIdx"))
data class NotificationDTO (

    @ColumnInfo var timestamp : String = "",
    @ColumnInfo var fromToken : String?=null,
    @ColumnInfo var type :  String ="",
    @ColumnInfo var detailIdx : Int,
    @ColumnInfo var title : String? = null,
    @ColumnInfo var content: String?=null,
    @ColumnInfo var fromUser: String?=null,
    @ColumnInfo var isRead : Boolean=false

): Parcelable