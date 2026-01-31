package com.example.remedialucp2.view.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remedialucp2.R
import com.example.remedialucp2.room.Pengarang
import com.example.remedialucp2.view.route.DestinasiEntryBuku
import com.example.remedialucp2.viewmodel.DetailBuku
import com.example.remedialucp2.viewmodel.EntryBukuViewModel
import com.example.remedialucp2.viewmodel.UIStateBuku
import com.example.remedialucp2.viewmodel.provider.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryBukuScreen(navigateBack: () -> Unit, modifier: Modifier = Modifier, viewModel: EntryBukuViewModel = viewModel(factory = PenyediaViewModel.Factory)) {
    val coroutineScope = rememberCoroutineScope()
    val listPengarang by viewModel.listPengarang.collectAsState()
    Scaffold(topBar = { BukuTopAppBar(title = stringResource(DestinasiEntryBuku.titleRes), canNavigateBack = true, navigateUp = navigateBack) }) { innerPadding ->
        EntryBodyBuku(uiStateBuku = viewModel.uiStateBuku, listPengarang = listPengarang, onBukuValueChange = viewModel::updateUiState, onSaveClick = { coroutineScope.launch { viewModel.saveBuku(); navigateBack() } }, modifier = Modifier.padding(innerPadding).verticalScroll(rememberScrollState()).fillMaxWidth())
    }
}
@Composable
fun EntryBodyBuku(uiStateBuku: UIStateBuku, listPengarang: List<Pengarang>, onBukuValueChange: (DetailBuku) -> Unit, onSaveClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_large)), modifier = modifier.padding(dimensionResource(R.dimen.padding_medium))) {
        FormInputBuku(detailBuku = uiStateBuku.detailBuku, listPengarang = listPengarang, onValueChange = onBukuValueChange, modifier = Modifier.fillMaxWidth())
        Button(onClick = onSaveClick, enabled = uiStateBuku.isEntryValid, shape = MaterialTheme.shapes.small, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.simpan)) }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputBuku(detailBuku: DetailBuku, listPengarang: List<Pengarang>, onValueChange: (DetailBuku) -> Unit, modifier: Modifier = Modifier) {
    var statusExpanded by remember { mutableStateOf(false) }
    val statusOptions = listOf("tersedia", "dipinjam")
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))) {
        OutlinedTextField(value = detailBuku.namaKategori, onValueChange = {}, label = { Text(stringResource(R.string.kategori)) }, modifier = Modifier.fillMaxWidth(), enabled = false, singleLine = true)
        OutlinedTextField(value = detailBuku.judul, onValueChange = { onValueChange(detailBuku.copy(judul = it)) }, label = { Text(stringResource(R.string.judul_buku)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(value = detailBuku.tahunTerbit, onValueChange = { onValueChange(detailBuku.copy(tahunTerbit = it)) }, label = { Text(stringResource(R.string.tahun_terbit)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        ExposedDropdownMenuBox(expanded = statusExpanded, onExpandedChange = { statusExpanded = !statusExpanded }) {
            OutlinedTextField(value = detailBuku.status, onValueChange = {}, readOnly = true, label = { Text(stringResource(R.string.status)) }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) }, modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth())
            ExposedDropdownMenu(expanded = statusExpanded, onDismissRequest = { statusExpanded = false }) { statusOptions.forEach { status -> DropdownMenuItem(text = { Text(status) }, onClick = { onValueChange(detailBuku.copy(status = status)); statusExpanded = false }) } }
        }
        Text(stringResource(R.string.pilih_pengarang), style = MaterialTheme.typography.titleMedium)
        Column { listPengarang.forEach { pengarang -> Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(checked = detailBuku.selectedPengarangIds.contains(pengarang.id), onCheckedChange = { checked -> val newList = if (checked) detailBuku.selectedPengarangIds + pengarang.id else detailBuku.selectedPengarangIds - pengarang.id; onValueChange(detailBuku.copy(selectedPengarangIds = newList)) }); Text(pengarang.namaPengarang) } } }
        if (listPengarang.isEmpty()) { Text(text = stringResource(R.string.tambah_pengarang_dulu), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error) }
        HorizontalDivider()
        Text(stringResource(R.string.required_note), modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium)))
    }
}