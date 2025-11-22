package com.example.driftui

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path as ComposePath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.foundation.Canvas as ComposeCanvas
import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.input.pointer.consume
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.positionChanged

class Path {
    private val _points = mutableListOf<Offset>()
    val points: List<Offset> get() = _points

    fun start(at: Offset) {
        _points.clear()
        _points.add(at)
    }

    fun lineTo(p: Offset) {
        _points.add(p)
    }

    fun finish() {}

    fun add(p: Offset) {
        _points.add(p)
    }

    fun clear() {
        _points.clear()
    }

    fun smooth(): Path {
        if (_points.size < 3) return this
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
        _points.clear()
        _points.addAll(smoothed)
        return this
    }

    fun toComposePath(): ComposePath {
        val cp = ComposePath()
        if (_points.isEmpty()) return cp
        cp.moveTo(_points.first().x, _points.first().y)
        for (p in _points.drop(1)) cp.lineTo(p.x, p.y)
        return cp
    }
}