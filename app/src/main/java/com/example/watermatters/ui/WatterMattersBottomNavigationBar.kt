package com.example.watermatters.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.watermatters.WatterMattersScreen
import com.example.watermatters.R
@Composable
fun WatterMattersBottomNavigationBar(
    navigationBarItemClicked : (route: String) -> Unit,
){
    NavigationBar() {
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.ic_qr_code_scanner), contentDescription = "QRScanner") },
            label = { Text("QRScanner") },
            selected = false,
            onClick = { navigationBarItemClicked(WatterMattersScreen.QRScanner.name) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.List, contentDescription = "Users") },
            label = { Text("Users") },
            selected = false,
            onClick = { navigationBarItemClicked(WatterMattersScreen.Users.name) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.CheckCircle, contentDescription = "Prizes") },
            label = { Text("Prizes") },
            selected = false,
            onClick = { navigationBarItemClicked(WatterMattersScreen.Prizes.name) }
        )
    }
}