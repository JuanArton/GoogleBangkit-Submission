package com.juanarton.githubapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MyProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        supportActionBar?.title = getString(R.string.profil)
    }
}