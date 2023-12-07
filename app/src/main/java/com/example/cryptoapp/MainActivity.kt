package com.example.cryptoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import me.ibrahimsn.lib.SmoothBottomBar

class MainActivity : AppCompatActivity() {

    lateinit var bottomBar : SmoothBottomBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomBar = findViewById(R.id.bottomBar)

        val navHost = supportFragmentManager.findFragmentById(R.id.container1)
        val navController = navHost!!.findNavController()

        val popmenu = PopupMenu(this,null)
        popmenu.inflate(R.menu.bottom_navigation)
        bottomBar.setupWithNavController(popmenu.menu,navController)

    }
}