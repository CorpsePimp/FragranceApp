package com.example.fragranceapp.presentation.screens.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun FilterDialog(
    initialBrandId: Int?,
    initialFamilyId: Int?,
    initialConcentrationId: Int?,
    initialMinRating: Float?,
    initialYear: Int?,
    onDismiss: () -> Unit,
    onApplyFilters: (
        brandId: Int?,
        familyId: Int?,
        concentrationId: Int?,
        minRating: Float?,
        year: Int?
    ) -> Unit
) {
    var brandId by remember { mutableStateOf(initialBrandId) }
    var familyId by remember { mutableStateOf(initialFamilyId) }
    var concentrationId by remember { mutableStateOf(initialConcentrationId) }
    var minRating by remember { mutableFloatStateOf(initialMinRating ?: 0f) }
    var selectedYear by remember { mutableIntStateOf(initialYear ?: 0) }
    
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (1900..currentYear).reversed().toList()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Фильтры") },
        text = {
            Column {
                Text("Минимальный рейтинг: ${minRating.toInt()}")
                Slider(
                    value = minRating,
                    onValueChange = { minRating = it },
                    valueRange = 0f..5f,
                    steps = 4,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Здесь должны быть DropDown меню для выбора брендов, семейств и концентраций
                // Это упрощенная версия для демонстрации
                
                Text("Этот диалог является упрощённым. В реальном приложении здесь будут выпадающие списки для выбора брендов, семейств ароматов и концентраций.")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onApplyFilters(
                        brandId,
                        familyId,
                        concentrationId,
                        if (minRating > 0f) minRating else null,
                        if (selectedYear > 0) selectedYear else null
                    )
                }
            ) {
                Text("Применить")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}