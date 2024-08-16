
package com.example.crimeshield

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.crimeshield.ui.theme.CrimeShieldTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean
)

//Information for UI
val items = listOf(
    BottomNavigationItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNews = false,
    ),
    BottomNavigationItem(
        title = "Map",
        selectedIcon = Icons.Filled.LocationOn,
        unselectedIcon = Icons.Outlined.LocationOn,
        hasNews = true
    ),
    BottomNavigationItem(
        title = "Home",
        selectedIcon = Icons.Filled.AddCircle,
        unselectedIcon = Icons.Outlined.AddCircle,
        hasNews = false
    ),
    BottomNavigationItem(
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        hasNews = false
    )
)
//Information for UI


class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if(!hasRequiredPermissions())
        {
            ActivityCompat.requestPermissions(
                    this, CAMERAX_PERMISSIONS, 0
                    )
        }
        setContent {
            CrimeShieldTheme{
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background)
                {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "home")
                    {
                        composable("home") { HomeScreen(navController, startDestination = "home") }
                        composable("details") { MapScreen(navController, startDestination = "details") }
                        composable("create") { CreateScreen(navController, startDestination = "create") }
                        composable("settings") { SettingsScreen(navController, startDestination = "settings") }) }
                    }
                }
            }
        }
    }
    private fun hasRequiredPermissions(): Boolean{
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it) == PackageManager.PERMISSION_GRANTED

        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }
}





