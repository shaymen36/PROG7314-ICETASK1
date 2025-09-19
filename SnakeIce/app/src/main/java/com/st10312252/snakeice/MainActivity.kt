package com.example.snakeice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnakeGameScreen()
        }
    }
}

@Composable
fun SnakeGameScreen() {
    val game = remember { SnakeGame() }
    var tick by remember { mutableStateOf(120L) }

    LaunchedEffect(game.alive) {
        while (game.alive) {
            delay(tick)
            game.step()
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Score: ${game.score}")

        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        if (abs(dragAmount.x) > abs(dragAmount.y)) {
                            if (dragAmount.x > 0) game.changeDir(Direction.Right)
                            else game.changeDir(Direction.Left)
                        } else {
                            if (dragAmount.y > 0) game.changeDir(Direction.Down)
                            else game.changeDir(Direction.Up)
                        }
                    }
                }
        ) {
            Canvas(Modifier.fillMaxSize()) {
                val cellW = size.width / game.cols
                val cellH = size.height / game.rows
                val cellSize = minOf(cellW, cellH)

                // snake
                game.snake.forEach {
                    drawRect(
                        topLeft = Offset(it.x * cellSize, it.y * cellSize),
                        size = androidx.compose.ui.geometry.Size(cellSize, cellSize)
                    )
                }
                // food
                drawRect(
                    topLeft = Offset(game.food.x * cellSize, game.food.y * cellSize),
                    size = androidx.compose.ui.geometry.Size(cellSize, cellSize)
                )
            }
        }
    }
}
