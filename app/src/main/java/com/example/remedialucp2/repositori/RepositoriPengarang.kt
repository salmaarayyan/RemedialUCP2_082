package com.example.remedialucp2.repositori

import com.example.remedialucp2.room.Pengarang
import com.example.remedialucp2.room.PengarangDao
import kotlinx.coroutines.flow.Flow
interface RepositoriPengarang {
    fun getAllPengarangStream(): Flow<List<Pengarang>>
    fun getPengarangStream(id: Int): Flow<Pengarang?>
    suspend fun insertPengarang(pengarang: Pengarang)
    suspend fun updatePengarang(pengarang: Pengarang)
    suspend fun deletePengarang(pengarang: Pengarang)
}
class OfflineRepositoriPengarang(
    private val pengarangDao: PengarangDao
) : RepositoriPengarang {
    override fun getAllPengarangStream() = pengarangDao.getAllPengarang()
    override fun getPengarangStream(id: Int) = pengarangDao.getPengarang(id)
    override suspend fun insertPengarang(pengarang: Pengarang) = pengarangDao.insert(pengarang)
    override suspend fun updatePengarang(pengarang: Pengarang) = pengarangDao.update(pengarang)
    override suspend fun deletePengarang(pengarang: Pengarang) = pengarangDao.delete(pengarang)
}