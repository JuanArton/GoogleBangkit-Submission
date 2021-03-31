package com.juanarton.consumerapp.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.juanarton.githubapp"
    const val SCHEME = "content"

    class FavUserColumns : BaseColumns {

        companion object {
            private const val TABLE_NAME = "favuser"
            const val ID = "id"
            const val LOGIN = "login"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }

    }
}