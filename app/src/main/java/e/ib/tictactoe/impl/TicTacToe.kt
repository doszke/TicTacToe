package e.ib.tictactoe.impl

import android.content.Context
import android.support.v7.widget.AppCompatImageButton
import android.util.AttributeSet
import android.view.View
import e.ib.tictactoe.R
import e.ib.tictactoe.impl.Item.*
import e.ib.tictactoe.impl.ai.AI
import java.util.*
import java.lang.Math.abs

class TicTacToe {

    //takie rozwiązanie umożliwia podgląd prywatnych pól klasy TTT bez naurszania zasad hermetyzacji
    inner class TTTImageButton(context : Context, set : AttributeSet?) : AppCompatImageButton(context, set) {

        var row: Int = 0
        var col: Int = 0

        init {
            this.setOnClickListener(this::onCLickListener)
        }

        //ustawia enum w wewnętrznej tablicy
        private fun placeMarker(coords : Array<Int>, marker : Item) : Unit {
            if (BOARD[coords[0]][coords[1]] != EMPTY) throw Exception() //do debugu, nie powinno wyrzucić, sprawdzam w onCLickListener
            else BOARD[coords[0]][coords[1]] = marker
        }

        //ustawia etykiete na
        protected fun onCLickListener(view : View) {
            if (BOARD[col][row] != EMPTY) return
            val resId = if (currentPlayer == Item.X) R.drawable.x else R.drawable.o
            this.setBackgroundResource(resId)
            placeMarker(arrayOf(row, col), currentPlayer)
            isChosen = true
        }


    }


    private lateinit var currentPlayer : Item
    private lateinit var ai : AI
    @Volatile internal var isChosen = false
    private val queue : Queue<Item> =  LinkedList<Item>()
    private val BOARD : Array<Array<Item>> = arrayOf(
        arrayOf(EMPTY, EMPTY, EMPTY),
        arrayOf(EMPTY, EMPTY, EMPTY),
        arrayOf(EMPTY, EMPTY, EMPTY)
    )

    init {
        val r  = Random()
        val firstPlayer = if (r.nextBoolean()) X else O
        val otherPlayer = if (firstPlayer == X) O else X
        for (i in 0 until 9) {
            queue.add(if(i%2==0) firstPlayer else otherPlayer)
        }
    }

    fun play(){
        while(validateBoard() == EMPTY) {
            if (queue.isEmpty()) break
            currentPlayer = queue.poll()
            waitForSelection()
        }
    }

    private fun waitForSelection() {
        while(!isChosen) { Thread.sleep(100) }
    }

    private fun validateBoard() : Item {
        val col = validateColumns()
        val cross = validateCross()
        val row = validateRows()
        return if (col != EMPTY) col else if (cross != EMPTY) return cross else row //jak row też bedzie EMPTY to całośc będzie EMPTY, czyli nikt nie wygrał
    }

    private fun validateCross() : Item {
        var placeholder = arrayOf(BOARD[0][0].value + BOARD[1][1].value + BOARD[2][2].value, BOARD[0][2].value + BOARD[1][1].value + BOARD[2][0].value)
        var lastItems = arrayOf(BOARD[2][2], BOARD[2][0])
        return validate(placeholder, lastItems)
    }

    private fun validateColumns() : Item {
        val output = arrayOf(0, 0, 0)
        val lastItem = arrayOf<Item>(EMPTY, EMPTY, EMPTY)
        for (i in 0..2) {
            for (j in 0..2)
                output[j] += BOARD[i][j].value
            lastItem[i] = BOARD[2][i]
        }
        return validate(output, lastItem)
    }

    private fun validateRows() : Item {
        val output = arrayOf(0, 0, 0)
        val lastItem = arrayOf<Item>(EMPTY, EMPTY, EMPTY)
        for (i in 0..2) {
            BOARD[i].forEach { output[i] += it.value }
            lastItem[i] = BOARD[i][2]
        }
        return validate(output, lastItem)
    }

    private fun validate(output: Array<Int>, lastItem: Array<Item>) : Item {
        for (i in 0 until output.size)
            if (abs(output[i]) == 3) return lastItem[i]
        return EMPTY
    }

    override fun toString() : String{
        val sb = StringBuilder("\n")
        for (i in 0..2) {
            for (j in 0..2) {
                sb.append(BOARD[i][j])
                if (j != 2) sb.append(" │ ")
            }
            if (i != 2) sb.append("\n─ ┼ ─ ┼ ─\n") else sb.append("\n")
        }
        return sb.toString()
    }

}