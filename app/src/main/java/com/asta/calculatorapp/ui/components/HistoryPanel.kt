package com.asta.calculatorapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.asta.calculatorapp.ui.viewmodel.CalculationHistoryItem
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorySheet(
    history: List<CalculationHistoryItem>,
    onRecallExpression: (CalculationHistoryItem) -> Unit,
    onRecallResult: (CalculationHistoryItem) -> Unit,
    onClearHistory: () -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)) {
        HistoryPanelContent(history,
            { onRecallExpression(it); onDismiss() },
            { onRecallResult(it); onDismiss() }, onClearHistory, onDismiss,
            Modifier.fillMaxHeight(0.92f).imePadding().padding(horizontal = 20.dp))
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
    Surface(modifier, shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surface) {
        HistoryPanelContent(history, onRecallExpression, onRecallResult, onClearHistory,
            onClose, Modifier.padding(16.dp))
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
    var query by rememberSaveable { mutableStateOf("") }
    var menuOpen by remember { mutableStateOf(false) }
    var confirmClear by rememberSaveable { mutableStateOf(false) }
    val focus = LocalFocusManager.current
    val listState = rememberLazyListState()
    val filtered = remember(history, query) {
        val term = query.trim()
        history.filter { term.isEmpty() || it.expression.contains(term, ignoreCase = true) || it.result.contains(term, ignoreCase = true) }
    }
    val groups = remember(filtered) { filtered.groupBy { dateLabel(it.timestamp) } }
    val colors = MaterialTheme.colorScheme

    if (confirmClear) AlertDialog(onDismissRequest = { confirmClear = false },
        icon = { Icon(Icons.Outlined.DeleteOutline, null) },
        title = { Text("Clear history?") },
        text = { Text("This removes all calculations from your history. Your current calculation stays on the calculator.") },
        confirmButton = { TextButton(onClick = {
            onClearHistory(); query = ""; confirmClear = false
        }) { Text("Clear history") } },
        dismissButton = { TextButton(onClick = { confirmClear = false }) { Text("Keep history") } })

    Column(modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("History", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                Text(if (history.isEmpty()) "A place for your recent calculations"
                    else "${history.size} ${if (history.size == 1) "calculation" else "calculations"} · tap to reuse a result",
                    style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
            }
            if (history.isNotEmpty()) Box {
                IconButton(onClick = { menuOpen = true }) { Icon(Icons.Outlined.MoreVert, "History options") }
                DropdownMenu(menuOpen, { menuOpen = false }) {
                    DropdownMenuItem(text = { Text("Clear history") },
                        leadingIcon = { Icon(Icons.Outlined.DeleteOutline, null) },
                        onClick = { menuOpen = false; confirmClear = true })
                }
            }
            if (onClose != null) IconButton(onClick = onClose) { Icon(Icons.Outlined.Close, "Close history") }
        }
        if (history.isNotEmpty()) OutlinedTextField(value = query, onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            placeholder = { Text("Search calculations") },
            leadingIcon = { Icon(Icons.Outlined.Search, null) },
            trailingIcon = { if (query.isNotEmpty()) IconButton(onClick = { query = "" }) {
                Icon(Icons.Outlined.Close, "Clear search")
            } }, shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focus.clearFocus() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.surface,
                unfocusedContainerColor = colors.surface,
                unfocusedBorderColor = colors.outlineVariant))

        LazyColumn(Modifier.weight(1f).fillMaxWidth(), state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 24.dp)) {
            if (filtered.isEmpty()) item {
                Column(Modifier.fillParentMaxHeight().fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    Surface(shape = RoundedCornerShape(24.dp), color = colors.primaryContainer) {
                        Icon(if (history.isEmpty()) Icons.Outlined.History else Icons.Outlined.Search,
                            null, Modifier.padding(20.dp).size(32.dp), tint = colors.onPrimaryContainer)
                    }
                    Spacer(Modifier.height(20.dp))
                    Text(if (history.isEmpty()) "No calculations yet" else "No matches",
                        style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(if (history.isEmpty()) "Your results appear here after you press equals."
                        else "Try a different number or expression.",
                        textAlign = TextAlign.Center, color = colors.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium)
                    if (history.isNotEmpty() || onClose != null) TextButton(onClick = { if (history.isEmpty()) onClose?.invoke() else query = "" }) {
                        Text(if (history.isEmpty()) "Start calculating" else "Clear search")
                    }
                }
            }
            groups.forEach { (day, entries) ->
                item(key = "day:$day") {
                    Text(day, Modifier.padding(top = 8.dp, bottom = 4.dp),
                        style = MaterialTheme.typography.labelLarge, color = colors.onSurfaceVariant)
                }
                items(entries, key = { it.id }) { entry ->
                    HistoryEntry(entry, { onRecallExpression(entry) }, { onRecallResult(entry) })
                }
            }
        }
    }
}

@Composable
private fun HistoryEntry(item: CalculationHistoryItem, onEdit: () -> Unit, onUseResult: () -> Unit) {
    var options by remember { mutableStateOf(false) }
    val time = remember(item.timestamp) { DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(item.timestamp)) }
    val colors = MaterialTheme.colorScheme
    Surface(shape = RoundedCornerShape(18.dp), color = colors.primaryContainer.copy(alpha = 0.25f)) {
        Row(Modifier.fillMaxWidth().clickable(role = Role.Button,
            onClickLabel = "Use result ${item.result}", onClick = onUseResult).padding(start = 16.dp, top = 12.dp, bottom = 16.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(time, style = MaterialTheme.typography.labelSmall, color = colors.onSurfaceVariant)
                Text(item.expression, style = MaterialTheme.typography.bodyLarge, color = colors.onSurface)
                Text("= ${item.result}", style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold, color = colors.primary)
            }
            Box {
                IconButton(onClick = { options = true }) { Icon(Icons.Outlined.MoreVert, "Options for ${item.expression}") }
                DropdownMenu(options, { options = false }) {
                    DropdownMenuItem(text = { Text("Use result") },
                        leadingIcon = { Icon(Icons.AutoMirrored.Outlined.ArrowForward, null) },
                        onClick = { options = false; onUseResult() })
                    DropdownMenuItem(text = { Text("Edit calculation") },
                        leadingIcon = { Icon(Icons.Outlined.Edit, null) },
                        onClick = { options = false; onEdit() })
                }
            }
        }
    }
}

private fun dateLabel(timestamp: Long): String {
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
    }
    val yesterday = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -1) }
    return when {
        timestamp >= today.timeInMillis -> "Today"
        timestamp >= yesterday.timeInMillis -> "Yesterday"
        else -> DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(timestamp))
    }
}
