package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment.Companion.Rectangle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import com.example.driftui.DriftView
import com.example.driftui.NavigationStack
import com.example.driftui.VStack
import com.example.driftui.darkMode
import com.example.driftui.lightMode
import com.example.driftui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
//        WindowCompat.setDecorFitsSystemWindows(window, true)
        super.onCreate(savedInstanceState)
        setContent {

        DrawingScreen()

        }
    }
}



@Composable
fun MyScreen() {
    val showSheet = State(false)
    var quantity = State(50)


    NavigationStack(
        Modifier.navigationBarBackButtonHidden(true).sheet(
            isPresented = showSheet,
            detents = listOf( 0.5, 0.95),
            initialDetent = 0.5,
            showGrabber = true,
            cornerRadius = 20,
            allowDismiss = true
        ) {
            // Sheet content
            VStack(spacing = 20) {
                Text("Sheet Content", Modifier.font(system(size = 24, weight = bold)))
                Text("Current state: ${showSheet.value}")

                Button(action = { showSheet.set(false) }, Modifier.offset(x = 160, y = -100)) {
                    Text("CLOSE X", Modifier.font(system(size = 18)))
                }

                Text("Swipe down or tap outside to dismiss")

            }
        }
    ) {
        // Main screen content
        DriftView() {



                    VStack() {
                        PenTool(color = Color.red, width = 12f, smooth = false)
                        }

                    }


            }
        }





@Composable
fun DrawingScreen() {

    val pathState = remember { State(Path()) }
    val isEraserMode = remember { State(false) }

    DriftView {

        VStack(spacing = 20) {

            // -----------------------------
            // TOP BAR
            // -----------------------------
            HStack(Modifier.padding(horizontal = 20, vertical = 10)) {
                Text("Drift Draw", Modifier.font(system(24, bold)))
                Spacer()

                Button(
                    action = {
                        pathState.value.clear()
                        pathState.set(Path())   // Refresh state
                    }
                ) {
                    Text("Clear", Modifier.foregroundStyle(Color.red))
                }
            }



            // -----------------------------
            // DRAWING AREA
            // -----------------------------
            ZStack(
                Modifier
                    .frame(300, maxHeight = 400)
                    .background(Color.white)
                    .cornerRadius(20)
                    .shadow(10)
                    .padding(20)
            ) {

                // DRAW TOOL
                PenTool(
                    path = pathState,
                    color = Color.black,
                    width = 5f,
                    smooth = true,
                    modifier = Modifier
                        .frame(300, 400)
                )

                // ERASER OVERLAY
                if (isEraserMode.value) {
                    EraserTool(
                        path = pathState,
                        radius = 30f,
                        type = EraserType.Area,
                        modifier = Modifier
                            .frame(300, 400)
                    )
                }
            }


            // -----------------------------
            // CONTROLS
            // -----------------------------
            HStack(spacing = 20) {

                Toggle("Eraser Mode", value = isEraserMode, Modifier.padding(bottom = 30))

                Capsule(width = 10, height = 10, Modifier.foregroundStyle(
                    if (isEraserMode.value) Color.red else Color.green
                ).padding(bottom = 30))


                Text(
                    if (isEraserMode.value) "Erasing" else "Drawing",
                    Modifier.font(system(14, medium))
                        .foregroundStyle(Color.gray).padding(bottom = 30)
                )
            }

        }
    }
}




