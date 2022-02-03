package com.likefirst.btos.data.entities.firebase

data class NoticeDTO (
    var type :  String? =null,
    var fromUid : String? = null,
    var content : String? = null,
    var timestamp : Long? = null,
)