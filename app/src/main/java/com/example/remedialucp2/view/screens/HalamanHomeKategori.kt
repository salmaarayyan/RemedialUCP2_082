package com.example.remedialucp2.view.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remedialucp2.R
import com.example.remedialucp2.room.Kategori
import com.example.remedialucp2.view.route.DestinasiHomeKategori
import com.example.remedialucp2.viewmodel.HomeKategoriViewModel
import com.example.remedialucp2.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeKategoriScreen(navigateToBuku: (Int) -> Unit, modifier: Modifier = Modifier, viewModel: HomeKategoriViewModel = viewModel(factory = PenyediaViewModel.Factory)) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeUiState by viewModel.homeUiState.collectAsState()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { BukuTopAppBar(title = stringResource(DestinasiHomeKategori.titleRes), canNavigateBack = false, scrollBehavior = scrollBehavior) },
        floatingActionButton = { FloatingActionButton(onClick = { viewModel.showDialog() }, shape = MaterialTheme.shapes.medium, modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))) { Icon(Icons.Default.Add, contentDescription = stringResource(R.string.tambah_kategori)) } }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (homeUiState.listKategori.isEmpty()) {
                Text(text = stringResource(R.string.no_kategori), textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge, modifier = Modifier.fillMaxWidth().padding(16.dp))
            } else {
                LazyColumn(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))) {
                    items(items = homeUiState.listKategori, key = { it.id }) { kategori ->
                        CardKategori(kategori = kategori, onKategoriClick = { navigateToBuku(kategori.id) }, onEditClick = { viewModel.showDialog(kategori) }, onDeleteClick = { viewModel.tryDeleteKategori(kategori) }, modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
                    }
                }
            }
        }
    }
    if (viewModel.dialogUiState.isShowing) {
        DialogInputKategori(isEdit = viewModel.dialogUiState.isEdit, namaKategori = viewModel.dialogUiState.namaKategori, onNamaChange = { viewModel.updateNamaKategori(it) }, onDismiss = { viewModel.hideDialog() }, onConfirm = { viewModel.saveKategori() })
    }
    if (viewModel.deleteDialogState.isShowing) {
        AlertDialog(onDismissRequest = { viewModel.hideDeleteDialog() }, title = { Text("Hapus Kategori") }, text = { Text(viewModel.deleteDialogState.message) }, dismissButton = { TextButton(onClick = { viewModel.hideDeleteDialog() }) { Text("Batal") } }, confirmButton = { Row { TextButton(onClick = { viewModel.deleteKategoriMoveBooks() }) { Text("Pindahkan Buku") }; TextButton(onClick = { viewModel.deleteKategoriWithBooks() }) { Text("Hapus Semua") } } })
    }
}
@Composable
fun CardKategori(kategori: Kategori, onKategoriClick: () -> Unit, onEditClick: () -> Unit, onDeleteClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth().clickable { onKategoriClick() }, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.padding_medium)), verticalAlignment = Alignment.CenterVertically) {
            Text(text = kategori.namaKategori, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            IconButton(onClick = onEditClick) { Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit)) }
            IconButton(onClick = onDeleteClick) { Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete)) }
        }
    }
}
@Composable
fun DialogInputKategori(isEdit: Boolean, namaKategori: String, onNamaChange: (String) -> Unit, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss, title = { Text(if (isEdit) stringResource(R.string.edit_kategori) else stringResource(R.string.tambah_kategori)) }, text = { OutlinedTextField(value = namaKategori, onValueChange = onNamaChange, label = { Text(stringResource(R.string.nama_kategori)) }, modifier = Modifier.fillMaxWidth(), singleLine = true) }, dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.batal)) } }, confirmButton = { TextButton(onClick = onConfirm, enabled = namaKategori.isNotBlank()) { Text(stringResource(R.string.simpan)) } })
}