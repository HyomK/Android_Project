package com.likefirst.btos.data.entities.firebase

data class NoticeDTO (
    val noticeIdx: Int?=null,
    var type :  String? =null,
    var fromUid : String? = null,
    var title : String?=null,
    var content : String? = null,
    var createdAt : String? = null,
)