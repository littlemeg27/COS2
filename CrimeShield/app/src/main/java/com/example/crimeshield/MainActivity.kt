
package com.example.crimeshield

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.crimeshield.ui.theme.CrimeShieldTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.launch
import android.Manifest
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.ui.unit.dp

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
    @OptIn(ExperimentalMaterial3Api::class)
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

                // Start of CameraX Code
                val scope = rememberCoroutineScope()
                val scaffoldState = rememberBottomSheetScaffoldState()
                val controller = remember {
                    LifecycleCameraController(applicationContext).apply {
                       setEnabledUseCases(
                           CameraController.IMAGE_CAPTURE or
                           CameraController.VIDEO_CAPTURE
                       )
                    }
                }
                val viewModel = viewModel<MainViewModel>()
                val bitmaps by viewModel.bitmaps.collectAsState()

                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    sheetContent = {
                        PhotoBottomSheetContent(
                            bitmaps = bitmaps,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    },
                    sheetPeekHeight = 0.dp,
                )
                {
                    padding ->
                    Box(modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                    ){
                        CameraPreview(controller = controller,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                        IconButton(onClick =
                        {
                            controller.cameraSelector =
                                if(controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                                {
                                    CameraSelector.DEFAULT_FRONT_CAMERA
                                } else {
                                    CameraSelector.DEFAULT_BACK_CAMERA
                                }
                        },
                            modifier = Modifier
                                .offset(16.dp, 16.dp)
                        ) {
                           Icon(
                               imageVector = Icons.Default.Cameraswitch,
                               contentDescription = "Switch Camera"
                           )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ){
                            IconButton(onClick = {
                                scope.launch {
                                    scaffoldState.bottomSheetState.expand()
                                }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Photo,
                                    contentDescription = "Open Gallery"
                                )
                            }
                            IconButton(
                                onClick = {
                                    takePhoto(
                                        controller = controller,
                                        onPhotoTaken = viewModel::onTakePhoto
                                    )
                                }
                            ) {
                                Icon(imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = "Take Photo"
                                )
                            }
                        }
                    }
                }  // End of CameraX Code

                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background)
                {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "home") //NavBar for the bottom of the screen
                    {
                        composable("home") { HomeScreen(Boolean, navigate =  , navController, startDestination = "home") }
                        composable("details") { MapScreen(Boolean, navigate =  , navController, startDestination = "details") }
                        composable("create") { CreateScreen(Boolean, navigate =  , navController, startDestination = "create") }
                        composable("settings") { SettingsScreen(Boolean, navigate = , navController, startDestination = "settings") }
                    }
                }
            }
        }
        /* NavigateBack: Boolean,
    navigate: () -> Unit,
    navController: NavController,
    startDestination: String,*/
    }

    private fun takePhoto( //Taking a photo
        controller: LifecycleCameraController,
        onPhotoTaken: (Bitmap) -> Unit
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(applicationContext),
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )
                    onPhotoTaken(rotatedBitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }
            }
        )
    }

    private fun hasRequiredPermissions(): Boolean { //Checking if the app has the required permissions
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object { //Permissions for the app
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        )
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    NavigateBack: Boolean.Companion,
    navigate: () -> Unit,
    navController: NavController,
    startDestination: String,
)
{
    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold( //Navigation Bar for the bottom of the screen
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
                        alwaysShowLabel = true,
                        icon =
                        {
                            if (NavigateBack)
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
        Image( //Crime Shield Icon
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
        Button( //Button to create a report
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
        Image( //Place holder for map
            painter = painterResource(id = R.drawable.map),
            contentDescription = "Map",
            modifier = Modifier
                .padding(top = 10.dp)
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MapScreen(
    NavigateBack: Boolean.Companion,
    navigate: () -> Unit,
    navController: NavController,
    startDestination: String,
)
{
    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold( //Navigation Bar for the bottom of the screen
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
                        alwaysShowLabel = true,
                        icon =
                        {
                            if (NavigateBack)
                            {
                                IconButton(onClick = navigate)
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
fun CreateScreen(
    NavigateBack: Boolean.Companion,
    navigate: () -> Unit,
    navController: NavController,
    startDestination: String,
    )
{
    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold( //Navigation Bar for the bottom of the screen
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
                        alwaysShowLabel = true,
                        icon =
                        {
                            if (NavigateBack)
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
        TextField( //Text Field for name
            value = textState1,
            onValueChange = { textState1 = it },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Person"
                )
            }
        )
        TextField( //Text Field for phone number
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
        Button( //Button to get location
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
        TextField( //Text Field for details
            value = textState3,
            onValueChange = { textState3 = it },
            maxLines = Int.MAX_VALUE,
            singleLine = false,
            modifier = Modifier
                .padding(20.dp)
        )

        CameraPreview() //Getting the Camera to show

        Button( //Button to submit report
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
fun SettingsScreen(
    NavigateBack: Boolean.Companion,
    navigate: () -> Unit,
    navController: NavController,
    startDestination: String,

    )
{
    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold( //Navigation Bar for the bottom of the screen
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
                        alwaysShowLabel = true,
                        icon =
                        {
                            if (NavigateBack)
                            {
                                IconButton(onClick = navigate)
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
fun SentReportsScreen(
    NavigateBack: Boolean,
    navigate: () -> Unit,
    navController: NavController,
    startDestination: String,
)
{
    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold( //Navigation Bar for the bottom of the screen
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
                        alwaysShowLabel = true,
                        icon =
                        {
                            if (NavigateBack)
                            {
                                IconButton(onClick = navigate)
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
fun NewsScreen(
    NavigateBack: Boolean,
    navigate: () -> Unit,
    navController: NavController,
    startDestination: String,
)
{
    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold( //Navigation Bar for the bottom of the screen
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
                        alwaysShowLabel = true,
                        icon =
                        {
                            if (NavigateBack)
                            {
                                IconButton(onClick = navigate)
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
fun MissingScreen(
    NavigateBack: Boolean,
    navigate: () -> Unit,
    navController: NavController,
    startDestination: String,
)
{
    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold( //Navigation Bar for the bottom of the screen
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
                        alwaysShowLabel = true,
                        icon =
                        {
                            if (NavigateBack)
                            {
                                IconButton(onClick = navigate)
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
fun SexOffendersScreen(
    NavigateBack: Boolean,
    navigate: () -> Unit,
    navController: NavController,
    startDestination: String,
)
{
    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold( //Navigation Bar for the bottom of the screen
        bottomBar = {
            NavigationBar { //Navigation Bar for the bottom of the screen
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
                        alwaysShowLabel = true,
                        icon =
                        {
                            if (NavigateBack)
                            {
                                IconButton(onClick = navigate)
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
@Preview(showBackground = true, name = "Home Preview") //Preview for Home Screen
@Composable
fun PreviewHomeView()
{
    CrimeShieldTheme {
        HomeScreen(navController = rememberNavController(), startDestination = "Home" )
    }
}

@Preview(showBackground = true, name = "Map Preview") //Preview for Map Screen
@Composable
fun PreviewMapView()
{
    CrimeShieldTheme {
        MapScreen(navController = rememberNavController(), startDestination = "Map")
    }
}

@Preview(showBackground = true, name = "Create Preview") //Preview for Create Screen
@Composable
fun PreviewCreateView()
{
    CrimeShieldTheme {
        CreateScreen(navController = rememberNavController(), startDestination = "Create")
    }
}

@Preview(showBackground = true, name = "Sent Reports Preview") //Preview for Sent Reports Screen
@Composable
fun PreviewSentReportsView()
{
    CrimeShieldTheme {
        SentReportsScreen(navController = rememberNavController(), startDestination = "Sent Reports")
    }
}

@Preview(showBackground = true, name = "Settings Preview") //Preview for Settings Screen
@Composable
fun PreviewSettingsView()
{
    CrimeShieldTheme {
        SettingsScreen(navController = rememberNavController(), startDestination = "Settings")
    }
}

@Preview(showBackground = true, name = "News Preview") //Preview for News Screen
@Composable
fun PreviewNewsView()
{
    CrimeShieldTheme {
        NewsScreen(navController = rememberNavController(), startDestination = "News")
    }
}

@Preview(showBackground = true, name = "Missing Preview") //Preview for Missing Screen
@Composable
fun PreviewMissingView()
{
    CrimeShieldTheme {
        MissingScreen(navController = rememberNavController(), startDestination = "Missing")
    }
}

@Preview(showBackground = true, name = "Sex Offenders Preview") //Preview for Sex Offenders Screen
@Composable
fun PreviewSexOffendersView()
{
    CrimeShieldTheme {
        SexOffendersScreen(navController = rememberNavController(), startDestination = "Sex Offenders")
    }
}
