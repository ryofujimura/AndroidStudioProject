package com.zybooks.studyhelper.model

data class Subject(
    var id: Long = 0,
    var text: String,
    var updateTime: Long = System.currentTimeMillis()) {
}