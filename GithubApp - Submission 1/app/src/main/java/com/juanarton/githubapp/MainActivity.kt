package com.juanarton.githubapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.juanarton.githubapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var listData: ArrayList<DataParcel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "Github Users"

        listData.addAll(getData())
        showRecyclerList()
    }

    private fun getData(): ArrayList<DataParcel> {
        val name = resources.getStringArray(R.array.data_nama)
        val avatar = resources.getStringArray(R.array.data_avatar)
        val username = resources.getStringArray(R.array.data_username)
        val followers = resources.getStringArray(R.array.data_followers)
        val following = resources.getStringArray(R.array.data_following)
        val company = resources.getStringArray(R.array.data_company)
        val location = resources.getStringArray(R.array.data_location)
        val repo = resources.getStringArray(R.array.data_repository)
        val profile = resources.getStringArray(R.array.data_profile)
        val list = ArrayList<DataParcel>()
        for (idx in name.indices) {
            val myData = DataParcel(
                name[idx],
                avatar[idx],
                username[idx],
                followers[idx],
                following[idx],
                company[idx],
                location[idx],
                repo[idx],
                profile[idx]
            )
            list.add(myData)
        }
        return list
    }

    private fun showRecyclerList(){
        binding.recyclerContainer.layoutManager = LinearLayoutManager(this)
        val dataAdapter = RecyclerAdapter(listData, this@MainActivity)
        binding.recyclerContainer.adapter = dataAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this, MyProfile::class.java))
        return super.onOptionsItemSelected(item)
    }
}