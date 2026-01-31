package com.example.remedialucp2.viewmodel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2.repositori.RepositoriBuku
import com.example.remedialucp2.repositori.RepositoriKategori
import com.example.remedialucp2.room.Buku
import com.example.remedialucp2.view.route.DestinasiHomeBuku
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
data class HomeBukuUiState(val listBuku: List<Buku> = listOf())
class HomeBukuViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriBuku: RepositoriBuku,
    private val repositoriKategori: RepositoriKategori
) : ViewModel() {
    val kategoriId: Int = checkNotNull(savedStateHandle[DestinasiHomeBuku.kategoriIdArg])
    val namaKategori: String = runBlocking {
        repositoriKategori.getKategoriStream(kategoriId).first()?.namaKategori ?: "Kategori"
    }
    val homeUiState: StateFlow<HomeBukuUiState> = repositoriBuku.getBukuByKategoriStream(kategoriId)
        .map { HomeBukuUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeBukuUiState())
    fun softDeleteBuku(buku: Buku) {
        viewModelScope.launch { repositoriBuku.softDeleteBuku(buku.id) }
    }
}