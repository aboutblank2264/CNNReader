package com.aboutblank.cnnreader.backend

data class Status(
    val status: StatusEnum,
    val message: String
) {
    constructor(status: StatusEnum) : this(status, "")
}

enum class StatusEnum {
    OK,
    ERROR,
    LOADING
}