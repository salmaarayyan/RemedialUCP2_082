package com.example.remedialucp2.view.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.example.remedialucp2.room.Pengarang
import com.example.remedialucp2.view.route.DestinasiHomePengarang
import com.example.remedialucp2.viewmodel.HomePengarangViewModel
import com.example.remedialucp2.viewmodel.provider.PenyediaViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePengarangScreen(modifier: Modifier = Modifier, viewModel: HomePengarangViewModel = viewModel(factory = PenyediaViewModel.Factory)) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeUiState by viewModel.homeUiState.collectAsState()
    Scaffold(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = { BukuTopAppBar(title = stringResource(DestinasiHomePengarang.titleRes), canNavigateBack = false, scrollBehavior = scrollBehavior) }, floatingActionButton = { FloatingActionButton(onClick = { viewModel.showDialog() }, shape = MaterialTheme.shapes.medium, modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))) { Icon(Icons.Default.Add, contentDescription = stringResource(R.string.tambah_pengarang)) } }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (homeUiState.listPengarang.isEmpty()) { Text(text = stringResource(R.string.no_pengarang), textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge, modifier = Modifier.fillMaxWidth().padding(16.dp)) }
            else { LazyColumn(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))) { items(items = homeUiState.listPengarang, key = { it.id }) { pengarang -> CardPengarang(pengarang = pengarang, onEditClick = { viewModel.showDialog(pengarang) }, onDeleteClick = { viewModel.deletePengarang(pengarang) }, modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))) } } }
        }
    }
    if (viewModel.dialogUiState.isShowing) { DialogInputPengarang(isEdit = viewModel.dialogUiState.isEdit, namaPengarang = viewModel.dialogUiState.namaPengarang, onNamaChange = { viewModel.updateNamaPengarang(it) }, onDismiss = { viewModel.hideDialog() }, onConfirm = { viewModel.savePengarang() }) }
}
@Composable
fun CardPengarang(pengarang: Pengarang, onEditClick: () -> Unit, onDeleteClick: () -> Unit, modifier: Modifier = Modifier) {
    var deleteConfirmation by rememberSaveable { mutableStateOf(false) }
    Card(modifier = modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.padding_medium)), verticalAlignment = Alignment.CenterVertically) {
            Text(text = pengarang.namaPengarang, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            IconButton(onClick = onEditClick) { Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit)) }
            IconButton(onClick = { deleteConfirmation = true }) { Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete)) }
        }
    }
    if (deleteConfirmation) { AlertDialog(onDismissRequest = { deleteConfirmation = false }, title = { Text(stringResource(R.string.attention)) }, text = { Text(stringResource(R.string.tanya_hapus)) }, dismissButton = { TextButton(onClick = { deleteConfirmation = false }) { Text(stringResource(R.string.no)) } }, confirmButton = { TextButton(onClick = { deleteConfirmation = false; onDeleteClick() }) { Text(stringResource(R.string.yes)) } }) }
}
@Composable
fun DialogInputPengarang(isEdit: Boolean, namaPengarang: String, onNamaChange: (String) -> Unit, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss, title = { Text(if (isEdit) stringResource(R.string.edit_pengarang) else stringResource(R.string.tambah_pengarang)) }, text = { OutlinedTextField(value = namaPengarang, onValueChange = onNamaChange, label = { Text(stringResource(R.string.nama_pengarang)) }, modifier = Modifier.fillMaxWidth(), singleLine = true) }, dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.batal)) } }, confirmButton = { TextButton(onClick = onConfirm, enabled = namaPengarang.isNotBlank()) { Text(stringResource(R.string.simpan)) } })
}