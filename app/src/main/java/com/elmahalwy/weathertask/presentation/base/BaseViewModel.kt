package com.elmahalwy.weathertask.presentation.base

import androidx.lifecycle.ViewModel
import com.elmahalwy.weathertask.domain.usecase.errors.ErrorManager
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    /**Inject Singleton ErrorManager
     * Use this errorManager to get the Errors
     */
    @Inject
    lateinit var errorManager: ErrorManager
}
