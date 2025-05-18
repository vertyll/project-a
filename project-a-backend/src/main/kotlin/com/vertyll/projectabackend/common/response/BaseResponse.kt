package com.vertyll.projectabackend.common.response

import java.time.LocalDateTime

abstract class BaseResponse<T>(
    override val data: T?,
    override val message: String,
    override val timestamp: LocalDateTime = LocalDateTime.now(),
) : IResponse<T?>
