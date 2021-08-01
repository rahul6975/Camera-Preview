package com.rahul.camerasnapshots.views

import android.app.Application
import com.rahul.camerasnapshots.repository.MyRepository
import com.rahul.camerasnapshots.room.DatabaseClass

//Application class
class MyApplication : Application() {
    val daoClass by lazy {
        val listDatabase = DatabaseClass.getDatabase(this)
        listDatabase.getImageDao()
    }
    val myRepository by lazy {
        MyRepository(daoClass)
    }
}