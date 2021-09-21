package com.example.lab6

import android.app.Application
import androidx.room.Room
import com.example.lab6.data.DebtorDatabase
import com.example.lab6.data.DebtorSessionDatabase

class DeudoresApp : Application() {

    companion object{
        lateinit var database: DebtorDatabase
        lateinit var database2: DebtorSessionDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, DebtorDatabase::class.java, "debtor_db").allowMainThreadQueries().build()
        database2 = Room.databaseBuilder(this,DebtorSessionDatabase::class.java, "debtor_db_session").allowMainThreadQueries().build()
    }

}