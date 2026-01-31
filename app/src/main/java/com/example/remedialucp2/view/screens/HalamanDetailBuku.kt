package com.example.remedialucp2.view.screens
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remedialucp2.R
import com.example.remedialucp2.view.route.DestinasiDetailBuku
import com.example.remedialucp2.viewmodel.DetailBuku
import com.example.remedialucp2.viewmodel.DetailBukuUiState
import com.example.remedialucp2.viewmodel.DetailBukuViewModel
import com.example.remedialucp2.viewmodel.provider.PenyediaViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBukuScreen(navigateBack: () -> Unit, navigateToEdit: (Int) -> Unit, modifier: Modifier = Modifier, viewModel: DetailBukuViewModel = viewModel(factory = PenyediaViewModel.Factory)) {
    val uiState by viewModel.uiDetailState.collectAsState()
    Scaffold(topBar = { BukuTopAppBar(title = stringResource(DestinasiDetailBuku.titleRes), canNavigateBack = true, navigateUp = navigateBack) }, floatingActionButton = { FloatingActionButton(onClick = { navigateToEdit(uiState.detailBuku.id) }, shape = MaterialTheme.shapes.medium, modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))) { Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit)) } }, modifier = modifier) { innerPadding ->
        BodyDetailBuku(detailUiState = uiState, onDelete = { viewModel.deleteBuku(); navigateBack() }, modifier = Modifier.padding(innerPadding).verticalScroll(rememberScrollState()))
    }
}
@Composable
private fun BodyDetailBuku(detailUiState: DetailBukuUiState, onDelete: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(dimensionResource(R.dimen.padding_medium)), verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))) {
        var deleteConfirmation by rememberSaveable { mutableStateOf(false) }
        CardDetailBuku(detailBuku = detailUiState.detailBuku, listPengarangNama = detailUiState.listPengarang.map { it.namaPengarang }, modifier = Modifier.fillMaxWidth())
        OutlinedButton(onClick = { deleteConfirmation = true }, shape = MaterialTheme.shapes.small, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.delete)) }
        if (deleteConfirmation) { DeleteConfirmDialog(onDeleteConfirm = { deleteConfirmation = false; onDelete() }, onDeleteCancel = { deleteConfirmation = false }) }
    }
}
@Composable
fun CardDetailBuku(detailBuku: DetailBuku, listPengarangNama: List<String>, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)) {
        Column(modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.padding_medium)), verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))) {
            BarisDetailBuku(labelResID = R.string.judul_buku, itemDetail = detailBuku.judul)
            BarisDetailBuku(labelResID = R.string.tahun_terbit, itemDetail = detailBuku.tahunTerbit)
            BarisDetailBuku(labelResID = R.string.kategori, itemDetail = detailBuku.namaKategori)
            BarisDetailBuku(labelResID = R.string.status, itemDetail = detailBuku.status)
            BarisDetailBuku(labelResID = R.string.pengarang, itemDetail = listPengarangNama.joinToString(", ").ifEmpty { "-" })
        }
    }
}
@Composable
private fun BarisDetailBuku(@StringRes labelResID: Int, itemDetail: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))) { Text(stringResource(labelResID)); Spacer(modifier = Modifier.weight(1f)); Text(text = itemDetail, fontWeight = FontWeight.Bold) }
}