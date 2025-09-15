package com.kamildevelopments.exchangeappkotlin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Calculate
import androidx.lifecycle.lifecycleScope
import com.kamildevelopments.exchangeappkotlin.api.ApiClient
import com.kamildevelopments.exchangeappkotlin.api.ApiService
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ExchangeAppMain(
                onCalculatorClick = {
                    val intent = Intent(this, CalculatorActivity::class.java)
                    startActivity(intent)
                }
            )
        }
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        lifecycleScope.launch {
            try {
                val response = apiService.getLatestRates(
                    apiKey = "fca_live_MQrCby5bpfAbqZLB2L34RjESQeZqipxA4kC3fAIM",
                    currencies = "EUR,USD,CAD"
                )
                println("Rates: ${response.data}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

@Composable
fun ExchangeAppMain(onCalculatorClick: () -> Unit) {
    var selectedItem by remember { mutableStateOf("Home") }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedItem == "Home",
                    onClick = { selectedItem = "Home" },
                    label = { Text("Home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = selectedItem == "Calculator",
                    onClick = {
                        selectedItem = "Calculator"
                        onCalculatorClick()
                    },
                    label = { Text("Calculator") },
                    icon = { Icon(Icons.Default.Calculate, contentDescription = null) }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            if (selectedItem == "Home") {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Choose a currency:")
        Spacer(modifier = Modifier.height(8.dp))

        var expanded by remember { mutableStateOf(false) }
        var selectedCurrency by remember { mutableStateOf("USD") }
        val currencies = listOf("USD", "EUR", "PLN")

        Box {
            Button(onClick = { expanded = true }) {
                Text(selectedCurrency)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                currencies.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency) },
                        onClick = {
                            selectedCurrency = currency
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("=")

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(10) { index ->
                BasicText("Currency $index: ${(1..100).random()} rate")
            }
        }
    }
}
