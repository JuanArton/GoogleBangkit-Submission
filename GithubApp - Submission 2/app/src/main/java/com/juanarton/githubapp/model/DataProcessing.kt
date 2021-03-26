package com.juanarton.githubapp.model

import com.juanarton.githubapp.`interface`.MainView
import com.juanarton.githubapp.api.API
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
                        val responseObject = JSONObject(response.body()!!.string())
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
            "", "", "", "")
        try {
            API().services.getDetail("request", "https://api.github.com/users/" + data.login)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if(response.code() == 200) {
                            val responseObject = JSONObject(response.body()!!.string())
                            detailData = (
                                DetailDataParcel(
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
}