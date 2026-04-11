package com.vertyll.kotlinapi.common.response

import java.time.LocalDateTime

interface IResponse<T> {
    val data: T?
    val message: String
    val timestamp: LocalDateTime
}
