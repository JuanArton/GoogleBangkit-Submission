package com.juanarton.githubapp.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.juanarton.githubapp.`interface`.MainView
import com.juanarton.githubapp.api.API
import com.juanarton.githubapp.database.DatabaseContract.FavUserColumns.Companion.CONTENT_URI
import com.juanarton.githubapp.database.DatabaseContract.FavUserColumns.Companion.ID
import com.juanarton.githubapp.database.DatabaseContract.FavUserColumns.Companion.LOGIN
import com.juanarton.githubapp.database.FavuserHelper
import com.juanarton.githubapp.helper.CursorHelper
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataProcessing(private val view: MainView){

    private val listData = ArrayList<DataParcel>()

    fun getData(username: String): ArrayList<DataParcel>{
        try {
            API().services.getData("request", username)
                .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.code() == 200) {
                        val responseObject = JSONObject(response.body()?.string())
                        val items = responseObject.getJSONArray("items")
                        for (i in 0 until items.length()) {
                            val jsonObject = items.getJSONObject(i)
                            val login = jsonObject.getString("login")
                            val avatar = jsonObject.getString("avatar_url")
                            val myData = DataParcel(
                                    login,
                                    avatar
                            )
                            listData.add(myData)
                        }
                    }
                    view.showRecyclerList()
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    view.showMessage("Failed to get data")
                }
            })
        }
        catch (e:Exception){
            view.showMessage("Failed to get data")
        }
        return listData
    }

    fun getDetail(data: DataParcel): DetailDataParcel{
        var detailData = DetailDataParcel("", "", "", "",
            "", "", "", "", "")
        try {
            API().services.getDetail("request", "https://api.github.com/users/" + data.login)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if(response.code() == 200) {
                            val responseObject = JSONObject(response.body()?.string())
                            detailData = (
                                DetailDataParcel(
                                    responseObject.getString("id").toString(),
                                    responseObject.getString("name").toString(),
                                    responseObject.getString("login").toString(),
                                    responseObject.getString("followers").toString(),
                                    responseObject.getString("following").toString(),
                                    responseObject.getString("company").toString(),
                                    responseObject.getString("location").toString(),
                                    responseObject.getString("repos_url").toString(),
                                    responseObject.getString("avatar_url").toString()
                                )
                            )
                        }
                        view.showDetail(detailData)
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        view.showMessage("Failed to get data")
                    }
                })
        }
        catch (e:Exception){
            view.showMessage("Failed to get data")
        }
        return detailData
    }

    fun getSocNetwork(username: String, mode: String): ArrayList<DataParcel>{
        try {
            API().services.getFollowers("request", "https://api.github.com/users/$username/$mode")
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if(response.code() == 200) {
                            val responseObject = JSONArray(response.body()?.string())
                            for (i in 0 until responseObject.length()) {
                                val jsonObject = responseObject.getJSONObject(i)
                                val login = jsonObject.getString("login")
                                val avatar = jsonObject.getString("avatar_url")
                                val myData = DataParcel(
                                    login,
                                    avatar
                                )
                                listData.add(myData)
                            }
                        }
                        view.showRecyclerList()
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        view.showMessage("Failed to get data")
                    }
                })
        }
        catch (e:Exception){
            view.showMessage("Failed to get data")
        }
        return listData
    }

    fun addToDB(context: Context, username: String, id: String){
        val data = ContentValues()
        data.put(ID, id)
        data.put(LOGIN, username)
        context.contentResolver.insert(CONTENT_URI, data)
    }

    fun deleteFromDb(context: Context, id: String){
        val favUserHelper = FavuserHelper.getInstance(context)
        favUserHelper.open()
        favUserHelper.deleteById(id)
    }

    fun readDB(context: Context): ArrayList<DataParcel>{
        lateinit var cursor: Cursor
        lateinit var data: ArrayList<DataParcel>
        try{
            cursor = context.contentResolver?.query(CONTENT_URI, null, null, null, null)!!
            data = (CursorHelper.mapCursorToArrayList(cursor))
        }
        catch (e: Exception){
            view.showMessage("Failed to get data")
        }
        return data
    }

    fun readAllDB(context: Context): ArrayList<DataParcel> {
        try{
            val listFav = readDB(context)
            runBlocking{
                coroutineScope {
                    (0 until listFav.size).map {
                        async(Dispatchers.IO) {
                            val data = listFav[it]
                            val userdata = API().services.getDetail("request", "https://api.github.com/users/" + data.login).execute()
                            val responseObject = JSONObject(userdata.body()?.string())
                            val myData = DataParcel(
                                responseObject.getString("login"),
                                responseObject.getString("avatar_url")
                            )
                            listData.add(myData)
                        }
                    }.awaitAll()
                }
            }
        }
        catch(e: Exception){
            view.showMessage("Failed to get data")
        }
        return listData
    }
}