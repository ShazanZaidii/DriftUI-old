package com.example.driftui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path as ComposePath
import kotlin.math.abs // Import for math

class Path {
    // Stores a list of strokes, where each stroke is a list of Offset points.
    private val _strokes = mutableListOf<MutableList<Offset>>()

    // Public getter for all strokes
    val strokes: List<List<Offset>> get() = _strokes

    // Stores the current stroke being drawn
    private var currentStroke: MutableList<Offset>? = null

    fun start(at: Offset) {
        val newStroke = mutableListOf(at)
        _strokes.add(newStroke)
        currentStroke = newStroke
    }

    fun lineTo(p: Offset) {
        currentStroke?.add(p)
    }

    fun finish() {
        currentStroke = null
    }

    fun clear() {
        _strokes.clear()
        currentStroke = null
    }

    fun removeStrokeAt(point: Offset, radius: Float): Boolean {
        val iterator = _strokes.iterator()
        var wasRemoved = false
        val radiusSq = radius * radius

        while (iterator.hasNext()) {
            val stroke = iterator.next()
            val isHit = stroke.any { strokePoint ->
                val dx = strokePoint.x - point.x
                val dy = strokePoint.y - point.y
                (dx * dx + dy * dy) <= radiusSq
            }

            if (isHit) {
                iterator.remove()
                wasRemoved = true
            }
        }
        return wasRemoved
    }

    fun smoothAllStrokes(): Path {
        val smoothedStrokes = mutableListOf<MutableList<Offset>>()
        for (_points in _strokes) {
            if (_points.size < 3) {
                smoothedStrokes.add(_points)
                continue
            }
            val smoothed = mutableListOf<Offset>()
            smoothed.add(_points.first())
            for (i in 1 until _points.size - 1) {
                val prev = _points[i - 1]
                val curr = _points[i]
                val next = _points[i + 1]
                val mid1 = Offset((prev.x + curr.x) / 2, (prev.y + curr.y) / 2)
                val mid2 = Offset((curr.x + next.x) / 2, (curr.y + next.y) / 2)
                smoothed.add(mid1)
                smoothed.add(mid2)
            }
            smoothed.add(_points.last())
            smoothedStrokes.add(smoothed)
        }
        _strokes.clear()
        _strokes.addAll(smoothedStrokes)
        return this
    }

    fun toComposePath(): ComposePath {
        val cp = ComposePath()
        for (stroke in _strokes) {
            if (stroke.isEmpty()) continue
            cp.moveTo(stroke.first().x, stroke.first().y)
            for (p in stroke.drop(1)) {
                cp.lineTo(p.x, p.y)
            }
        }
        return cp
    }

    // --- FIXED: Function is now INSIDE the class ---
    fun eraseAreaAt(eraserPos: Offset, radius: Float): Boolean {
        val newStrokes = mutableListOf<MutableList<Offset>>()
        var hasChanged = false
        val radiusSq = radius * radius

        for (stroke in _strokes) {
            // 1. Fast Bounding Box Check
            if (stroke.isNotEmpty()) {
                val first = stroke.first()
                // Using kotlin.math.abs
                if (abs(first.x - eraserPos.x) > radius + 500 &&
                    abs(first.y - eraserPos.y) > radius + 500) {
                    newStrokes.add(stroke)
                    continue
                }
            }

            // 2. Cutting Logic
            var currentSegment = mutableListOf<Offset>()

            for (point in stroke) {
                val dx = point.x - eraserPos.x
                val dy = point.y - eraserPos.y

                // Check if point is OUTSIDE the eraser
                if (dx * dx + dy * dy > radiusSq) {
                    currentSegment.add(point)
                } else {
                    // Point is INSIDE. Cut here.
                    if (currentSegment.isNotEmpty()) {
                        if (currentSegment.size >= 2) {
                            newStrokes.add(currentSegment)
                        }
                        currentSegment = mutableListOf()
                    }
                    hasChanged = true
                }
            }

            // Add remainder
            if (currentSegment.isNotEmpty() && currentSegment.size >= 2) {
                newStrokes.add(currentSegment)
            }
        }

        if (hasChanged) {
            _strokes.clear()
            _strokes.addAll(newStrokes)
        }
        return hasChanged
    }
}