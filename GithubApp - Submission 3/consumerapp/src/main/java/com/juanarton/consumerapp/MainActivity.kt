package com.juanarton.consumerapp

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.juanarton.consumerapp.UserProfileActivity.Companion.EXTRA_DATA
import com.juanarton.consumerapp.`interface`.MainView
import com.juanarton.consumerapp.adapter.RecyclerAdapter
import com.juanarton.consumerapp.database.DatabaseContract.FavUserColumns.Companion.CONTENT_URI
import com.juanarton.consumerapp.databinding.ActivityMainBinding
import com.juanarton.consumerapp.model.DataParcel
import com.juanarton.consumerapp.model.DataProcessing
import com.juanarton.consumerapp.model.DetailDataParcel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), MainView {

    private var listData = ArrayList<DataParcel>()
    private lateinit var binding: ActivityMainBinding
    private var executeOnFirstResume = false
    val mViewModel = MViewModel(DataProcessing(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val dataObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                getData()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, dataObserver)

        getData()
    }

    private fun getData(){
        GlobalScope.launch(Dispatchers.IO) {
            listData = mViewModel.readAllDB(this@MainActivity)
            binding.progressBar.visibility = View.INVISIBLE
            runOnUiThread {
                showRecycleFavorite()
                Log.d("favdata", listData.toString())
            }
        }
    }

    private fun showRecycleFavorite(){
        binding.favRecycler.visibility = View.VISIBLE
        binding.favRecycler.layoutManager = LinearLayoutManager(this@MainActivity)
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

    override fun showRecyclerList() {
    }

    override fun showDetail(dataDetail: DetailDataParcel) {
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra(EXTRA_DATA, dataDetail)
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
}