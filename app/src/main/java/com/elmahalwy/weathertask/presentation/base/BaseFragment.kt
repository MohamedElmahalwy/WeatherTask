package com.elmahalwy.weathertask.presentation.base

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import com.elmahalwy.weathertask.R

abstract class BaseFragment<VM : BaseViewModel, DB : ViewBinding>(@LayoutRes contentLayout: Int) :
    Fragment(contentLayout) {
    protected lateinit var binding: DB

    protected abstract val viewModel: VM
    protected lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        retainInstance = true
        binding = getFragmentBinding(inflater, container)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.let {
            try {
                navController = Navigation.findNavController(it)
            } catch (e: java.lang.Exception) {
            }
        }
    }


     abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): DB


    fun NavController.safeNavigate(direction: NavDirections) {
        currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
    }

    fun NavController.safeNavigate(
        @IdRes currentDestinationId: Int,
        @IdRes id: Int,
        args: Bundle? = null
    ) {
        if (currentDestinationId == currentDestination?.id) {
            navigate(id, args)
        }
    }

}

