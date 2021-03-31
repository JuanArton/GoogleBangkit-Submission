package com.juanarton.githubapp.helper

import android.database.Cursor
import com.juanarton.githubapp.database.DatabaseContract
import com.juanarton.githubapp.model.DataParcel
import java.util.*

object CursorHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<DataParcel> {
        val favList = ArrayList<DataParcel>()

        notesCursor?.apply {
            while (moveToNext()) {
                val login = getString(getColumnIndexOrThrow(DatabaseContract.FavUserColumns.LOGIN))
                val id = getString(getColumnIndexOrThrow(DatabaseContract.FavUserColumns.ID))
                favList.add(
                    DataParcel(
                            login,
                            id
                    )
                )
            }
        }
        return favList
    }
}