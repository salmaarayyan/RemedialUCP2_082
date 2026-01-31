package com.example.remedialucp2.viewmodel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2.repositori.RepositoriPengarang
import com.example.remedialucp2.room.Pengarang
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
data class HomePengarangUiState(val listPengarang: List<Pengarang> = listOf())
data class DialogPengarangState(val isShowing: Boolean = false, val isEdit: Boolean = false, val pengarangId: Int = 0, val namaPengarang: String = "")
class HomePengarangViewModel(private val repositoriPengarang: RepositoriPengarang) : ViewModel() {
    val homeUiState: StateFlow<HomePengarangUiState> = repositoriPengarang.getAllPengarangStream()
        .map { HomePengarangUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomePengarangUiState())
    var dialogUiState by mutableStateOf(DialogPengarangState())
        private set
    fun showDialog(pengarang: Pengarang? = null) {
        dialogUiState = if (pengarang != null) {
            DialogPengarangState(isShowing = true, isEdit = true, pengarangId = pengarang.id, namaPengarang = pengarang.namaPengarang)
        } else {
            DialogPengarangState(isShowing = true)
        }
    }
    fun hideDialog() { dialogUiState = DialogPengarangState() }
    fun updateNamaPengarang(nama: String) { dialogUiState = dialogUiState.copy(namaPengarang = nama) }
    fun savePengarang() {
        viewModelScope.launch {
            if (dialogUiState.namaPengarang.isNotBlank()) {
                val pengarang = Pengarang(id = dialogUiState.pengarangId, namaPengarang = dialogUiState.namaPengarang)
                if (dialogUiState.isEdit) repositoriPengarang.updatePengarang(pengarang)
                else repositoriPengarang.insertPengarang(pengarang)
                hideDialog()
            }
        }
    }
    fun deletePengarang(pengarang: Pengarang) {
        viewModelScope.launch { repositoriPengarang.deletePengarang(pengarang) }
    }
}