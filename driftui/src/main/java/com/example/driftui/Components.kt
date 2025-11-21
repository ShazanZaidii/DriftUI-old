package com.example.driftui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image as ComposeImage
import androidx.compose.foundation.background as foundationBackground
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider as MaterialDivider
import androidx.compose.material3.Slider as MaterialSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text as MaterialText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.composed
// --- CUSTOM IMPORTS ---
import com.example.driftui.driftColors
import com.example.driftui.center
import com.example.driftui.State
import com.example.driftui.Binding
import com.example.driftui.SystemFont
import com.example.driftui.FontModifier
import com.example.driftui.ForegroundColorModifier
import com.example.driftui.ToggleStyleModifier
import com.example.driftui.SliderStyleModifier
import com.example.driftui.RoundedRectangle
import com.example.driftui.Capsule
import com.example.driftui.Circle
import com.example.driftui.onTapGesture


// ---------------------------------------------------------------------------------------------
// SHAPES
// ---------------------------------------------------------------------------------------------

@Composable
private fun Modifier.getForegroundColor(): Color? {
    var chosenColor: Color? = null
    this.foldIn(Unit) { _, element ->
        if (element is ForegroundColorModifier) {
            chosenColor = element.color
        }
        Unit
    }
    return chosenColor
}

@Composable
fun Rectangle(
    width: Int,
    height: Int,
    modifier: Modifier = Modifier
) {
    val fgColor = modifier.getForegroundColor() ?: driftColors.text

    Box(
        modifier = modifier
            .size(width.dp, height.dp)
            .clip(androidx.compose.ui.graphics.RectangleShape)
            .foundationBackground(fgColor)
    )
}
@Composable
fun Circle(
    radius: Int,
    modifier: Modifier = Modifier
) {
    val fgColor = modifier.getForegroundColor() ?: driftColors.text

    Box(
        modifier = modifier
            .size(radius.dp * 2)
            .clip(CircleShape)
            .foundationBackground(fgColor)
    )
}

@Composable
fun Capsule(
    width: Int,
    height: Int,
    modifier: Modifier = Modifier
) {
    val fgColor = modifier.getForegroundColor() ?: driftColors.text

    Box(
        modifier = modifier
            .size(width.dp, height.dp)
            .clip(RoundedCornerShape(percent = 50))
            .foundationBackground(fgColor)
    )
}

@Composable
fun RoundedRectangle(
    width: Int,
    height: Int,
    cornerRadius: Int,
    modifier: Modifier = Modifier
) {
    val fgColor = modifier.getForegroundColor() ?: driftColors.text

    Box(
        modifier = modifier
            .size(width.dp, height.dp)
            .clip(RoundedCornerShape(cornerRadius.dp))
            .foundationBackground(fgColor)
    )
}
fun Circle(): Shape = CircleShape
fun Capsule(): Shape = RoundedCornerShape(percent = 50)
fun RoundedRectangle(radius: Int): Shape = RoundedCornerShape(radius.dp)


// ---------------------------------------------------------------------------------------------
// TEXT & IMAGE
// ---------------------------------------------------------------------------------------------

@Composable
fun Text(text: String, modifier: Modifier = Modifier) {
    var chosenFont: SystemFont? = null
    var chosenColor = Color.Unspecified

    modifier.foldIn(Unit) { _, element ->
        when (element) {
            is FontModifier -> chosenFont = element.font
            is ForegroundColorModifier -> chosenColor = element.color
        }
        Unit
    }

    val style = TextStyle(
        fontSize = chosenFont?.size?.sp ?: TextStyle.Default.fontSize,
        fontWeight = chosenFont?.weight ?: TextStyle.Default.fontWeight,
        color = chosenColor.takeUnless { it == Color.Unspecified } ?: driftColors.text
    )

    MaterialText(
        text = text,
        modifier = modifier,
        style = style,
    )
}

@Composable
fun Image(
    name: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    val ctx = LocalContext.current
    val id = ctx.resources.getIdentifier(name, "drawable", ctx.packageName)

    ComposeImage(
        painter = painterResource(id),
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
    )
}

// ---------------------------------------------------------------------------------------------
// INPUTS (TextField, SecureField)
// ---------------------------------------------------------------------------------------------

