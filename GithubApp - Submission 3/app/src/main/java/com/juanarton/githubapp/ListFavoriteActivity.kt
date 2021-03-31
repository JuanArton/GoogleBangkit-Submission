package com.juanarton.githubapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.juanarton.githubapp.`interface`.MainView
import com.juanarton.githubapp.adapter.RecyclerAdapter
import com.juanarton.githubapp.database.FavuserHelper
import com.juanarton.githubapp.databinding.ActivityListFavoriteBinding
import com.juanarton.githubapp.model.DataParcel
import com.juanarton.githubapp.model.DataProcessing
import com.juanarton.githubapp.model.DetailDataParcel
import kotlinx.coroutines.*

class ListFavoriteActivity : AppCompatActivity(), MainView {

    private var listData = ArrayList<DataParcel>()
    private lateinit var binding: ActivityListFavoriteBinding
    private lateinit var dbHelper: FavuserHelper
    private var executeOnFirstResume = false
    val mViewModel = MViewModel(DataProcessing(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.favorite)

        dbHelper = FavuserHelper.getInstance(applicationContext)
        dbHelper.open()

        getData()
    }

    private fun getData(){
        GlobalScope.launch(Dispatchers.IO) {
            listData = mViewModel.readAllDB(this@ListFavoriteActivity)
            binding.progressBar.visibility = View.INVISIBLE
            runOnUiThread {
                showRecycleFavorite()
            }
        }
    }

    private fun showRecycleFavorite(){
        binding.favRecycler.visibility = View.VISIBLE
        binding.favRecycler.layoutManager = LinearLayoutManager(this@ListFavoriteActivity)
        val dataAdapter = RecyclerAdapter(listData)
        binding.favRecycler.adapter = dataAdapter
        dataAdapter.setOnItemClickCallback(object : RecyclerAdapter.OnItemClickCallback {

            override fun onItemClicked(data: DataParcel) {
                binding.progressBar.visibility = View.VISIBLE
                binding.favRecycler.visibility = View.INVISIBLE
                mViewModel.getDetail(data)
            }
        })
    }

    override fun showDetail(dataDetail: DetailDataParcel) {
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra(UserProfileActivity.EXTRA_DATA, dataDetail)
        binding.progressBar.visibility = View.INVISIBLE
        startActivity(intent)
    }

    override fun showMessage(messsage: String) {
        runOnUiThread {
            Toast.makeText(this, messsage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar.visibility = View.VISIBLE
        if(executeOnFirstResume){
            listData.clear()
            getData()
        } else{
            executeOnFirstResume = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }

    override fun showRecyclerList() {
    }
}