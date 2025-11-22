package com.example.driftui
//This file is LayoutPrimitives.kt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// ---------------------------------------------------------------------------------------------
// ALIGNMENTS
// ---------------------------------------------------------------------------------------------

val top: Alignment = Alignment.TopCenter
val center: Alignment = Alignment.Center
val bottom: Alignment = Alignment.BottomCenter
val leading: Alignment = Alignment.CenterStart
val trailing: Alignment = Alignment.CenterEnd
val topLeading: Alignment = Alignment.TopStart
val topTrailing: Alignment = Alignment.TopEnd
val bottomLeading: Alignment = Alignment.BottomStart
val bottomTrailing: Alignment = Alignment.BottomEnd


// ---------------------------------------------------------------------------------------------
// VSTACK / HSTACK / ZSTACK / DriftView
// ---------------------------------------------------------------------------------------------

@Composable
fun VStack(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    spacing: Int = 8,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = alignment,
        verticalArrangement = Arrangement.spacedBy(spacing.dp),
        content = content
    )
}

@Composable
fun HStack(
    modifier: Modifier = Modifier,
    alignment: Alignment.Vertical = Alignment.CenterVertically,
    spacing: Int = 8,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = alignment,
        horizontalArrangement = Arrangement.spacedBy(spacing.dp),
        content = content
    )
}

@Composable
fun ZStack(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier, contentAlignment = contentAlignment, content = content)
}

@Composable
fun DriftView(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(modifier = modifier.fillMaxSize().background(driftColors.background),
        contentAlignment = Alignment.Center, content = content)
}


// ---------------------------------------------------------------------------------------------
// SPACER
// ---------------------------------------------------------------------------------------------

@Composable
fun ColumnScope.Spacer() = androidx.compose.foundation.layout.Spacer(Modifier.weight(1f))

@Composable
fun RowScope.Spacer() = androidx.compose.foundation.layout.Spacer(Modifier.weight(1f))

@Composable
fun Spacer(size: Int) = androidx.compose.foundation.layout.Spacer(Modifier.size(size.dp))