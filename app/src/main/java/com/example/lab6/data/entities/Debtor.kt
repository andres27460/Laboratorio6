package com.example.lab6.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "table_debtor")
data class Debtor (

        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "name")val name: String,
        @ColumnInfo(name = "phone")val phone: String,
        @ColumnInfo(name = "amount")val amount: Long


) : Serializable

@Entity(tableName = "table_session")
data class DebtorSession(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "user_login")val user_login: String,
        @ColumnInfo(name = "pass_login")val pass_login: String
        ) : Serializable
