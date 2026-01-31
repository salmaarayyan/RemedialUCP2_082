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
import com.example.remedialucp2.room.Buku
import com.example.remedialucp2.room.BukuPengarang
import com.example.remedialucp2.room.Pengarang
import com.example.remedialucp2.view.route.DestinasiEntryBuku
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
data class UIStateBuku(val detailBuku: DetailBuku = DetailBuku(), val isEntryValid: Boolean = false)
data class DetailBuku(
    val id: Int = 0,
    val judul: String = "",
    val tahunTerbit: String = "",
    val kategoriId: Int? = null,
    val namaKategori: String = "",
    val status: String = "tersedia",
    val selectedPengarangIds: List<Int> = emptyList()
)
fun DetailBuku.toBuku(): Buku = Buku(
    id = id,
    judul = judul,
    tahunTerbit = tahunTerbit.toIntOrNull() ?: 0,
    kategoriId = kategoriId,
    status = status
)
fun Buku.toDetailBuku(namaKategori: String, pengarangIds: List<Int>): DetailBuku = DetailBuku(
    id = id,
    judul = judul,
    tahunTerbit = tahunTerbit.toString(),
    kategoriId = kategoriId,
    namaKategori = namaKategori,
    status = status,
    selectedPengarangIds = pengarangIds
)
class EntryBukuViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriBuku: RepositoriBuku,
    private val repositoriKategori: RepositoriKategori,
    private val repositoriPengarang: RepositoriPengarang,
    private val repositoriBukuPengarang: RepositoriBukuPengarang
) : ViewModel() {
    private val kategoriId: Int = checkNotNull(savedStateHandle[DestinasiEntryBuku.kategoriIdArg])
    private val namaKategori: String = runBlocking {
        repositoriKategori.getKategoriStream(kategoriId).first()?.namaKategori ?: "Kategori"
    }
    var uiStateBuku by mutableStateOf(UIStateBuku(detailBuku = DetailBuku(kategoriId = kategoriId, namaKategori = namaKategori)))
        private set
    val listPengarang: StateFlow<List<Pengarang>> = repositoriPengarang.getAllPengarangStream()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    fun updateUiState(detailBuku: DetailBuku) {
        uiStateBuku = UIStateBuku(detailBuku = detailBuku, isEntryValid = validasiInput(detailBuku))
    }
    private fun validasiInput(uiState: DetailBuku = uiStateBuku.detailBuku): Boolean {
        return with(uiState) { judul.isNotBlank() && tahunTerbit.isNotBlank() && selectedPengarangIds.isNotEmpty() }
    }
    suspend fun saveBuku() {
        if (validasiInput()) {
            val bukuId = repositoriBuku.insertBuku(uiStateBuku.detailBuku.toBuku()).toInt()
            uiStateBuku.detailBuku.selectedPengarangIds.forEach { pengarangId ->
                repositoriBukuPengarang.insertBukuPengarang(BukuPengarang(bukuId = bukuId, pengarangId = pengarangId))
            }
        }
    }
}