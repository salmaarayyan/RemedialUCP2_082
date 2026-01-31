package com.example.remedialucp2.room

import androidx.room.Dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BukuDao {
    @Query("SELECT * from tblBuku WHERE isDeleted = 0 ORDER BY judul ASC")
    fun getAllBuku(): Flow<List<Buku>>
    @Query("SELECT * from tblBuku WHERE kategoriId = :kategoriId AND isDeleted = 0 ORDER BY judul ASC")
    fun getBukuByKategori(kategoriId: Int): Flow<List<Buku>>
    @Query("SELECT * from tblBuku WHERE id = :id")
    fun getBuku(id: Int): Flow<Buku?>
    @Query("SELECT COUNT(*) from tblBuku WHERE kategoriId = :kategoriId AND status = 'dipinjam' AND isDeleted = 0")
    suspend fun countBukuDipinjamByKategori(kategoriId: Int): Int
    @Query("UPDATE tblBuku SET isDeleted = 1 WHERE kategoriId = :kategoriId")
    suspend fun softDeleteByKategori(kategoriId: Int)
    @Query("UPDATE tblBuku SET kategoriId = NULL WHERE kategoriId = :kategoriId")
    suspend fun setKategoriNull(kategoriId: Int)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(buku: Buku): Long
    @Update
    suspend fun update(buku: Buku)
    @Query("UPDATE tblBuku SET isDeleted = 1 WHERE id = :id")
    suspend fun softDelete(id: Int)
}