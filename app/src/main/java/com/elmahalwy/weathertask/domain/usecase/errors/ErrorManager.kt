package com.elmahalwy.weathertask.domain.usecase.errors

import javax.inject.Inject
import com.elmahalwy.taskmazady.data.error.Error
import com.elmahalwy.weathertask.data.error.mapper.ErrorMapper


class ErrorManager @Inject constructor(private val errorMapper: ErrorMapper) : ErrorUseCase {
    override fun getError(errorCode: Int): Error {
        return Error(code = errorCode, description = errorMapper.errorsMap.getValue(errorCode))
    }
}
