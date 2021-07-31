package com.rahul.camerasnapshots

import android.app.Application
import com.rahul.camerasnapshots.room.DatabaseClass

//Application class
class MyApplication : Application() {
    val listDao by lazy {
        val listDatabase = DatabaseClass.getDatabase(this)
        listDatabase.getImageDao()
    }
}