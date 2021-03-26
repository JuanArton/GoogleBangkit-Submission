package com.juanarton.githubapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataParcel (
    val nama: String,
    val avatar: String,
    val username: String,
    val followers: String,
    val following: String,
    val company: String,
    val location: String,
    val repo: String,
    val profile: String
) : Parcelable