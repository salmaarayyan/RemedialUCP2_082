package com.example.remedialucp2.viewmodel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2.repositori.RepositoriBuku
import com.example.remedialucp2.repositori.RepositoriBukuPengarang
import com.example.remedialucp2.repositori.RepositoriKategori
import com.example.remedialucp2.repositori.RepositoriPengarang
import com.example.remedialucp2.room.BukuPengarang
import com.example.remedialucp2.room.Pengarang
import com.example.remedialucp2.view.route.DestinasiEditBuku
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
class EditBukuViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriBuku: RepositoriBuku,
    private val repositoriKategori: RepositoriKategori,
    private val repositoriPengarang: RepositoriPengarang,
    private val repositoriBukuPengarang: RepositoriBukuPengarang
) : ViewModel() {
    private val bukuId: Int = checkNotNull(savedStateHandle[DestinasiEditBuku.bukuIdArg])
    var uiStateBuku by mutableStateOf(UIStateBuku())
        private set
    val listPengarang: StateFlow<List<Pengarang>> = repositoriPengarang.getAllPengarangStream()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    init {
        viewModelScope.launch {
            val buku = repositoriBuku.getBukuStream(bukuId).filterNotNull().first()
            val namaKategori = buku.kategoriId?.let { repositoriKategori.getKategoriStream(it).first()?.namaKategori } ?: "Tanpa Kategori"
            val pengarangIds = repositoriBukuPengarang.getPengarangByBukuStream(bukuId).first().map { it.pengarangId }
            uiStateBuku = UIStateBuku(detailBuku = buku.toDetailBuku(namaKategori, pengarangIds), isEntryValid = true)
        }
    }
    fun updateUiState(detailBuku: DetailBuku) {
        uiStateBuku = UIStateBuku(detailBuku = detailBuku, isEntryValid = validasiInput(detailBuku))
    }
    private fun validasiInput(uiState: DetailBuku = uiStateBuku.detailBuku): Boolean {
        return with(uiState) { judul.isNotBlank() && tahunTerbit.isNotBlank() && selectedPengarangIds.isNotEmpty() }
    }
    suspend fun updateBuku() {
        if (validasiInput()) {
            repositoriBuku.updateBuku(uiStateBuku.detailBuku.toBuku())
            repositoriBukuPengarang.deleteByBuku(bukuId)
            uiStateBuku.detailBuku.selectedPengarangIds.forEach { pengarangId ->
                repositoriBukuPengarang.insertBukuPengarang(BukuPengarang(bukuId = bukuId, pengarangId = pengarangId))
            }
        }
    }
}