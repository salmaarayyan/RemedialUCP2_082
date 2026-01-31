package com.example.remedialucp2.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tblBuku",
    foreignKeys = [
        ForeignKey(
            entity = Kategori::class,
            parentColumns = ["id"],
            childColumns = ["kategoriId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Buku(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val judul: String,
    val tahunTerbit: Int,
    val kategoriId: Int? = null,
    val status: String = "tersedia",
    val isDeleted: Boolean = false
)