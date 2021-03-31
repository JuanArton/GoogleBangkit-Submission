package com.juanarton.consumerapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.juanarton.consumerapp.`interface`.MainView
import com.juanarton.consumerapp.adapter.PagerAdapter
import com.juanarton.consumerapp.databinding.ActivityUserProfileBinding
import com.juanarton.consumerapp.model.DetailDataParcel

class UserProfileActivity : AppCompatActivity(), MainView {

    private lateinit var binding: ActivityUserProfileBinding
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

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data by getParcelableExtra<DetailDataParcel>(EXTRA_DATA)
        if (data != null){
            globalData = data as DetailDataParcel
        }

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
        binding.followers.text = data?.followers.toString() + " " + getString(R.string.followers)
        binding.following.text = data?.following.toString() + " " + getString(R.string.following)
        binding.company.text = data?.company
        binding.location.text = data?.location
        binding.repository.text = data?.repository
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

    override fun showRecyclerList() {
    }

    override fun showDetail(dataDetail: DetailDataParcel) {
    }

    override fun showMessage(messsage: String) {
    }
}