@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, startDestination: String)
{
    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar =
        {
            NavigationBar{
                items.forEachIndexed { index, item ->
                    val isSelected = item.title.lowercase() == navBackStackEntry?.destination?.route

                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick =
                        {
                            selectedItemIndex = index
                            navController.navigate(item.title.lowercase())
                            {
                                popUpTo(navController.graph.findStartDestination().id)
                                {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = {
                            Text(text = item.title)
                        },
                        alwaysShowLabel = false,
                        icon =
                        {
                            Icon(
                                imageVector = if (isSelected)
                                {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                            BadgedBox(
                                badge =
                                {
                                    if (item.hasNews) {
                                        Badge {
                                            Badge()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        }
                    )
                }
            }
        }
    ) {
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "CRIME SHIELD",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 20.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.crimeshieldicon),
            contentDescription = "Icon",
            modifier = Modifier
                .padding(top = 10.dp)
        )
        Text(
            text = "Create a Report!",
            fontSize = 25.sp,
            modifier = Modifier
                .padding(top = 10.dp)
        )
        Button(
            modifier = Modifier
                .height(70.dp)
                .width(190.dp)
                .padding(top = 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            onClick = {}
        )
        {
            Text(text = "Create a Report!")
        }
        Image(
            painter = painterResource(id = R.drawable.map),
            contentDescription = "Map",
            modifier = Modifier
                .padding(top = 10.dp)
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MapScreen(navController: NavController, startDestination: String)
{
    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar =
        {
            NavigationBar{
                items.forEachIndexed { index, item ->
                    val isSelected = item.title.lowercase() == navBackStackEntry?.destination?.route

                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick =
                        {
                            selectedItemIndex = index
                            navController.navigate(item.title.lowercase())
                            {
                                popUpTo(navController.graph.findStartDestination().id)
                                {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = {
                            Text(text = item.title)
                        },
                        alwaysShowLabel = false,
                        icon =
                        {
                            Icon(
                                imageVector = if (isSelected)
                                {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                            BadgedBox(
                                badge =
                                {
                                    if (item.hasNews) {
                                        Badge {
                                            Badge()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        }
                    )
                }
            }
        }
    ) {
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "Map",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(20.dp))
        Image(
            painter = painterResource(id = R.drawable.map2),
            contentDescription = "Map",
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp)
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateScreen(navController: NavController, startDestination: String)
{


    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar =
        {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    val isSelected = item.title.lowercase() == navBackStackEntry?.destination?.route

                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick =
                        {
                            selectedItemIndex = index
                            navController.navigate(item.title.lowercase())
                            {
                                popUpTo(navController.graph.findStartDestination().id)
                                {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label =
                        {
                            Text(text = item.title)
                        },
                        alwaysShowLabel = false,
                        icon =
                        {
                            Icon(
                                imageVector = if (isSelected)
                                {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                            BadgedBox(
                                badge =
                                {
                                    if (item.hasNews)
                                    {
                                        Badge {
                                            Badge()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        }
                    )
                }
            }
        }
    ) {

    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        var textState1 by remember { mutableStateOf("Name") }
        var textState2 by remember { mutableStateOf("Phone Number") }
        var textState3 by remember { mutableStateOf("Details") }

        Text(
            text = "Crime Shield",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 20.dp, bottom = 5.dp),
        )
        Text(
            text = "Report!",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 20.dp),
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
            modifier = Modifier
                .padding(20.dp),
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
            onClick = { }
        )
        {
            Text(text = "Get Location")
        }
        TextField(
            value = textState3,
            onValueChange = { textState3 = it },
            maxLines = Int.MAX_VALUE,
            singleLine = false,
            modifier = Modifier
                .padding(20.dp)
        )

        Button(
            modifier = Modifier
                .height(50.dp)
                .width(190.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            onClick = { }
        )
        {
            Text(text = "Submit")
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(navController: NavController, startDestination: String)
{
    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    val isSelected = item.title.lowercase() == navBackStackEntry?.destination?.route

                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.title.lowercase())
                            {
                                popUpTo(navController.graph.findStartDestination().id)
                                {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label =
                        {
                            Text(text = item.title)
                        },
                        alwaysShowLabel = false,
                        icon =
                        {
                            Icon(
                                imageVector = if (isSelected)
                                {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                            BadgedBox(
                                badge =
                                {
                                    if (item.hasNews)
                                    {
                                        Badge {
                                            Badge()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        }
                    )
                }
            }
        }
    ) {

    }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp, start = 125.dp),
            text = "Settings",
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SentReportsScreen(navController: NavController, startDestination: String)
{
    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    val isSelected = item.title.lowercase() == navBackStackEntry?.destination?.route

                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.title.lowercase())
                            {
                                popUpTo(navController.graph.findStartDestination().id)
                                {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label =
                        {
                            Text(text = item.title)
                        },
                        alwaysShowLabel = false,
                        icon =
                        {
                            Icon(
                                imageVector = if (isSelected)
                                {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                            BadgedBox(
                                badge =
                                {
                                    if (item.hasNews)
                                    {
                                        Badge {
                                            Badge()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        }
                    )
                }
            }
        }
    ) {

    }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp, start = 80.dp),
            text = "Sent Reports",
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewsScreen(navController: NavController, startDestination: String)
{
    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    val isSelected = item.title.lowercase() == navBackStackEntry?.destination?.route

                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.title.lowercase())
                            {
                                popUpTo(navController.graph.findStartDestination().id)
                                {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label =
                        {
                            Text(text = item.title)
                        },
                        alwaysShowLabel = false,
                        icon =
                        {
                            Icon(
                                imageVector = if (isSelected)
                                {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                            BadgedBox(
                                badge =
                                {
                                    if (item.hasNews)
                                    {
                                        Badge {
                                            Badge()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        }
                    )
                }
            }
        }
    ) {

    }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp, start = 150.dp),
            text = "News",
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MissingScreen(navController: NavController, startDestination: String)
{
    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    val isSelected = item.title.lowercase() == navBackStackEntry?.destination?.route

                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.title.lowercase())
                            {
                                popUpTo(navController.graph.findStartDestination().id)
                                {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label =
                        {
                            Text(text = item.title)
                        },
                        alwaysShowLabel = false,
                        icon =
                        {
                            Icon(
                                imageVector = if (isSelected)
                                {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                            BadgedBox(
                                badge =
                                {
                                    if (item.hasNews)
                                    {
                                        Badge {
                                            Badge()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        }
                    )
                }
            }
        }
    ) {

    }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp, start = 120.dp),
            text = "Missing",
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SexOffendersScreen(navController: NavController, startDestination: String)
{
    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    val isSelected = item.title.lowercase() == navBackStackEntry?.destination?.route

                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.title.lowercase())
                            {
                                popUpTo(navController.graph.findStartDestination().id)
                                {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label =
                        {
                            Text(text = item.title)
                        },
                        alwaysShowLabel = false,
                        icon =
                        {
                            Icon(
                                imageVector = if (isSelected)
                                {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                            BadgedBox(
                                badge =
                                {
                                    if (item.hasNews)
                                    {
                                        Badge {
                                            Badge()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        }
                    )
                }
            }
        }
    ) {

    }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp, start = 70.dp),
            text = "Sex Offenders",
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )
    }
}


//To show the App
@Preview(showBackground = true, name = "Home Preview")
@Composable
fun PreviewHomeView()
{
    CrimeShieldTheme {
        HomeScreen(navController = rememberNavController(), startDestination = "Home" )
    }
}

@Preview(showBackground = true, name = "Map Preview")
@Composable
fun PreviewMapView()
{
    CrimeShieldTheme {
        MapScreen(navController = rememberNavController(), startDestination = "Map")
    }
}

@Preview(showBackground = true, name = "Create Preview")
@Composable
fun PreviewCreateView()
{
    CrimeShieldTheme {
        CreateScreen(navController = rememberNavController(), startDestination = "Create")
    }
}

@Preview(showBackground = true, name = "Sent Reports Preview")
@Composable
fun PreviewSentReportsView()
{
    CrimeShieldTheme {
        SentReportsScreen(navController = rememberNavController(), startDestination = "Sent Reports")
    }
}

@Preview(showBackground = true, name = "Settings Preview")
@Composable
fun PreviewSettingsView()
{
    CrimeShieldTheme {
        SettingsScreen(navController = rememberNavController(), startDestination = "Settings")
    }
}

@Preview(showBackground = true, name = "News Preview")
@Composable
fun PreviewNewsView()
{
    CrimeShieldTheme {
        NewsScreen(navController = rememberNavController(), startDestination = "News")
    }
}

@Preview(showBackground = true, name = "Missing Preview")
@Composable
fun PreviewMissingView()
{
    CrimeShieldTheme {
        MissingScreen(navController = rememberNavController(), startDestination = "Missing")
    }
}

@Preview(showBackground = true, name = "Sex Offenders Preview")
@Composable
fun PreviewSexOffendersView()
{
    CrimeShieldTheme {
        SexOffendersScreen(navController = rememberNavController(), startDestination = "Sex Offenders")
    }
}
