package com.asta.calculatorapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asta.calculatorapp.ui.theme.CherryContainerDeep
import com.asta.calculatorapp.ui.theme.DarkBgPrimary
import com.asta.calculatorapp.ui.theme.DarkBgSecondary
import com.asta.calculatorapp.ui.theme.ErrorBright
import com.asta.calculatorapp.ui.theme.NeonCherryPrimary
import com.asta.calculatorapp.ui.theme.NeonCherrySecondary
import com.asta.calculatorapp.ui.theme.SciFiGlassBorder
import com.asta.calculatorapp.ui.theme.SciFiGlassPanel
import com.asta.calculatorapp.ui.theme.TextBright
import com.asta.calculatorapp.ui.theme.TextDim
import com.asta.calculatorapp.ui.theme.TextMuted
import com.asta.calculatorapp.ui.viewmodel.CalculationHistoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorySheet(
    history: List<CalculationHistoryItem>,
    onRecallExpression: (CalculationHistoryItem) -> Unit,
    onRecallResult: (CalculationHistoryItem) -> Unit,
    onClearHistory: () -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = DarkBgSecondary,
        scrimColor = DarkBgPrimary.copy(alpha = 0.75f),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            HistoryPanelContent(
                history = history,
                onRecallExpression = { item ->
                    onRecallExpression(item)
                    onDismiss()
                },
                onRecallResult = { item ->
                    onRecallResult(item)
                    onDismiss()
                },
                onClearHistory = onClearHistory,
                onClose = onDismiss
            )
        }
    }
}

@Composable
fun EmbeddedHistoryPanel(
    history: List<CalculationHistoryItem>,
    onRecallExpression: (CalculationHistoryItem) -> Unit,
    onRecallResult: (CalculationHistoryItem) -> Unit,
    onClearHistory: () -> Unit,
    onClose: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(DarkBgSecondary)
            .border(
                width = 1.5.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        NeonCherryPrimary.copy(alpha = 0.4f),
                        SciFiGlassBorder,
                        NeonCherrySecondary.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        HistoryPanelContent(
            history = history,
            onRecallExpression = onRecallExpression,
            onRecallResult = onRecallResult,
            onClearHistory = onClearHistory,
            onClose = onClose
        )
    }
}

@Composable
fun HistoryPanelContent(
    history: List<CalculationHistoryItem>,
    onRecallExpression: (CalculationHistoryItem) -> Unit,
    onRecallResult: (CalculationHistoryItem) -> Unit,
    onClearHistory: () -> Unit,
    onClose: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val filteredHistory = remember(history, searchQuery) {
        if (searchQuery.isBlank()) {
            history
        } else {
            val query = searchQuery.trim().lowercase()
            history.filter { item ->
                item.expression.lowercase().contains(query) ||
                        item.result.lowercase().contains(query)
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Sheet/Panel Header: Title + Clear History + Close
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = NeonCherrySecondary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CALCULATION LOGS",
                    color = TextBright,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (history.isNotEmpty()) {
                    IconButton(
                        onClick = onClearHistory,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CherryContainerDeep)
                            .border(1.dp, ErrorBright.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear History",
                            tint = ErrorBright,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                if (onClose != null) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SciFiGlassPanel)
                            .border(1.dp, SciFiGlassBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Field
        if (history.isNotEmpty() || searchQuery.isNotEmpty()) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Search logs...",
                        color = TextMuted,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = NeonCherrySecondary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear Search",
                                tint = TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SciFiGlassPanel,
                    unfocusedContainerColor = SciFiGlassPanel,
                    focusedBorderColor = NeonCherrySecondary,
                    unfocusedBorderColor = SciFiGlassBorder,
                    focusedTextColor = TextBright,
                    unfocusedTextColor = TextBright,
                    cursorColor = NeonCherryPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        // List Content
        if (history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NO RECENT CALCULATIONS",
                    color = TextDim,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
            }
        } else if (filteredHistory.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NO LOGS MATCHING \"$searchQuery\"",
                    color = TextDim,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredHistory) { item ->
                    HistoryCard(
                        item = item,
                        onRecallExpression = { onRecallExpression(item) },
                        onRecallResult = { onRecallResult(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryCard(
    item: CalculationHistoryItem,
    onRecallExpression: () -> Unit,
    onRecallResult: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SciFiGlassPanel)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        NeonCherryPrimary.copy(alpha = 0.3f),
                        SciFiGlassBorder
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Expression
            Text(
                text = item.expression,
                color = TextMuted,
                fontSize = 15.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Result
            Text(
                text = "= ${item.result}",
                color = NeonCherrySecondary,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Recall Buttons: "Use Expression" & "Use Result"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RecallPillButton(
                    label = "Use Expression",
                    onClick = onRecallExpression
                )
                Spacer(modifier = Modifier.width(8.dp))
                RecallPillButton(
                    label = "Use Result",
                    onClick = onRecallResult,
                    isPrimary = true
                )
            }
        }
    }
}

@Composable
private fun RecallPillButton(
    label: String,
    onClick: () -> Unit,
    isPrimary: Boolean = false
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isPrimary) CherryContainerDeep else SciFiGlassPanel)
            .border(
                width = 1.dp,
                color = if (isPrimary) NeonCherryPrimary.copy(alpha = 0.8f) else SciFiGlassBorder,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isPrimary) TextBright else NeonCherrySecondary,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}
