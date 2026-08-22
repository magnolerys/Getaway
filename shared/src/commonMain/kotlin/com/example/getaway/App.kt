package com.example.getaway

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class BudgetCategory(val name: String, val emoji: String, var amount: String = "")

@Composable
@Preview
fun App() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFFFF6F61),
            secondary = Color(0xFFE8A33D),
            background = Color(0xFFFFF8F3)
        )
    ) {
        var currentScreen by remember { mutableStateOf("welcome") }
        var destination by remember { mutableStateOf("") }
        var duration by remember { mutableStateOf("") }
        var totalBudget by remember { mutableStateOf("") }

        when (currentScreen) {
            "welcome" -> WelcomeScreen(onStartClick = { currentScreen = "input" })
            "input" -> InputScreen(
                destination = destination,
                onDestinationChange = { destination = it },
                duration = duration,
                onDurationChange = { duration = it },
                budget = totalBudget,
                onBudgetChange = { totalBudget = it },
                onContinueClick = { currentScreen = "dashboard" }
            )
            "dashboard" -> DashboardScreen(
                destination = destination,
                totalBudget = totalBudget.toDoubleOrNull() ?: 0.0
            )
        }
    }
}

@Composable
fun WelcomeScreen(onStartClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        DoodleBackground()
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Getaway", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onStartClick, modifier = Modifier.fillMaxWidth()) {
                Text("Start Planning")
            }
        }
    }
}

@Composable
fun InputScreen(
    destination: String,
    onDestinationChange: (String) -> Unit,
    duration: String,
    onDurationChange: (String) -> Unit,
    budget: String,
    onBudgetChange: (String) -> Unit,
    onContinueClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        DoodleBackground()
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = destination,
                onValueChange = onDestinationChange,
                label = { Text("Where are you going?") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = duration,
                onValueChange = onDurationChange,
                label = { Text("How long? (days)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = budget,
                onValueChange = onBudgetChange,
                label = { Text("Your budget? (Rp)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onContinueClick, modifier = Modifier.fillMaxWidth()) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun DashboardScreen(destination: String, totalBudget: Double) {
    val categories = remember {
        mutableStateListOf(
            BudgetCategory("Tiket Pesawat", "\u2708\uFE0F"),
            BudgetCategory("Akomodasi", "\uD83C\uDFE8"),
            BudgetCategory("Transport Lokal", "\uD83D\uDE95"),
            BudgetCategory("Makan", "\uD83C\uDF5C"),
            BudgetCategory("Aktivitas", "\uD83C\uDFAB"),
            BudgetCategory("Belanja", "\uD83D\uDECD\uFE0F"),
            BudgetCategory("Emergency", "\uD83C\uDD98")
        )
    }
    var totalSpent by remember { mutableStateOf(0.0) }
    var hasCalculated by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Text("Trip to $destination", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Total budget: Rp${totalBudget.toInt()}",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(categories) { category ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${category.emoji} ${category.name}", modifier = Modifier.weight(1f))
                        OutlinedTextField(
                            value = category.amount,
                            onValueChange = { category.amount = it },
                            label = { Text("Rp") },
                            modifier = Modifier.width(140.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                totalSpent = categories.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                hasCalculated = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Hitung")
        }

        if (hasCalculated) {
            Spacer(modifier = Modifier.height(12.dp))
            val remaining = totalBudget - totalSpent
            Text(
                "Total pengeluaran: Rp${totalSpent.toInt()}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                if (remaining >= 0) "Sisa budget: Rp${remaining.toInt()}" else "Over budget: Rp${-remaining.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                color = if (remaining >= 0) Color(0xFF2E7D32) else Color(0xFFD32F2F)
            )
        }
    }
}

@Composable
fun DoodleBackground() {
    val iconColor = Color(0xFFFF6F61).copy(alpha = 0.4f)
    Box(modifier = Modifier.fillMaxSize()) {
        Icon(
            imageVector = Icons.Filled.Flight, contentDescription = null, tint = iconColor,
            modifier = Modifier.size(60.dp).align(Alignment.TopStart).padding(top = 40.dp, start = 24.dp)
        )
        Icon(
            imageVector = Icons.Filled.Landscape, contentDescription = null, tint = iconColor,
            modifier = Modifier.size(70.dp).align(Alignment.TopEnd).padding(top = 60.dp, end = 20.dp)
        )
        Icon(
            imageVector = Icons.Filled.BeachAccess, contentDescription = null, tint = iconColor,
            modifier = Modifier.size(60.dp).align(Alignment.BottomStart).padding(bottom = 100.dp, start = 30.dp)
        )
        Icon(
            imageVector = Icons.Filled.Place, contentDescription = null, tint = iconColor,
            modifier = Modifier.size(50.dp).align(Alignment.BottomEnd).padding(bottom = 60.dp, end = 30.dp)
        )
    }
}