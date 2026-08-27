package com.example.getaway

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ExpenseRow {
    var item by mutableStateOf("")
    var price by mutableStateOf("")
}

class ActivityRow {
    var dateDay by mutableStateOf("")
    var time by mutableStateOf("")
    var place by mutableStateOf("")
    var activity by mutableStateOf("")
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
            "input" -> InputScreen(onNextClick = { currentScreen = "activity" })
            "activity" -> ActivityScreen()
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
fun InputScreen(onNextClick: () -> Unit) {
    var tripTitle by remember { mutableStateOf("") }
    var personCount by remember { mutableStateOf("") }
    val rows = remember { mutableStateListOf(ExpenseRow()) }

    val persons = personCount.toIntOrNull() ?: 0

    fun perPerson(row: ExpenseRow): Double {
        val price = row.price.toDoubleOrNull() ?: 0.0
        return if (persons > 0) price / persons else 0.0
    }

    val grandTotal = rows.sumOf { it.price.toDoubleOrNull() ?: 0.0 }
    val grandPerPerson = if (persons > 0) grandTotal / persons else 0.0
    val borderColor = Color(0xFFFF6F61)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = tripTitle,
            onValueChange = { tripTitle = it },
            label = { Text("Trip") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = personCount,
            onValueChange = { personCount = it },
            label = { Text("Person") },
            modifier = Modifier.width(120.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            TableCell("Item", weight = 1f, isHeader = true, borderColor = borderColor)
            TableCell("Harga", weight = 1f, isHeader = true, borderColor = borderColor)
            TableCell("Per orang", weight = 1f, isHeader = true, borderColor = borderColor)
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(rows) { row ->
                Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxHeight().border(1.dp, borderColor),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        BasicRowInput(row.item, { row.item = it }, "Villa")
                    }
                    Box(
                        modifier = Modifier.weight(1f).fillMaxHeight().border(1.dp, borderColor),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        BasicRowInput(row.price, { row.price = it }, "Rp")
                    }
                    Box(
                        modifier = Modifier.weight(1f).fillMaxHeight().border(1.dp, borderColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Rp${perPerson(row).toInt()}", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = { rows.add(ExpenseRow()) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Tambah baris")
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "TOTAL: Rp${grandTotal.toInt()}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Per orang: Rp${grandPerPerson.toInt()}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onNextClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Lanjut ke Rencana Kegiatan")
        }
    }
}

@Composable
fun ActivityScreen() {
    val rows = remember { mutableStateListOf(ActivityRow()) }
    val borderColor = Color(0xFFFF6F61)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            "Rencana Kegiatan",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            TableCell("Tanggal & Hari", weight = 1.3f, isHeader = true, borderColor = borderColor)
            TableCell("Jam", weight = 0.8f, isHeader = true, borderColor = borderColor)
            TableCell("Tempat", weight = 1f, isHeader = true, borderColor = borderColor)
            TableCell("Kegiatan", weight = 1.3f, isHeader = true, borderColor = borderColor)
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(rows) { row ->
                Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                    Box(modifier = Modifier.weight(1.3f).fillMaxHeight().border(1.dp, borderColor)) {
                        BasicRowInput(row.dateDay, { row.dateDay = it }, "")
                    }
                    Box(modifier = Modifier.weight(0.8f).fillMaxHeight().border(1.dp, borderColor)) {
                        BasicRowInput(row.time, { row.time = it }, "")
                    }
                    Box(modifier = Modifier.weight(1f).fillMaxHeight().border(1.dp, borderColor)) {
                        BasicRowInput(row.place, { row.place = it }, "")
                    }
                    Box(modifier = Modifier.weight(1.3f).fillMaxHeight().border(1.dp, borderColor)) {
                        BasicRowInput(row.activity, { row.activity = it }, "")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = { rows.add(ActivityRow()) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Tambah kegiatan")
        }
    }
}

@Composable
fun RowScope.TableCell(text: String, weight: Float, isHeader: Boolean, borderColor: Color) {
    Box(
        modifier = Modifier
            .weight(weight)
            .border(1.dp, borderColor)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            fontSize = 12.sp
        )
    }
}

@Composable
fun BasicRowInput(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        textStyle = TextStyle(fontSize = 12.sp),
        decorationBox = { inner ->
            if (value.isEmpty()) {
                Text(placeholder, fontSize = 12.sp, color = Color.Gray)
            }
            inner()
        }
    )
}