package com.example.watermatters

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.watermatters.ui.*

enum class WatterMattersScreen() {
    QRScanner(),
    Users(),
    Prizes(),
    Info(),
}

@Composable
fun WaterMattersApp(){
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = WatterMattersScreen.valueOf(
        backStackEntry?.destination?.route ?: WatterMattersScreen.QRScanner.name
    )
    val viewModel: WaterMattersViewModel = viewModel()

    Scaffold(
        topBar = {
            WatterMattersTopAppBar(
                title = stringResource(id = R.string.app_name),
                infoClicked = {
                    navigateTo(WatterMattersScreen.Info.name, navController)
                }
            )
        },
        bottomBar = {
            WatterMattersBottomNavigationBar(
                navigationBarItemClicked = { route ->
                    navigateTo(route, navController)
                },
            )
        }
    ) {paddingValues ->
        val uiState by viewModel.uiState.collectAsState()
        NavHost(navController = navController, startDestination = currentScreen.name, modifier = Modifier.padding(paddingValues)) {
            composable(WatterMattersScreen.QRScanner.name) {
                QRScannerScreen(onConfirm = { userName ->
                    navigateTo(WatterMattersScreen.Users.name, navController)
                })
            }
            composable(WatterMattersScreen.Users.name) {
                UsersScreen()
            }
            composable(WatterMattersScreen.Prizes.name) {
                PrizesScreen()
            }
            composable(WatterMattersScreen.Info.name) {
                InfoScreen()
            }
        }
    }
}

private fun navigateTo(route: String, navController: NavController) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}