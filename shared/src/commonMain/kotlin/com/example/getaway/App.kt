package com.example.getaway

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ExpenseRow {
    var item by mutableStateOf("")
    var price by mutableStateOf("")
    var isDivide by mutableStateOf(false) // false = "×", true = "÷"
}

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

        when (currentScreen) {
            "welcome" -> WelcomeScreen(onStartClick = { currentScreen = "input" })
            "input" -> InputScreen()
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
fun InputScreen() {
    var tripTitle by remember { mutableStateOf("") }
    var personCount by remember { mutableStateOf("") }
    val rows = remember { mutableStateListOf(ExpenseRow()) }

    val persons = personCount.toIntOrNull() ?: 0

    // Hitung biaya total per baris
    fun rowTotal(row: ExpenseRow): Double {
        val price = row.price.toDoubleOrNull() ?: 0.0
        return if (row.isDivide) price else price * persons
    }

    val grandTotal = rows.sumOf { rowTotal(it) }
    val perPerson = if (persons > 0) grandTotal / persons else 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Judul trip di atas-tengah
        OutlinedTextField(
            value = tripTitle,
            onValueChange = { tripTitle = it },
            label = { Text("Nama Trip (misal: Bali)") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Person, kecil, kiri
        OutlinedTextField(
            value = personCount,
            onValueChange = { personCount = it },
            label = { Text("Person") },
            modifier = Modifier.width(120.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Header tabel
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Text("Item", modifier = Modifier.weight(1.2f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("Harga", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("Mode", modifier = Modifier.weight(0.6f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("Hasil", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }

        // Baris-baris tabel
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(rows) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = row.item,
                        onValueChange = { row.item = it },
                        placeholder = { Text("Surfing", fontSize = 11.sp) },
                        modifier = Modifier.weight(1.2f).padding(end = 2.dp)
                    )
                    OutlinedTextField(
                        value = row.price,
                        onValueChange = { row.price = it },
                        placeholder = { Text("Rp", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f).padding(end = 2.dp)
                    )
                    // Tombol toggle mode × / ÷
                    OutlinedButton(
                        onClick = { row.isDivide = !row.isDivide },
                        contentPadding = PaddingValues(4.dp),
                        modifier = Modifier.weight(0.6f).padding(end = 2.dp)
                    ) {
                        Text(if (row.isDivide) "\u00F7" else "\u00D7")
                    }
                    Text(
                        "Rp${rowTotal(row).toInt()}",
                        modifier = Modifier.weight(1f),
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Tombol tambah baris
        OutlinedButton(
            onClick = { rows.add(ExpenseRow()) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Tambah baris")
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        // Total keseluruhan & per orang
        Text(
            "TOTAL: Rp${grandTotal.toInt()}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Per orang: Rp${perPerson.toInt()}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}