package com.example.remedialucp2.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2.repositori.RepositoriBuku
import com.example.remedialucp2.repositori.RepositoriKategori
import com.example.remedialucp2.room.Buku
import com.example.remedialucp2.room.Kategori
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
data class HomeSemuaBukuUiState(val listBuku: List<Buku> = listOf(), val listKategori: List<Kategori> = listOf())
class HomeSemuaBukuViewModel(
    private val repositoriBuku: RepositoriBuku,
    private val repositoriKategori: RepositoriKategori
) : ViewModel() {
    val homeUiState: StateFlow<HomeSemuaBukuUiState> = combine(
        repositoriBuku.getAllBukuStream(),
        repositoriKategori.getAllKategoriStream()
    ) { buku, kategori -> HomeSemuaBukuUiState(buku, kategori) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeSemuaBukuUiState())
    fun getNamaKategori(kategoriId: Int?, listKategori: List<Kategori>): String {
        return listKategori.find { it.id == kategoriId }?.namaKategori ?: "Tanpa Kategori"
    }
}