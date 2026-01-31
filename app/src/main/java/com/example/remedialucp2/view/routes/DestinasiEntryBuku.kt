package com.example.remedialucp2.view.route
import com.example.remedialucp2.R

object DestinasiEntryBuku : DestinasiNavigasi {
    override val route = "entry_buku"
    override val titleRes = R.string.entry_buku
    const val kategoriIdArg = "kategoriId"
    val routeWithArgs = "$route/{$kategoriIdArg}"
}