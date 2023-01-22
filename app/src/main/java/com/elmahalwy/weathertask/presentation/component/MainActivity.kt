package com.elmahalwy.weathertask.presentation.component

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.elmahalwy.weathertask.R
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavagtionController()

    }

    fun setupNavagtionController(){
        navController = findNavController(R.id.mainFragmentContainer)
        findNavController(R.id.mainFragmentContainer).graph = navController.graph

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainFragmentContainer) as NavHostFragment?
        val size = navHostFragment!!.childFragmentManager.fragments.size
        navHostFragment.childFragmentManager.fragments[size - 1]
            .onActivityResult(requestCode, resultCode, data)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainFragmentContainer) as NavHostFragment?
        val size = navHostFragment!!.childFragmentManager.fragments.size
        navHostFragment.childFragmentManager.fragments[size - 1]
            .onRequestPermissionsResult(requestCode, permissions, grantResults)


    }
}
