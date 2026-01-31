package com.example.remedialucp2.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BukuPengarangDao {
    @Query("SELECT * from tblBukuPengarang WHERE bukuId = :bukuId")
    fun getPengarangByBuku(bukuId: Int): Flow<List<BukuPengarang>>
    @Query("SELECT p.* from tblPengarang p INNER JOIN tblBukuPengarang bp ON p.id = bp.pengarangId WHERE bp.bukuId = :bukuId")
    fun getPengarangListByBuku(bukuId: Int): Flow<List<Pengarang>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bukuPengarang: BukuPengarang)
    @Query("DELETE FROM tblBukuPengarang WHERE bukuId = :bukuId")
    suspend fun deleteByBuku(bukuId: Int)
    @Query("DELETE FROM tblBukuPengarang WHERE bukuId = :bukuId AND pengarangId = :pengarangId")
    suspend fun delete(bukuId: Int, pengarangId: Int)
}