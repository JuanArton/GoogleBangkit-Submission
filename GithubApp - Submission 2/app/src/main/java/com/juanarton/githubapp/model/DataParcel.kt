package com.juanarton.githubapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataParcel(
    var login: String,
    var avatar: String
): Parcelable