package com.example.remedialucp2.view.route
import com.example.remedialucp2.R

object DestinasiEditBuku : DestinasiNavigasi {
    override val route = "edit_buku"
    override val titleRes = R.string.edit_buku
    const val bukuIdArg = "bukuId"
    val routeWithArgs = "$route/{$bukuIdArg}"
}