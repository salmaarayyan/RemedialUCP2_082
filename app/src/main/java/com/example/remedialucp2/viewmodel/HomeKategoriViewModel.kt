package com.example.remedialucp2.viewmodel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2.repositori.RepositoriBuku
import com.example.remedialucp2.repositori.RepositoriKategori
import com.example.remedialucp2.room.Kategori
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
data class HomeKategoriUiState(val listKategori: List<Kategori> = listOf())
data class DialogKategoriState(val isShowing: Boolean = false, val isEdit: Boolean = false, val kategoriId: Int = 0, val namaKategori: String = "")
data class DeleteKategoriDialogState(val isShowing: Boolean = false, val kategori: Kategori? = null, val hasBukuDipinjam: Boolean = false, val message: String = "")
class HomeKategoriViewModel(
    private val repositoriKategori: RepositoriKategori,
    private val repositoriBuku: RepositoriBuku
) : ViewModel() {
    val homeUiState: StateFlow<HomeKategoriUiState> = repositoriKategori.getAllKategoriStream()
        .map { HomeKategoriUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeKategoriUiState())
    var dialogUiState by mutableStateOf(DialogKategoriState())
        private set
    var deleteDialogState by mutableStateOf(DeleteKategoriDialogState())
        private set
    fun showDialog(kategori: Kategori? = null) {
        dialogUiState = if (kategori != null) {
            DialogKategoriState(isShowing = true, isEdit = true, kategoriId = kategori.id, namaKategori = kategori.namaKategori)
        } else {
            DialogKategoriState(isShowing = true)
        }
    }
    fun hideDialog() { dialogUiState = DialogKategoriState() }
    fun updateNamaKategori(nama: String) { dialogUiState = dialogUiState.copy(namaKategori = nama) }
    fun saveKategori() {
        viewModelScope.launch {
            if (dialogUiState.namaKategori.isNotBlank()) {
                val kategori = Kategori(id = dialogUiState.kategoriId, namaKategori = dialogUiState.namaKategori)
                if (dialogUiState.isEdit) repositoriKategori.updateKategori(kategori)
                else repositoriKategori.insertKategori(kategori)
                hideDialog()
            }
        }
    }
    fun tryDeleteKategori(kategori: Kategori) {
        viewModelScope.launch {
            val dipinjam = repositoriBuku.countBukuDipinjamByKategori(kategori.id)
            deleteDialogState = if (dipinjam > 0) {
                DeleteKategoriDialogState(isShowing = true, kategori = kategori, hasBukuDipinjam = true, message = "Kategori memiliki $dipinjam buku dipinjam. Tidak dapat dihapus.")
            } else {
                DeleteKategoriDialogState(isShowing = true, kategori = kategori, message = "Hapus kategori ini? Pilih aksi untuk buku di dalamnya.")
            }
        }
    }
    fun hideDeleteDialog() { deleteDialogState = DeleteKategoriDialogState() }
    fun deleteKategoriWithBooks() {
        viewModelScope.launch {
            deleteDialogState.kategori?.let {
                repositoriBuku.softDeleteByKategori(it.id)
                repositoriKategori.deleteKategori(it)
            }
            hideDeleteDialog()
        }
    }
    fun deleteKategoriMoveBooks() {
        viewModelScope.launch {
            deleteDialogState.kategori?.let {
                repositoriBuku.setKategoriNull(it.id)
                repositoriKategori.deleteKategori(it)
            }
            hideDeleteDialog()
        }
    }
}