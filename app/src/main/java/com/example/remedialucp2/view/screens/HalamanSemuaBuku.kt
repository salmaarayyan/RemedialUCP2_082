package com.example.remedialucp2.view.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remedialucp2.R
import com.example.remedialucp2.view.route.DestinasiSemuaBuku
import com.example.remedialucp2.viewmodel.HomeSemuaBukuViewModel
import com.example.remedialucp2.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemuaBukuScreen(navigateToDetail: (Int) -> Unit, modifier: Modifier = Modifier, viewModel: HomeSemuaBukuViewModel = viewModel(factory = PenyediaViewModel.Factory)) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeUiState by viewModel.homeUiState.collectAsState()
    Scaffold(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = { BukuTopAppBar(title = stringResource(DestinasiSemuaBuku.titleRes), canNavigateBack = false, scrollBehavior = scrollBehavior) }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (homeUiState.listBuku.isEmpty()) {
                Text(text = stringResource(R.string.no_buku), textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge, modifier = Modifier.fillMaxWidth().padding(16.dp))
            } else {
                LazyColumn(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))) {
                    items(items = homeUiState.listBuku, key = { it.id }) { buku ->
                        CardBuku(buku = buku, onBukuClick = { navigateToDetail(buku.id) }, onDeleteClick = {}, namaKategori = viewModel.getNamaKategori(buku.kategoriId, homeUiState.listKategori), modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
                    }
                }
            }
        }
    }
}