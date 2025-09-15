package com.kamildevelopments.exchangeappkotlin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class CalculatorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CalculatorScreen(
                onHomeClick = {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun CalculatorScreen(onHomeClick: () -> Unit) {
    var selectedItem by remember { mutableStateOf("Calculator") }
    var amount by remember { mutableStateOf("") }
    var currency1 by remember { mutableStateOf("USD") }
    var currency2 by remember { mutableStateOf("EUR") }
    var result by remember { mutableStateOf("0.0") }

    val currencies = listOf("USD", "EUR", "PLN")

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedItem == "Home",
                    onClick = {
                        selectedItem = "Home"
                        onHomeClick()
                    },
                    label = { Text("Home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = selectedItem == "Calculator",
                    onClick = { selectedItem = "Calculator" },
                    label = { Text("Calculator") },
                    icon = { Icon(Icons.Default.Calculate, contentDescription = null) }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Choose first currency:")
            CurrencyDropdown(currencies, currency1) { currency1 = it }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Choose second currency:")
            CurrencyDropdown(currencies, currency2) { currency2 = it }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Enter amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("=")

            Spacer(modifier = Modifier.height(16.dp))

            Text("Result: $result")
        }
    }
}

@Composable
fun CurrencyDropdown(
    currencies: List<String>,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
                        onCurrencySelected(currency)
                        expanded = false
                    }
                )
            }
        }
    }
}
