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

// For background & color
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color

// For Shapes
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape

//ClipShape
import androidx.compose.ui.draw.clip

//Frame:
import androidx.compose.ui.unit.Dp

// Font

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text as MaterialText
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

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

// ---------------------------------------------------------
// MARK: - Spacer (SwiftUI-style)
// ---------------------------------------------------------

@Composable
fun ColumnScope.Spacer() {
    androidx.compose.foundation.layout.Spacer(
        modifier = Modifier.weight(1f)
    )
}

@Composable
fun RowScope.Spacer() {
    androidx.compose.foundation.layout.Spacer(
        modifier = Modifier.weight(1f)
    )
}

@Composable
fun Spacer(size: Int) {
    // Fixed size Spacer
    androidx.compose.foundation.layout.Spacer(
        modifier = Modifier.size(size.dp)
    )
}

// ---------------------------------------------------------
// MARK: - SwiftUI Shape DSL
// ---------------------------------------------------------



fun Circle(): Shape = CircleShape

fun Capsule(): Shape = RoundedCornerShape(percent = 50)

fun RoundedRectangle(cornerRadius: Int): Shape =
    RoundedCornerShape(cornerRadius.dp)

//ClipShape:

fun Modifier.clipShape(shape: Shape): Modifier =
    this.then(clip(shape))
//Corner-Radius:
fun Modifier.cornerRadius(radius: Int): Modifier =
    this.clipShape(RoundedRectangle(radius))


// ---------------------------------------------------------
// MARK: - Background Color (SwiftUI-style)
// --------------------------------------------------------- 
fun Modifier.backgroundColor(color: Color): Modifier =
    this.then(background(color))

// ---------------------------------------------------------
// MARK: - Frame (SwiftUI-style)
// ---------------------------------------------------------



fun Modifier.frame(
    width: Int? = null,
    height: Int? = null,
    minWidth: Int = 0,
    maxWidth: Int? = null,
    minHeight: Int = 0,
    maxHeight: Int? = null
): Modifier {

    var m = this

    // Width (fixed or ranged)
    if (width != null) {
        m = m.then(Modifier.width(width.dp))
    } else {
        m = m.then(
            Modifier.widthIn(
                min = if (minWidth > 0) minWidth.dp else Dp.Unspecified,
                max = if (maxWidth != null) maxWidth.dp else Dp.Unspecified
            )
        )
    }

    // Height (fixed or ranged)
    if (height != null) {
        m = m.then(Modifier.height(height.dp))
    } else {
        m = m.then(
            Modifier.heightIn(
                min = if (minHeight > 0) minHeight.dp else Dp.Unspecified,
                max = if (maxHeight != null) maxHeight.dp else Dp.Unspecified
            )
        )
    }

    return m
}

// ---------------------------------------------------------
// MARK: - Font (SwiftUI-style)
// ---------------------------------------------------------

// Font weights (DriftUI style, no dot syntax)
val bold = FontWeight.Bold
val semibold = FontWeight.SemiBold
val medium = FontWeight.Medium
val regular = FontWeight.Normal
val light = FontWeight.Light
val thin = FontWeight.Thin
val ultralight = FontWeight.ExtraLight
val black = FontWeight.Black

// DriftUI System font model and helper (usage: system(size = 18, weight = bold))
data class SystemFont(
    val size: Int,
    val weight: FontWeight = regular
)

fun system(size: Int, weight: FontWeight = regular): SystemFont =
    SystemFont(size, weight)


// store font metadata in Modifier chain
private data class FontModifier(val font: SystemFont) : Modifier.Element

fun Modifier.font(font: SystemFont): Modifier =
    this.then(FontModifier(font))




// DriftUI Text wrapper: it reads FontModifier from the provided modifier and applies style.
@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier
) {
    // Walk modifier chain to find FontModifier (the last one wins if multiple)
    val fontFromModifier: SystemFont? = remember(modifier) {
        var found: SystemFont? = null
        modifier.foldIn(Unit) { _, element ->
            if (element is FontModifier) {
                found = element.font
            }
            Unit
        }
        found
    }

    val style = if (fontFromModifier != null) {
        TextStyle(
            fontSize = fontFromModifier.size.sp,
            fontWeight = fontFromModifier.weight
        )
    } else {
        TextStyle.Default
    }

    // Delegate to Material Text with the computed style
    MaterialText(
        text = text,
        modifier = modifier,
        style = style
    )
}
