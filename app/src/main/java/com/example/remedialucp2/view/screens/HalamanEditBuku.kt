package com.example.remedialucp2.view.screens
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remedialucp2.R
import com.example.remedialucp2.view.route.DestinasiEditBuku
import com.example.remedialucp2.viewmodel.EditBukuViewModel
import com.example.remedialucp2.viewmodel.provider.PenyediaViewModel
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBukuScreen(navigateBack: () -> Unit, modifier: Modifier = Modifier, viewModel: EditBukuViewModel = viewModel(factory = PenyediaViewModel.Factory)) {
    val coroutineScope = rememberCoroutineScope()
    val listPengarang by viewModel.listPengarang.collectAsState()
    Scaffold(topBar = { BukuTopAppBar(title = stringResource(DestinasiEditBuku.titleRes), canNavigateBack = true, navigateUp = navigateBack) }) { innerPadding ->
        EntryBodyBuku(uiStateBuku = viewModel.uiStateBuku, listPengarang = listPengarang, onBukuValueChange = viewModel::updateUiState, onSaveClick = { coroutineScope.launch { viewModel.updateBuku(); navigateBack() } }, modifier = Modifier.padding(innerPadding).verticalScroll(rememberScrollState()).fillMaxWidth())
    }
}