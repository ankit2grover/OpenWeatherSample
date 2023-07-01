package com.example.openweathersample.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.openweathersample.ui.viewmodels.WeatherViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.openweathersample.R
import com.example.openweathersample.ui.viewmodels.state.WeatherUiState
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherScreenContent(weatherViewModel: WeatherViewModel = viewModel()) {
    val locationGranted by weatherViewModel.locationPermissionGranted.collectAsState()
    val isUserSearching by weatherViewModel.isUserSearching.collectAsState()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        CitySearchBar(weatherViewModel = weatherViewModel)
        if (locationGranted || isUserSearching) {
            val scope = rememberCoroutineScope()
            val context = LocalContext.current
            val locationClient = remember {
                LocationServices.getFusedLocationProviderClient(context)
            }
            weatherViewModel.getWeatherByLocation(locationClient)
            WeatherContent(weatherViewModel = weatherViewModel)
        } else {
            checkLocationPermissions(weatherViewModel = weatherViewModel)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun checkLocationPermissions(weatherViewModel: WeatherViewModel) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    if (locationPermissionsState.allPermissionsGranted) {
        weatherViewModel.setLocationPermissionGranted(true)
    } else {
        val allPermissionsRevoked =
            locationPermissionsState.permissions.size ==
                    locationPermissionsState.revokedPermissions.size
        val buttonText = if (!allPermissionsRevoked) {
            stringResource(id = R.string.allow_precise_location)
        } else {
            stringResource(id = R.string.enable_location_permissions)
        }
        locationPermissionsState.shouldShowRationale
        if (!allPermissionsRevoked) {
            weatherViewModel.setLocationPermissionGranted(true)
        } else {
           ConstraintLayout(modifier = Modifier
               .fillMaxWidth()
               .fillMaxHeight()
               .padding(16.dp)) {
               val (locationPermissionTitle, permissionButton) = createRefs()
               Text(text = stringResource(id = R.string.location_permission_required),
                   modifier = Modifier
                       .fillMaxWidth()
                       .constrainAs(locationPermissionTitle) {
                           start.linkTo(parent.start)
                           top.linkTo(parent.top)
                           end.linkTo(parent.end)
                       }, textAlign = TextAlign.Center
               )
               Button(modifier = Modifier
                   .padding(top = 16.dp)
                   .constrainAs(permissionButton) {
                       start.linkTo(parent.start)
                       top.linkTo(locationPermissionTitle.bottom)
                       end.linkTo(parent.end)
                   }, onClick = {
                   locationPermissionsState.launchMultiplePermissionRequest()
               }) {
                   Text(buttonText)
               }
               createVerticalChain(locationPermissionTitle,
                   permissionButton, chainStyle = ChainStyle.Packed)
           }
        }
    }
}


@Composable
fun WeatherContent(weatherViewModel: WeatherViewModel) {
    val weatherUiState by weatherViewModel.weatherStateFlow.collectAsState()
    when(weatherUiState) {
        is WeatherUiState.Loading -> {
            ShowProgressIndicator()
        }
        is WeatherUiState.Success -> {
            val weatherInfo = (weatherUiState as WeatherUiState.Success)
                .weatherUIData.currentTemp.toString()
            ConstraintLayout(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp)) {
                val (weatherInfoText) = createRefs()
                Text(text = weatherInfo, modifier = Modifier.constrainAs(weatherInfoText) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                })
            }
        }
        else -> {}
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowProgressIndicator() {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(16.dp)) {
        val (progress) = createRefs()
        CircularProgressIndicator(modifier = Modifier.constrainAs(progress) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end)
        })
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySearchBar(weatherViewModel: WeatherViewModel) {
    var citySearchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var searchHistory by remember { mutableStateOf(mutableListOf<String>()) }
        Box(Modifier.fillMaxWidth()) {
            Box(Modifier
                .semantics { isContainer = true }
                .zIndex(1f)
                .fillMaxWidth()) {
                DockedSearchBar(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp),
                    query = citySearchText,
                    onQueryChange = { citySearchText = it },
                    onSearch = {
                        if (!searchHistory.contains(it)) {
                            searchHistory.add(it)
                        }
                        active = false
                        weatherViewModel.setIsUserSearchingState(true)
                        weatherViewModel.getWeatherBySearchCity(searchCity = it)
                    },
                    active = active,
                    onActiveChange = { active = it },
                    placeholder = { Text("Hinted search text") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = { Icon(Icons.Rounded.MoreVert, contentDescription = null) },
                ) {
                    searchHistory.forEach {
                        Text(modifier = Modifier.padding(16.dp), text = it)
                    }
                }
            }
        }
}

