package com.example.remedialucp2.view.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remedialucp2.R
import com.example.remedialucp2.room.Buku
import com.example.remedialucp2.viewmodel.HomeBukuViewModel
import com.example.remedialucp2.viewmodel.provider.PenyediaViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBukuScreen(navigateToEntry: (Int) -> Unit, navigateToDetail: (Int) -> Unit, navigateBack: () -> Unit, modifier: Modifier = Modifier, viewModel: HomeBukuViewModel = viewModel(factory = PenyediaViewModel.Factory)) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeUiState by viewModel.homeUiState.collectAsState()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { BukuTopAppBar(title = "Buku - ${viewModel.namaKategori}", canNavigateBack = true, scrollBehavior = scrollBehavior, navigateUp = navigateBack) },
        floatingActionButton = { FloatingActionButton(onClick = { navigateToEntry(viewModel.kategoriId) }, shape = MaterialTheme.shapes.medium, modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))) { Icon(Icons.Default.Add, contentDescription = stringResource(R.string.tambah_buku)) } }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (homeUiState.listBuku.isEmpty()) {
                Text(text = stringResource(R.string.no_buku), textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge, modifier = Modifier.fillMaxWidth().padding(16.dp))
            } else {
                LazyColumn(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))) {
                    items(items = homeUiState.listBuku, key = { it.id }) { buku ->
                        CardBuku(buku = buku, onBukuClick = { navigateToDetail(buku.id) }, onDeleteClick = { viewModel.softDeleteBuku(buku) }, modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
                    }
                }
            }
        }
    }
}
@Composable
fun CardBuku(buku: Buku, onBukuClick: () -> Unit, onDeleteClick: () -> Unit, namaKategori: String? = null, modifier: Modifier = Modifier) {
    var deleteConfirmation by rememberSaveable { mutableStateOf(false) }
    Card(modifier = modifier.fillMaxWidth().clickable { onBukuClick() }, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.padding_medium)), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = buku.judul, style = MaterialTheme.typography.titleLarge)
                Text(text = "Tahun: ${buku.tahunTerbit}", style = MaterialTheme.typography.bodyMedium)
                if (namaKategori != null) { Text(text = "Kategori: $namaKategori", style = MaterialTheme.typography.bodySmall) }
                Text(text = "Status: ${buku.status}", style = MaterialTheme.typography.bodySmall, color = if (buku.status == "tersedia") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
            }
            IconButton(onClick = { deleteConfirmation = true }) { Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete)) }
        }
    }
    if (deleteConfirmation) { DeleteConfirmDialog(onDeleteConfirm = { deleteConfirmation = false; onDeleteClick() }, onDeleteCancel = { deleteConfirmation = false }) }
}
@Composable
fun DeleteConfirmDialog(onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit) {
    AlertDialog(onDismissRequest = onDeleteCancel, title = { Text(stringResource(R.string.attention)) }, text = { Text(stringResource(R.string.tanya_hapus)) }, dismissButton = { TextButton(onClick = onDeleteCancel) { Text(stringResource(R.string.no)) } }, confirmButton = { TextButton(onClick = onDeleteConfirm) { Text(stringResource(R.string.yes)) } })
}