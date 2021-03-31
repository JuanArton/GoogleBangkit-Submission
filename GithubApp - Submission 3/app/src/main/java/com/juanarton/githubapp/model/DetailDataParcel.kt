package com.juanarton.githubapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class DetailDataParcel (
    val id: String,
    val name: String,
    val login: String,
    val followers: String,
    val following: String,
    val company: String,
    val location: String,
    val repository: String,
    val avatar: String
): Parcelable