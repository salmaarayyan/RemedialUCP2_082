package com.example.remedialucp2.repositori

import com.example.remedialucp2.room.Buku
import com.example.remedialucp2.room.BukuDao
import kotlinx.coroutines.flow.Flow
interface RepositoriBuku {
    fun getAllBukuStream(): Flow<List<Buku>>
    fun getBukuByKategoriStream(kategoriId: Int): Flow<List<Buku>>
    fun getBukuStream(id: Int): Flow<Buku?>
    suspend fun countBukuDipinjamByKategori(kategoriId: Int): Int
    suspend fun softDeleteByKategori(kategoriId: Int)
    suspend fun setKategoriNull(kategoriId: Int)
    suspend fun insertBuku(buku: Buku): Long
    suspend fun updateBuku(buku: Buku)
    suspend fun softDeleteBuku(id: Int)
}
class OfflineRepositoriBuku(
    private val bukuDao: BukuDao
) : RepositoriBuku {
    override fun getAllBukuStream() = bukuDao.getAllBuku()
    override fun getBukuByKategoriStream(kategoriId: Int) = bukuDao.getBukuByKategori(kategoriId)
    override fun getBukuStream(id: Int) = bukuDao.getBuku(id)
    override suspend fun countBukuDipinjamByKategori(kategoriId: Int) = bukuDao.countBukuDipinjamByKategori(kategoriId)
    override suspend fun softDeleteByKategori(kategoriId: Int) = bukuDao.softDeleteByKategori(kategoriId)
    override suspend fun setKategoriNull(kategoriId: Int) = bukuDao.setKategoriNull(kategoriId)
    override suspend fun insertBuku(buku: Buku) = bukuDao.insert(buku)
    override suspend fun updateBuku(buku: Buku) = bukuDao.update(buku)
    override suspend fun softDeleteBuku(id: Int) = bukuDao.softDelete(id)
}