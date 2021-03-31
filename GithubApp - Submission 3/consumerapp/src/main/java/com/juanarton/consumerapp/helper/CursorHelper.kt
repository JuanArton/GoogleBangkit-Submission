package com.juanarton.consumerapp.helper

import android.database.Cursor
import com.juanarton.consumerapp.model.DataParcel
import com.juanarton.consumerapp.database.DatabaseContract
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