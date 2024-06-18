package com.example.weatherappcompose.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.weatherappcompose.data.api.APIInstance
import com.example.weatherappcompose.data.repos.WeatherRepoImpl
import com.example.weatherappcompose.presentation.WeatherViewModel
import com.example.weatherappcompose.ui.models.forecast.WeatherItem
import com.example.weatherappcompose.ui.theme.transparent
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun ForecastScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    weatherViewModel: WeatherViewModel = WeatherViewModel(WeatherRepoImpl(APIInstance.api))
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Back",
                    style = MaterialTheme.typography.headlineSmall
                )
            }, colors = TopAppBarDefaults.topAppBarColors(transparent), navigationIcon = {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }, modifier = Modifier
                        .clip(
                            RoundedCornerShape(20.dp)
                        )
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "",
                        modifier = Modifier.clipToBounds()
                    )
                }
            })
        },

        ) { paddingValues ->
        val data = weatherViewModel.forecastData.collectAsState().value?.list
        if (data != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(data) { it ->
                    ForecastItem(item = it, navController = navController)
                }
            }

        }
    }
}

@Composable
fun ForecastItem(
    modifier: Modifier = Modifier,
    item: WeatherItem,
    navController: NavController = rememberNavController()
) {

    val (textColor, backgroundColor) = generateContrastingColors()

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val bgColor by animateColorAsState(
        targetValue = if (!isVisible)
            backgroundColor else Color(0xFF202020), label = "", animationSpec = tween(600)
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            animationSpec = tween(1000)
        ) + scaleIn(
            animationSpec = tween(1000)
        ),
        exit = fadeOut(
            animationSpec = tween(1000)
        ) + scaleOut(
            animationSpec = tween(1000)
        )
    ) {


        Row(
            modifier = modifier
                .padding(2.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = item.dt_txt.substring(10),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = item.dt_txt.substring(0, 10),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Light
                )


            }
            Row {
                Icon(imageVector = Icons.Default.Star, contentDescription = "")
                Text(
                    text = item.weather[0].main,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = item.main.temp.minus(273.15).toString().substring(0, 4) + "°C",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }
    }
}

fun generateContrastingColors(): Pair<Color, Color> {
    val hue = Random.nextInt(0, 360)
    val saturation = 0.8f
    val value = 0.8f

    val backgroundColor = Color.hsv(hue.toFloat(), saturation, value)
    val textColor = Color.hsv(hue.toFloat(), 0.5f, 1.0f)

    return Pair(backgroundColor, textColor)
}