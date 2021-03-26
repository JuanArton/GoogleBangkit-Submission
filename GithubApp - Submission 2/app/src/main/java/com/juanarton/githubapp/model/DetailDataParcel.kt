package com.juanarton.githubapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class DetailDataParcel (
    var name: String,
    var login: String,
    var followers: String,
    var following: String,
    var company: String,
    var location: String,
    var repository: String,
    var avatar: String
): Parcelable