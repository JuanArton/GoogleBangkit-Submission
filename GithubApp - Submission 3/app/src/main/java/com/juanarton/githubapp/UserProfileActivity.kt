package com.juanarton.githubapp


import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.juanarton.githubapp.`interface`.MainView
import com.juanarton.githubapp.adapter.PagerAdapter
import com.juanarton.githubapp.database.FavuserHelper
import com.juanarton.githubapp.databinding.ActivityUserProfileBinding
import com.juanarton.githubapp.model.DataParcel
import com.juanarton.githubapp.model.DataProcessing
import com.juanarton.githubapp.model.DetailDataParcel
import com.juanarton.githubapp.widget.StackWidget


class UserProfileActivity : AppCompatActivity(), View.OnClickListener, MainView {

    private lateinit var binding: ActivityUserProfileBinding
    private val mViewModel = MViewModel(DataProcessing(this))
    private lateinit var dbHelper: FavuserHelper
    private lateinit var globalData: DetailDataParcel

    companion object{
        const val EXTRA_DATA = "extra_mydata"
        lateinit var username: String

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    private inline fun <reified T : Parcelable> Activity.getParcelableExtra(key: String) = lazy {
        intent.getParcelableExtra<T>(key)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = FavuserHelper.getInstance(applicationContext)
        dbHelper.open()

        val data by getParcelableExtra<DetailDataParcel>(EXTRA_DATA)
        if (data != null){
            globalData = data as DetailDataParcel
        }

        setButton()

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            val layout: ViewPager2 = binding.viewPager
            val params: ViewGroup.LayoutParams = layout.layoutParams
            params.height = this.resources.getDimensionPixelSize(R.dimen.customH)
            layout.layoutParams = params
        }

        supportActionBar?.title = data?.name
        username = data?.login.toString()

        val sectionsPagerAdapter = PagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        viewPager.requestDisallowInterceptTouchEvent(true)
        val tabs: TabLayout = binding.tabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        Glide.with(this)
            .load(data?.avatar)
            .centerCrop()
            .into(binding.userImage)

        binding.detailName.text = data?.name
        binding.detailusername.text = data?.login
        binding.followers.text = StringBuilder(data?.followers.toString()).append(" ").append(getString(R.string.followers))
        binding.following.text = StringBuilder(data?.following.toString()).append(" ").append(getString(R.string.following))
        binding.company.text = data?.company
        binding.location.text = data?.location
        binding.repository.text = data?.repository

        binding.favButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.favButton){
            val uid = searchUID()
            if(uid == null){
                mViewModel.addToDB(this, globalData.login, globalData.id)
                widgetUpdate()
                setButton()
            }else{
                mViewModel.deleteFromDB(this, globalData.id)
                widgetUpdate()
                setButton()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val layout: ViewPager2 = binding.viewPager
        val params: ViewGroup.LayoutParams = layout.layoutParams

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.height = this.resources.getDimensionPixelSize(R.dimen.customH)
            layout.layoutParams = params
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            params.height = RelativeLayout.LayoutParams.MATCH_PARENT
            layout.layoutParams = params
        }
    }

    private fun widgetUpdate(){
        val widgetManager = AppWidgetManager.getInstance(this)
        val widgetID = widgetManager.getAppWidgetIds(ComponentName(this, StackWidget::class.java))
        widgetManager.notifyAppWidgetViewDataChanged(widgetID, R.id.stackWidgetView)
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }

    private fun searchUID(): DataParcel? {
        val listFav = mViewModel.readDB(this)
        return listFav.find { it.anydata == globalData.id }
    }

    private fun setButton(){
        if(searchUID() == null){
            binding.favButton.setImageResource(R.drawable.ic_baseline_star_border_36)
        }else{
            binding.favButton.setImageResource(R.drawable.ic_baseline_star_36)
        }
    }

    override fun showRecyclerList() {
    }

    override fun showDetail(dataDetail: DetailDataParcel) {
    }

    override fun showMessage(messsage: String) {
    }
}