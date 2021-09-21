package com.example.lab6.data

import androidx.room.Database
import com.example.lab6.data.entities.DebtorSession
import androidx.room.RoomDatabase
import com.example.lab6.data.dao.DebtorDao
import com.example.lab6.data.dao.DebtorSessionDao
import com.example.lab6.data.entities.Debtor

@Database(entities = [Debtor::class], version = 1)
abstract class DebtorDatabase: RoomDatabase() {
    abstract  fun DebtorDao(): DebtorDao
}

@Database(entities = [DebtorSession::class], version = 1)
abstract class DebtorSessionDatabase: RoomDatabase(){

    abstract fun DebtorSessionDao(): DebtorSessionDao
}

