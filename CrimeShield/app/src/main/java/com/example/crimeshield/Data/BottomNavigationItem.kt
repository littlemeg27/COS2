package com.example.crimeshield.Data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean
)

val items = listOf(
    BottomNavigationItem("Home", Icons.Filled.Home, Icons.Outlined.Home, false),
    BottomNavigationItem("Map", Icons.Filled.LocationOn, Icons.Outlined.LocationOn, true),
    BottomNavigationItem("Create", Icons.Filled.AddCircle, Icons.Outlined.AddCircle, false),
    BottomNavigationItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings, false),
    BottomNavigationItem("Sent Reports", Icons.Filled.Info, Icons.Outlined.Info, false),
    BottomNavigationItem("News", Icons.Filled.Info, Icons.Outlined.Info, true),
    BottomNavigationItem("Missing", Icons.Filled.Info, Icons.Outlined.Info, false),
    BottomNavigationItem("Sex Offenders", Icons.Filled.Info, Icons.Outlined.Info, false)
)
