package com.example.remedialucp2.viewmodel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2.repositori.RepositoriBuku
import com.example.remedialucp2.repositori.RepositoriBukuPengarang
import com.example.remedialucp2.repositori.RepositoriKategori
import com.example.remedialucp2.room.Pengarang
import com.example.remedialucp2.view.route.DestinasiDetailBuku
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
data class DetailBukuUiState(val detailBuku: DetailBuku = DetailBuku(), val listPengarang: List<Pengarang> = emptyList())
class DetailBukuViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriBuku: RepositoriBuku,
    private val repositoriKategori: RepositoriKategori,
    private val repositoriBukuPengarang: RepositoriBukuPengarang
) : ViewModel() {
    private val bukuId: Int = checkNotNull(savedStateHandle[DestinasiDetailBuku.bukuIdArg])
    val uiDetailState: StateFlow<DetailBukuUiState> = combine(
        repositoriBuku.getBukuStream(bukuId).filterNotNull(),
        repositoriBukuPengarang.getPengarangListByBukuStream(bukuId),
        repositoriKategori.getAllKategoriStream()
    ) { buku, pengarangList, kategoriList ->
        val namaKategori = kategoriList.find { it.id == buku.kategoriId }?.namaKategori ?: "Tanpa Kategori"
        DetailBukuUiState(
            detailBuku = buku.toDetailBuku(namaKategori, pengarangList.map { it.id }),
            listPengarang = pengarangList
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DetailBukuUiState())
    fun deleteBuku() {
        viewModelScope.launch { repositoriBuku.softDeleteBuku(bukuId) }
    }
}