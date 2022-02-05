package com.likefirst.btos.data.entities.firebase

data class MessageDTO(
    var title : String? = null,
    var body: String?=null ,
    var emailID : String?=null,
    var type : String?=null,
    var timestamp : String? = null,
    var fromToken : String? = null,

)

