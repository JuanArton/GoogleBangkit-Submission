package com.juanarton.githubapp.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.juanarton.githubapp.MViewModel
import com.juanarton.githubapp.R
import com.juanarton.githubapp.`interface`.MainView
import com.juanarton.githubapp.model.DataParcel
import com.juanarton.githubapp.model.DataProcessing
import com.juanarton.githubapp.model.DetailDataParcel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.URL

internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory, MainView {

    private val mWidgetItems = ArrayList<Bitmap>()
    private var listData = ArrayList<DataParcel>()
    private val mViewModel = MViewModel(DataProcessing(this))

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        listData.clear()
        mWidgetItems.clear()
        runBlocking {
            val job: Job = launch(context = Dispatchers.IO) {
                listData = mViewModel.readAllDB(mContext)
            }
            job.join()
            for (i in 0 until listData.size) {
                mWidgetItems.add(BitmapFactory.decodeStream(URL(listData[i].anydata).openConnection().getInputStream()))
            }
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    @SuppressLint("RemoteViewLayout")
    override fun getViewAt(position: Int): RemoteViews {
        val rView = RemoteViews(mContext.packageName, R.layout.widget_item)
        rView.setImageViewBitmap(R.id.imageView, mWidgetItems[position])

        val extras = bundleOf(
            StackWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rView.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rView
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    override fun showRecyclerList() {
    }

    override fun showDetail(dataDetail: DetailDataParcel) {
    }

    override fun showMessage(messsage: String) {
    }

}