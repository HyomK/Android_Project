package com.likefirst.btos.data.entities.firebase

data class MessageDTO(
    var fromToken : String? = null,
    var toToken: String? = null,
    var userEmail: String ,
    var content : String? = null,
    var timestamp : Long? = null,
)
