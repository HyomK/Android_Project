package com.likefirst.btos.data.entities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class BasicHistory<T> (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "list") var list : ArrayList<T>,
)

//sender -> BasicHistory<SenderList>
data class SenderList(
    @ColumnInfo(name = "historyListNum") var historyListNum: Int?= 0,
    @ColumnInfo(name = "firstContent") var firstContent: Content
)

//diary/letter -> BasicHistory<Content>
//발신인 조회 -> Content
data class Content(
    @ColumnInfo(name = "type") var type: String = "",
    @ColumnInfo(name = "typeIdx") var typeIdx : Int = 0,
    @ColumnInfo(name = "content") var content: String? = "",
    @ColumnInfo(name = "emotionIdx") var emotionIdx: String? = "",
    @ColumnInfo(name = "doneListNum") var doneListNum: Int? = 0,
    @ColumnInfo(name = "sendAt_raw") var sendAt_raw: String?= "",
    @ColumnInfo(name = "sendAt") var sendAt: String?= "",
    @ColumnInfo(name = "senderNickName") var senderNickName: String?=""
)

//본문 보기 -> HistoryList
data class HistoryList(
    @ColumnInfo(name = "type") var type: String?="",
    @ColumnInfo(name = "idx") var idx: Int?= 0,
    @ColumnInfo(name = "content") var content : String? = "",
    @ColumnInfo(name = "emotionIdx") var emotionIdx: String? = "",
    @ColumnInfo(name = "doneList") var doneList: ArrayList<HistoryDoneList>,
    @ColumnInfo(name = "sendAt_raw") var sendAt_raw: String?= "",
    @ColumnInfo(name = "sendAt") var sendAt: String?= "",
    @ColumnInfo(name = "senderNickName") var senderNickName: String?="",
    @ColumnInfo(name = "positioning") var positioning: Boolean = false
)

data class HistoryDoneList(
    @ColumnInfo(name = "doneIdx") var doneIdx: Int?= 0,
    @ColumnInfo(name = "content") var content: String?= ""
)

data class PageInfo(
    @ColumnInfo(name = "hasNext") var hasNext: Boolean? = false,
    @ColumnInfo(name = "currentPage") var currentPage: Int? = 1,
    @ColumnInfo(name = "startPage") var startPage: Int? = 1,
    @ColumnInfo(name = "endPage") var endPage: Int? = 1,
    @ColumnInfo(name = "dataNumPerPage") var dataNumPerPage: Int? = 20,
    @ColumnInfo(name = "dataNum_currentPage") var dataNum_currentPage: Int? = 0,
    @ColumnInfo(name = "dataNum_total") var dataNum_total: Int? = 0,
)
