package com.example.snakeice

import kotlin.random.Random

enum class Direction { Up, Down, Left, Right }
data class Cell(val x: Int, val y: Int)

class SnakeGame(val cols: Int = 20, val rows: Int = 28) {
    var snake = mutableListOf<Cell>()
    var direction = Direction.Right
    var food = randomFood(emptyList())
    var score = 0
    var alive = true

    init { reset() }

    fun reset() {
        snake = mutableListOf(Cell(cols/2, rows/2))
        direction = Direction.Right
        score = 0
        alive = true
        food = randomFood(snake)
    }

    fun changeDir(newDir: Direction) {
        val bad = (direction == Direction.Up && newDir == Direction.Down) ||
                (direction == Direction.Down && newDir == Direction.Up) ||
                (direction == Direction.Left && newDir == Direction.Right) ||
                (direction == Direction.Right && newDir == Direction.Left)
        if (!bad) direction = newDir
    }

    fun step() {
        if (!alive) return
        val head = snake.first()
        val next = when(direction) {
            Direction.Up -> Cell(head.x, head.y - 1)
            Direction.Down -> Cell(head.x, head.y + 1)
            Direction.Left -> Cell(head.x - 1, head.y)
            Direction.Right -> Cell(head.x + 1, head.y)
        }
        if (next.x !in 0 until cols || next.y !in 0 until rows || next in snake) {
            alive = false; return
        }
        snake.add(0, next)
        if (next == food) {
            score += 10
            food = randomFood(snake)
        } else {
            snake.removeLast()
        }
    }

    private fun randomFood(occupied: List<Cell>): Cell {
        var c: Cell
        do {
            c = Cell(Random.nextInt(cols), Random.nextInt(rows))
        } while (occupied.contains(c))
        return c
    }
}
