package com.likefirst.btos.data.entities.firebase

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "NotificationTable")
data class NotificationDTO (
    @PrimaryKey(autoGenerate = false)

    @ColumnInfo val Idx: Int,
    @ColumnInfo var type :  String? =null,
    @ColumnInfo var detailIdx : Int?=null,
    @ColumnInfo var title : String? = null,
    @ColumnInfo  var content: String?=null,
    @ColumnInfo var timestamp : String? = null,
    @ColumnInfo var fromToken : String? = null,
): Parcelable