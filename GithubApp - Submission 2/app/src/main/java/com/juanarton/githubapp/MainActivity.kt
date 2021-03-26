package com.juanarton.githubapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.juanarton.githubapp.`interface`.MainView
import com.juanarton.githubapp.adapter.RecyclerAdapter
import com.juanarton.githubapp.databinding.ActivityMainBinding
import com.juanarton.githubapp.model.DataParcel
import com.juanarton.githubapp.model.DataProcessing
import com.juanarton.githubapp.model.DetailDataParcel

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding
    private var listData = ArrayList<DataParcel>()
    val mViewModel = MViewModel(DataProcessing(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listData = mViewModel.getData("JuanArton")

        binding.searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.progressBar.visibility = View.VISIBLE
                listData.clear()
                listData = mViewModel.getData(query)
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    override fun showRecyclerList(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.recyclerContainer.layoutManager = LinearLayoutManager(this)
        val dataAdapter = RecyclerAdapter(listData)
        binding.recyclerContainer.adapter = dataAdapter

        dataAdapter.setOnItemClickCallback(object : RecyclerAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataParcel) {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerContainer.visibility = View.INVISIBLE
                mViewModel.getDetail(data)
            }
        })
    }

    override fun showDetail(dataDetail: DetailDataParcel){
        val intent = Intent(this, UserProfile::class.java)
        intent.putExtra(UserProfile.EXTRA_DATA, dataDetail)
        binding.progressBar.visibility = View.INVISIBLE
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerContainer.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun showMessage(messsage: String) {
        Toast.makeText(this, messsage, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this, MyProfile::class.java))
        return super.onOptionsItemSelected(item)
    }
}