@Composable
fun TextField(
    placeholder: String,
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = modifier, // use external modifier entirely
        textStyle = TextStyle.Default.copy(fontSize = 16.sp),
        decorationBox = { inner ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 8, vertical = 8) // inner padding only
            ) {
                if (text.isEmpty()) {
                    MaterialText(
                        text = placeholder,
                        style = TextStyle.Default.copy(
                            fontSize = 16.sp,
//                            color = driftColors.text.copy(alpha = 0.6f)
                            color = Color.Gray
                        )
                    )
                }
                inner()
            }
        }
    )
}

@Composable
fun TextField(
    placeholder: String,
    value: State<String>, // RESTORED
    modifier: Modifier = Modifier
) {
    val b = value.binding()
    TextField(placeholder, b.value, b.set, modifier)
}

@Composable
fun SecureField(
    placeholder: String,
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = modifier, // use external modifier entirely
        textStyle = TextStyle.Default.copy(fontSize = 16.sp),
        visualTransformation = PasswordVisualTransformation(),
        decorationBox = { inner ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 8, vertical = 8) // inner padding only
            ) {
                if (text.isEmpty()) {
                    MaterialText(
                        text = placeholder,
                        style = TextStyle.Default.copy(
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    )
                }
                inner()
            }
        }
    )
}

@Composable
fun SecureField(
    placeholder: String,
    value: State<String>, // RESTORED
    modifier: Modifier = Modifier
) {
    val b = value.binding()
    SecureField(placeholder, b.value, b.set, modifier)
}


// ---------------------------------------------------------------------------------------------
// BUTTON, DIVIDER, LIST
// ---------------------------------------------------------------------------------------------

@Composable
fun Button(
    action: () -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.clickable(
            onClick = action,
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ),
        contentAlignment = Alignment.Center,
        content = label
    )
}

@Composable
fun Divider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
    thickness: Int = 1
) {
    MaterialDivider(
        modifier = modifier,
        color = color,
        thickness = thickness.dp
    )
}

@Composable
fun List(
    alignment: Alignment = center,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = alignment) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8)
                .clip(RoundedRectangle(10))
                .background(driftColors.fieldBackground.copy(alpha = 0.2f)),
            content = content
        )
    }
}

@Composable
fun <T> List(
    items: List<T>,
    alignment: Alignment = center,
    modifier: Modifier = Modifier,
    rowContent: @Composable (T) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 8),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(items) { index, item ->
            rowContent(item)
            if (index < items.size - 1) Divider()
        }
    }
}


// ---------------------------------------------------------------------------------------------
// TOGGLE, SLIDER, SCROLLVIEW
// ---------------------------------------------------------------------------------------------

