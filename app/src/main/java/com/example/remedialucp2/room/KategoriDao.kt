package com.example.remedialucp2.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface KategoriDao {
    @Query("SELECT * from tblKategori ORDER BY namaKategori ASC")
    fun getAllKategori(): Flow<List<Kategori>>
    @Query("SELECT * from tblKategori WHERE parentId IS NULL ORDER BY namaKategori ASC")
    fun getRootKategori(): Flow<List<Kategori>>
    @Query("SELECT * from tblKategori WHERE parentId = :parentId ORDER BY namaKategori ASC")
    fun getSubKategori(parentId: Int): Flow<List<Kategori>>
    @Query("SELECT * from tblKategori WHERE id = :id")
    fun getKategori(id: Int): Flow<Kategori?>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(kategori: Kategori)
    @Update
    suspend fun update(kategori: Kategori)
    @Delete
    suspend fun delete(kategori: Kategori)
}