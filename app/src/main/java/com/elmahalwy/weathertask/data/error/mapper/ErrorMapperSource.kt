package com.elmahalwy.weathertask.data.error.mapper

interface ErrorMapperSource {
    fun getErrorString(errorId: Int): String
    val errorsMap: Map<Int, String>
}
