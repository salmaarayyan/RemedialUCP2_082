package com.example.remedialucp2.view.route
import com.example.remedialucp2.R

object DestinasiHomeBuku : DestinasiNavigasi {
    override val route = "home_buku"
    override val titleRes = R.string.daftar_buku
    const val kategoriIdArg = "kategoriId"
    val routeWithArgs = "$route/{$kategoriIdArg}"
}