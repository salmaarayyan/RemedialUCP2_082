package com.example.remedialucp2.viewmodel.provider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.remedialucp2.repositori.AplikasiBuku
import com.example.remedialucp2.viewmodel.*
object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            HomeKategoriViewModel(aplikasiBuku().container.repositoriKategori, aplikasiBuku().container.repositoriBuku)
        }
        initializer {
            HomeBukuViewModel(this.createSavedStateHandle(), aplikasiBuku().container.repositoriBuku, aplikasiBuku().container.repositoriKategori)
        }
        initializer {
            HomeSemuaBukuViewModel(aplikasiBuku().container.repositoriBuku, aplikasiBuku().container.repositoriKategori)
        }
        initializer {
            EntryBukuViewModel(this.createSavedStateHandle(), aplikasiBuku().container.repositoriBuku, aplikasiBuku().container.repositoriKategori, aplikasiBuku().container.repositoriPengarang, aplikasiBuku().container.repositoriBukuPengarang)
        }
        initializer {
            DetailBukuViewModel(this.createSavedStateHandle(), aplikasiBuku().container.repositoriBuku, aplikasiBuku().container.repositoriKategori, aplikasiBuku().container.repositoriBukuPengarang)
        }
        initializer {
            EditBukuViewModel(this.createSavedStateHandle(), aplikasiBuku().container.repositoriBuku, aplikasiBuku().container.repositoriKategori, aplikasiBuku().container.repositoriPengarang, aplikasiBuku().container.repositoriBukuPengarang)
        }
        initializer {
            HomePengarangViewModel(aplikasiBuku().container.repositoriPengarang)
        }
    }
}
fun CreationExtras.aplikasiBuku(): AplikasiBuku = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AplikasiBuku)