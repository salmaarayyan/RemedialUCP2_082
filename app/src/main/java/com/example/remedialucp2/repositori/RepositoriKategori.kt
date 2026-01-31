package com.example.remedialucp2.repositori

import com.example.remedialucp2.room.Kategori
import com.example.remedialucp2.room.KategoriDao
import kotlinx.coroutines.flow.Flow
interface RepositoriKategori {
    fun getAllKategoriStream(): Flow<List<Kategori>>
    fun getRootKategoriStream(): Flow<List<Kategori>>
    fun getSubKategoriStream(parentId: Int): Flow<List<Kategori>>
    fun getKategoriStream(id: Int): Flow<Kategori?>
    suspend fun insertKategori(kategori: Kategori)
    suspend fun updateKategori(kategori: Kategori)
    suspend fun deleteKategori(kategori: Kategori)
}
class OfflineRepositoriKategori(
    private val kategoriDao: KategoriDao
) : RepositoriKategori {
    override fun getAllKategoriStream() = kategoriDao.getAllKategori()
    override fun getRootKategoriStream() = kategoriDao.getRootKategori()
    override fun getSubKategoriStream(parentId: Int) = kategoriDao.getSubKategori(parentId)
    override fun getKategoriStream(id: Int) = kategoriDao.getKategori(id)
    override suspend fun insertKategori(kategori: Kategori) = kategoriDao.insert(kategori)
    override suspend fun updateKategori(kategori: Kategori) = kategoriDao.update(kategori)
    override suspend fun deleteKategori(kategori: Kategori) = kategoriDao.delete(kategori)
}