package com.example.weatherappcompose.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.tasks.await

@Composable
fun GetPermissions(modifier: Modifier = Modifier) {
    val permissionResultLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {


        }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
//                            permissionResultLauncher.launch(android.Manifest.permission.LOCATION_HARDWARE)
//
            permissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//                            permissionResultLauncher.launch(android.Manifest.permission.CAMERA)
        }) {
            Text("Allow Location")
        }

    }

}

suspend fun getLocations(
    fusedLocationClient: FusedLocationProviderClient,
    context: Context
): String {
    var location = "aa"

    // Check if permissions are granted
    val fineLocationPermission = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val coarseLocationPermission = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (!fineLocationPermission && !coarseLocationPermission) {
        return "error: Location permissions not granted"
    }

    try {
        val lastLocation = fusedLocationClient.lastLocation.await()
        if (lastLocation != null) {
            location = lastLocation.time.toString()
        } else {
            location = "Location not available"
        }
    } catch (e: Exception) {
        location = "Error getting location: ${e.message}"
    }

    return location
}









