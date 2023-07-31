package board

import java.lang.IllegalArgumentException
import java.util.function.Consumer


open class SquareBoardImpl(final override val size: Int) : SquareBoard {

    var squareCells: Array<Array<Cell>> = arrayOf()

    init {
        squareCells += createArray(size)
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        try {
            verifyArguments(i, j)
        } catch (ex: IllegalArgumentException) {
            return null
        }

        return squareCells[i - 1][j - 1]
    }

    override fun getCell(i: Int, j: Int): Cell {
        verifyArguments(i, j)

        return squareCells[i - 1][j - 1]
    }

    override fun getAllCells(): Collection<Cell> {
        val listOfCells: ArrayList<Cell> = ArrayList(size * size);

        for (rows in 1..size) {
            for (cols in 1..size) {
                listOfCells.add(squareCells[rows - 1][cols - 1]);
            }
        }

        return listOfCells;
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        verifyArguments(i);

        val listOfCells: ArrayList<Cell> = ArrayList(size * size);

        jRange.forEach(Consumer {
            if (it <= size) {
                listOfCells.add(squareCells[i - 1][it - 1])
            }
        })

        return listOfCells;
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        verifyArguments(j)
        val listOfCells: ArrayList<Cell> = ArrayList(size * size)
        iRange.forEach(Consumer { it ->
            if (it <= size) {
                listOfCells.add(squareCells[it - 1][j - 1])
            }
        })

        return listOfCells
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        val cell: Cell?
        if (direction == Direction.DOWN) {
            if (this.i + 1 > size || this.j > size) {
                cell = null
            } else {
                cell = squareCells[i][j - 1]
            }
        } else if (direction == Direction.UP) {

            if (this.i <= 1 || this.j > size) {
                cell = null
            } else {
                cell = squareCells[this.i - 2][this.j - 1]
            }
        } else if (direction == Direction.RIGHT) {
            cell = if (this.i > size || this.j + 1 > size) {
                null
            } else {
                squareCells[i - 1][j]
            }
        } else {
            cell = if (this.i > size || this.j <= 1) {
                null
            } else {
                squareCells[i - 1][j - 2]
            }
        }

        return cell
    }

    private fun verifyArguments(i: Int, j: Int) {
        if (i > size || j > size) {
            throw IllegalArgumentException("incorrect values of i and j")
        }
    }

    private fun verifyArguments(i: Int) {
        if (i > size) {
            throw IllegalArgumentException("incorrect values of i and j")
        }
    }

    private fun createArray(arrSize: Int): Array<Array<Cell>> {
        var cols = arrayOf<Array<Cell>>();

        for (i in 1..arrSize) {
            var rows = arrayOf<Cell>();

            for (j in 1..arrSize) {
                rows += Cell(i, j);
            }

            cols += rows;
        }

        return cols;
    }
}

class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {

    private val boardCellsMap: MutableMap<Cell, T?> = mutableMapOf()

    init {
        for (i in 1..width) {
            for (j in 1..width) {
                val cell = squareCells[i - 1][j - 1]
                boardCellsMap.put(cell, null)
            }
        }
    }

    override fun get(cell: Cell): T? {
        return boardCellsMap[cell]
    }

    override fun set(cell: Cell, value: T?) {
        boardCellsMap[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return boardCellsMap.keys
            .filter { it ->
                boardCellsMap[it]
                    .let(predicate)
            }
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return boardCellsMap.keys
            .find {
                boardCellsMap[it].let(predicate)
            }

    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return boardCellsMap.values
            .any(predicate)
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return boardCellsMap.values.all(predicate)
    }

}

fun createSquareBoard(width: Int): SquareBoard {
    return SquareBoardImpl(width)
}
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)
