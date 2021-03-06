package com.febrian.icc

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.febrian.icc.databinding.ActivityMainBinding
import com.febrian.icc.utils.Constant.KEY_LOG

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        val sharedPref = applicationContext.getSharedPreferences(KEY_LOG, Context.MODE_PRIVATE)
        sharedPref.edit().putString(KEY_LOG, KEY_LOG).apply()
    }
}