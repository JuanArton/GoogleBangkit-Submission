package com.juanarton.githubapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.juanarton.githubapp.databinding.ActivityUserProfileBinding

class UserProfile : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    companion object {
        const val EXTRA_MYDATA = "extra_mydata"
    }

    private inline fun <reified T : Parcelable> Activity.getParcelableExtra(key: String) = lazy {
        intent.getParcelableExtra<T>(key)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data by getParcelableExtra<DataParcel>(EXTRA_MYDATA)

        supportActionBar!!.title = data?.nama
        val id = this.resources.getIdentifier(data?.avatar,"drawable", this.packageName)

        Glide.with(this)
            .load(id)
            .centerCrop()
            .into(binding.userImage)

        binding.detailName.text = data?.nama
        binding.detailusername.text = data?.username
        binding.followers.text = data?.followers + " Followers"
        binding.following.text = data?.following + " Following"
        binding.company.text = data?.company
        binding.location.text = data?.location
        binding.repository.text = data?.repo

        binding.visitProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(data?.profile)
            startActivity(intent)
        }

        binding.visitRepo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(data?.repo)
            startActivity(intent)
        }
    }
}