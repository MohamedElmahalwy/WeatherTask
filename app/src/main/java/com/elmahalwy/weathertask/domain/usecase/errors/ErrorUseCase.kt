package com.elmahalwy.weathertask.domain.usecase.errors

import com.elmahalwy.taskmazady.data.error.Error


interface ErrorUseCase {
    fun getError(errorCode: Int): Error
}
