package com.likefirst.btos.data.entities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

class HistoryDetail (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "historyIdx") var historyIdx: Int= 0,
    @ColumnInfo(name = "content") var content: String?="",
    @ColumnInfo(name = "date") var date: String? = "",
    @ColumnInfo(name = "sender") var sender: String?= ""
)