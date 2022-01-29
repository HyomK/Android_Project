package com.likefirst.btos.utils

import java.text.SimpleDateFormat
import java.util.*

fun stringToDate(value: String): Date {
    val fm = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
    return fm.parse(value)!!
}

fun dateToString(value: Date) : String{
    val fm = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
    return fm.format(value)
}