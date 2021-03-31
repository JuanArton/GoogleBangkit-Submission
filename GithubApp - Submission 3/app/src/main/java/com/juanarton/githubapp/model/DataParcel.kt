package com.juanarton.githubapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataParcel(
    val login: String,
    val anydata: String
): Parcelable