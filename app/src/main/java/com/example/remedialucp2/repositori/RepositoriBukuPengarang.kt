package com.example.remedialucp2.repositori

import com.example.remedialucp2.room.BukuPengarang
import com.example.remedialucp2.room.BukuPengarangDao
import com.example.remedialucp2.room.Pengarang
import kotlinx.coroutines.flow.Flow
interface RepositoriBukuPengarang {
    fun getPengarangByBukuStream(bukuId: Int): Flow<List<BukuPengarang>>
    fun getPengarangListByBukuStream(bukuId: Int): Flow<List<Pengarang>>
    suspend fun insertBukuPengarang(bukuPengarang: BukuPengarang)
    suspend fun deleteByBuku(bukuId: Int)
    suspend fun delete(bukuId: Int, pengarangId: Int)
}
class OfflineRepositoriBukuPengarang(
    private val bukuPengarangDao: BukuPengarangDao
) : RepositoriBukuPengarang {
    override fun getPengarangByBukuStream(bukuId: Int) = bukuPengarangDao.getPengarangByBuku(bukuId)
    override fun getPengarangListByBukuStream(bukuId: Int) = bukuPengarangDao.getPengarangListByBuku(bukuId)
    override suspend fun insertBukuPengarang(bukuPengarang: BukuPengarang) = bukuPengarangDao.insert(bukuPengarang)
    override suspend fun deleteByBuku(bukuId: Int) = bukuPengarangDao.deleteByBuku(bukuId)
    override suspend fun delete(bukuId: Int, pengarangId: Int) = bukuPengarangDao.delete(bukuId, pengarangId)
}