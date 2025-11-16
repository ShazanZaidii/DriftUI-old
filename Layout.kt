package com.example.driftui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// For ColumnScope / RowScope / BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.BoxScope

//MARK: - For HStack
import androidx.compose.ui.unit.dp

// For padding
import androidx.compose.foundation.layout.padding as foundationPadding
import androidx.compose.foundation.layout.PaddingValues




// ---------------------------------------------------------
// MARK: - VStack
// ---------------------------------------------------------

@Composable
fun VStack(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    spacing: Int = 8,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),   // only width, not height
        horizontalAlignment = alignment,
        verticalArrangement = Arrangement.spacedBy(spacing.dp),
        content = content
    )
}




// ---------------------------------------------------------
// MARK: - HStack
// ---------------------------------------------------------

@Composable
fun HStack(
    modifier: Modifier = Modifier,
    alignment: Alignment.Vertical = Alignment.CenterVertically,
    spacing: Int = 8,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier,  // <-- FIX: do NOT fillMaxWidth by default
        verticalAlignment = alignment,
        horizontalArrangement = Arrangement.spacedBy(spacing.dp),
        content = content
    )
}




// ---------------------------------------------------------
// MARK: - ZStack
// ---------------------------------------------------------

@Composable
fun ZStack(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier,   // DO NOT force fillMaxSize
        contentAlignment = contentAlignment,
        content = content
    )
}

//MAINNNNNNNNNN
@Composable
fun DriftView(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = content
    )
}
/////////////

// ---------------------------------------------------------
// MARK: - SwiftUI-style Padding
// ---------------------------------------------------------

fun Modifier.padding(all: Int): Modifier =
    this.then(
        foundationPadding(all.dp)
    )

fun Modifier.padding(horizontal: Int = 0, vertical: Int = 0): Modifier =
    this.then(
        foundationPadding(
            PaddingValues(
                start = horizontal.dp,
                end = horizontal.dp,
                top = vertical.dp,
                bottom = vertical.dp
            )
        )
    )

fun Modifier.padding(
    top: Int = 0,
    bottom: Int = 0,
    leading: Int = 0,
    trailing: Int = 0
): Modifier =
    this.then(
        foundationPadding(
            PaddingValues(
                start = leading.dp,
                end = trailing.dp,
                top = top.dp,
                bottom = bottom.dp
            )
        )
    )
