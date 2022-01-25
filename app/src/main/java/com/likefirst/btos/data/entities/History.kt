package com.likefirst.btos.data.entities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

//@Entity(tableName = "PlantItemTable")
data class History (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "historyIdx") var historyIdx: Int= 0, // album의 pk는 임의로 지정해주기 위해 autogenerate 안씁니다.
    @ColumnInfo(name = "content") var content: String?="",
    @ColumnInfo(name = "date") var date: String? = "",
    @ColumnInfo(name = "sender") var sender: String?= "",
    @ColumnInfo(name = "emotion") var emotion: Int?= 0,
    @ColumnInfo(name = "done") var done:Int? = 0
)