@Composable
fun Toggle(
    isOn: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null
) {
    // Read styling from modifier
    var styleOn: Color? = null
    var styleOff: Color? = null
    var styleThumb: Color? = null

    modifier.foldIn(Unit) { _, el ->
        if (el is ToggleStyleModifier) {
            if (el.onColor != null) styleOn = el.onColor
            if (el.offColor != null) styleOff = el.offColor
            if (el.thumbColor != null) styleThumb = el.thumbColor
        }
        Unit
    }

    // 1. Thumb overshoot animation (bounce on state change)
    val thumbOffset by animateDpAsState(
        targetValue = if (isOn) 22.dp else 2.dp,
        animationSpec = spring(
            dampingRatio = 0.55f,
            stiffness = 300f
        ),
        label = "thumb_offset"
    )

    val thumbScale by animateFloatAsState(
        targetValue = if (isOn) 1.06f else 1.0f,
        animationSpec = tween(120),
        label = "thumb_scale"
    )

    val trackColor by animateColorAsState(
        targetValue = if (isOn)
            (styleOn ?: driftColors.accent)
        else
            (styleOff ?: driftColors.fieldBackground),
        animationSpec = tween(200),
        label = "track_color"
    )

    val trackShadow = if (isOn) 8.dp else 1.dp
    val trackShadowColor = if (isOn)
        driftColors.accent.copy(alpha = 0.35f)
    else
        Color.Black.copy(alpha = 0.12f)

    val thumbColor by animateColorAsState(
        targetValue = styleThumb
            ?: if (isOn) Color.White
            else Color.White.copy(alpha = 0.85f),
        animationSpec = tween(180),
        label = "thumb_color"
    )

    val thumbShadow = if (isOn) 10.dp else 0.dp

    var pressed by remember { mutableStateOf(false) }
    val pressScale by animateFloatAsState(
        targetValue = if (pressed) 0.92f else 1.0f,
        animationSpec = tween(80),
        label = "press_scale"
    )

    // --- UI -----------------------------------------------------------

    Row(
        modifier = modifier
            .wrapContentHeight()
            .then(Modifier.onTapGesture {
                pressed = true
                onToggle(!isOn)
                // slight delay to release scale animation
                pressed = false
            }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Label
        if (label != null) label()

        // Track
        Box(
            modifier = Modifier
                .size(width = 52.dp, height = 30.dp)
                .graphicsLayer {
                    scaleX = pressScale
                    scaleY = pressScale
                }
                .clip(Capsule())
                .background(trackColor)
                .shadow(trackShadow, Capsule(), false, trackShadowColor),
            contentAlignment = Alignment.CenterStart
        ) {

            // Thumb
            Box(
                modifier = Modifier
                    .offset(x = thumbOffset)
                    .size(26.dp)
                    .graphicsLayer {
                        scaleX = thumbScale
                        scaleY = thumbScale
                        shadowElevation = thumbShadow.toPx()
                    }
                    .clip(Circle())
                    .background(thumbColor)
            )
        }
    }
}

@Composable
fun Toggle(
    value: State<Boolean>, // RESTORED
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit // Trailing lambda for custom content
) {
    val b = value.binding()
    Toggle(
        isOn = b.value,
        onToggle = b.set,
        modifier = modifier,
        label = label
    )
}

@Composable
fun Toggle(
    label: String, // String label for convenience
    value: State<Boolean>, // RESTORED
    modifier: Modifier = Modifier
) {
    val b = value.binding()
    Toggle(isOn = b.value, onToggle = b.set, modifier = modifier) {
        // Renders the string label using the Text composable
        Text(label)
    }
}

@Composable
fun Slider(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange = 0..100,
    step: Int = 0,
    modifier: Modifier = Modifier
) {
    // --- READ STYLING FROM MODIFIER CHAIN ---
    var styleActiveTrack: Color? = null
    var styleInactiveTrack: Color? = null
    var styleThumbColor: Color? = null
    var styleStepColor: Color? = null
    var styleStepOpacity: Float? = null

    modifier.foldIn(Unit) { _, el ->
        if (el is SliderStyleModifier) {
            styleActiveTrack = el.activeTrackColor ?: styleActiveTrack
            styleInactiveTrack = el.inactiveTrackColor ?: styleInactiveTrack
            styleThumbColor = el.thumbColor ?: styleThumbColor
            styleStepColor = el.stepColor ?: styleStepColor
            styleStepOpacity = el.stepOpacity ?: styleStepOpacity
        }
        Unit
    }

    // --- DETERMINE FINAL COLORS AND VALUES ---
    val finalActiveTrackColor = styleActiveTrack ?: driftColors.accent
    val finalInactiveTrackColor = styleInactiveTrack ?: driftColors.fieldBackground
    val finalThumbColor = styleThumbColor ?: driftColors.accent
    val finalStepColor = styleStepColor ?: styleActiveTrack ?: driftColors.accent // Default step color to active track

    // Apply Opacity to the step color if set
    val stepsColorWithOpacity = finalStepColor.copy(alpha = styleStepOpacity ?: 0.54f) // Default step opacity is usually 54%

    // --- CONVERSION LOGIC ---
    val floatValue = value.toFloat()
    val floatRange = range.first.toFloat()..range.last.toFloat()
    val stepsCount = if (step > 0) ((range.last - range.first) / step) else 0

    // --- RENDER SLIDER ---
    MaterialSlider(
        value = floatValue,
        onValueChange = { newValue -> onValueChange(newValue.toInt()) },
        modifier = modifier.fillMaxWidth(),
        valueRange = floatRange,
        steps = stepsCount,
        colors = SliderDefaults.colors(
            activeTrackColor = finalActiveTrackColor,
            inactiveTrackColor = finalInactiveTrackColor,
            thumbColor = finalThumbColor,
            activeTickColor = stepsColorWithOpacity,
            inactiveTickColor = stepsColorWithOpacity // Use the same color for both
        ),
    )
}

@Composable
fun Slider(
    value: State<Int>, // RESTORED
    range: IntRange = 0..100, // Uses standard IntRange
    step: Int = 0,
    modifier: Modifier = Modifier
) {
    // Delegates to the primary implementation using the State<Int> binding
    val b = value.binding()
    Slider(
        value = b.value,
        onValueChange = b.set,
        range = range,
        step = step,
        modifier = modifier
    )
}

@Composable
fun ScrollView(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        content = content
    )
}