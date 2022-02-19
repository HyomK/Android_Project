package com.likefirst.btos.data.entities.firebase

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "NotificationTable" )
data class NotificationDTO (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo var alarmIdx : Int,
    @ColumnInfo var content: String?=null,
    @ColumnInfo var createAt: String?=null,
    @ColumnInfo var isChecked: Boolean = false,

): Parcelable