package com.example.remedialucp2.view.uicontroller
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.remedialucp2.view.route.*
import com.example.remedialucp2.view.screens.*
@Composable
fun BukuApp(navController: NavHostController = rememberNavController(), modifier: Modifier = Modifier) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in listOf(DestinasiHomeKategori.route, DestinasiSemuaBuku.route, DestinasiHomePengarang.route)
    Scaffold(bottomBar = { if (showBottomBar) { BottomNavigationBar(currentRoute = currentRoute, onNavigate = { route -> navController.navigate(route) { popUpTo(DestinasiHomeKategori.route) { inclusive = false }; launchSingleTop = true } }) } }) { innerPadding ->
        HostNavigasi(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}
@Composable
fun BottomNavigationBar(currentRoute: String?, onNavigate: (String) -> Unit) {
    NavigationBar {
        NavigationBarItem(icon = { Icon(Icons.Default.Home, contentDescription = "Kategori") }, label = { Text("Kategori") }, selected = currentRoute == DestinasiHomeKategori.route, onClick = { onNavigate(DestinasiHomeKategori.route) })
        NavigationBarItem(icon = { Icon(Icons.Default.List, contentDescription = "Semua Buku") }, label = { Text("Semua Buku") }, selected = currentRoute == DestinasiSemuaBuku.route, onClick = { onNavigate(DestinasiSemuaBuku.route) })
        NavigationBarItem(icon = { Icon(Icons.Default.Person, contentDescription = "Pengarang") }, label = { Text("Pengarang") }, selected = currentRoute == DestinasiHomePengarang.route, onClick = { onNavigate(DestinasiHomePengarang.route) })
    }
}
@Composable
fun HostNavigasi(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = DestinasiHomeKategori.route, modifier = modifier) {
        composable(DestinasiHomeKategori.route) { HomeKategoriScreen(navigateToBuku = { kategoriId -> navController.navigate("${DestinasiHomeBuku.route}/$kategoriId") }) }
        composable(DestinasiSemuaBuku.route) { SemuaBukuScreen(navigateToDetail = { bukuId -> navController.navigate("${DestinasiDetailBuku.route}/$bukuId") }) }
        composable(DestinasiHomePengarang.route) { HomePengarangScreen() }
        composable(route = DestinasiHomeBuku.routeWithArgs, arguments = listOf(navArgument(DestinasiHomeBuku.kategoriIdArg) { type = NavType.IntType })) { HomeBukuScreen(navigateToEntry = { kategoriId -> navController.navigate("${DestinasiEntryBuku.route}/$kategoriId") }, navigateToDetail = { bukuId -> navController.navigate("${DestinasiDetailBuku.route}/$bukuId") }, navigateBack = { navController.popBackStack() }) }
        composable(route = DestinasiEntryBuku.routeWithArgs, arguments = listOf(navArgument(DestinasiEntryBuku.kategoriIdArg) { type = NavType.IntType })) { EntryBukuScreen(navigateBack = { navController.popBackStack() }) }
        composable(route = DestinasiDetailBuku.routeWithArgs, arguments = listOf(navArgument(DestinasiDetailBuku.bukuIdArg) { type = NavType.IntType })) { DetailBukuScreen(navigateBack = { navController.popBackStack() }, navigateToEdit = { bukuId -> navController.navigate("${DestinasiEditBuku.route}/$bukuId") }) }
        composable(route = DestinasiEditBuku.routeWithArgs, arguments = listOf(navArgument(DestinasiEditBuku.bukuIdArg) { type = NavType.IntType })) { EditBukuScreen(navigateBack = { navController.popBackStack() }) }
    }
}