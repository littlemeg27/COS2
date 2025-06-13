package com.example.crimeshield.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.crimeshield.Operations.items

@Composable
fun MissingScreen(navController: NavController)
{
    val selectedItemIndex by rememberSaveable { mutableIntStateOf(2) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.hasNews) {
                                        Badge()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (currentDestination?.hierarchy?.any { it.route == item.title.lowercase() } == true) {
                                        item.selectedIcon
                                    } else {
                                        item.unselectedIcon
                                    },
                                    contentDescription = item.title
                                )
                            }
                        },
                        label = { Text(text = item.title) },
                        alwaysShowLabel = false,
                        selected = currentDestination?.hierarchy?.any { it.route == item.title.lowercase() } == true,
                        onClick = {
                            navController.navigate(item.title.lowercase()) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            var textState1 by remember { mutableStateOf("Name") }
            var textState2 by remember { mutableStateOf("Phone Number") }
            var textState3 by remember { mutableStateOf("Details") }

            Text(
                text = "Crime Shield",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp, bottom = 5.dp)
            )
            Text(
                text = "Report!",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            TextField(
                value = textState1,
                onValueChange = { textState1 = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Person"
                    )
                }
            )
            TextField(
                value = textState2,
                onValueChange = { textState2 = it },
                modifier = Modifier.padding(20.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Call,
                        contentDescription = "Number"
                    )
                }
            )
            Button(
                modifier = Modifier
                    .height(40.dp)
                    .width(150.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                ),
                onClick = { /* Implement location logic */ }
            ) {
                Text(text = "Get Location")
            }
            TextField(
                value = textState3,
                onValueChange = { textState3 = it },
                maxLines = Int.MAX_VALUE,
                singleLine = false,
                modifier = Modifier.padding(20.dp)
            )
            Button(
                modifier = Modifier
                    .height(50.dp)
                    .width(190.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                onClick = { /* Implement submit logic */ }
            ) {
                Text(text = "Submit")
            }
        }
    }
}