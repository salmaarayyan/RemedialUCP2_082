package com.example.remedialucp2.view.screens
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.remedialucp2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BukuTopAppBar(title: String, canNavigateBack: Boolean, modifier: Modifier = Modifier, scrollBehavior: TopAppBarScrollBehavior? = null, navigateUp: () -> Unit = {}) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back)) }
            }
        }
    )
}