package e.ib.tictactoe.impl

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

class TicTacToeDrawing
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : TicTacToe(context, attrs, defStyle) {

    init {
        this.setOnClickListener { this.idleOnClick(it) } //wyłącz funkcjonalność
    }

    private val idxs = arrayOf(
        arrayOf(1,1),
        arrayOf(0,1),
        arrayOf(0,0),
        arrayOf(0,2),
        arrayOf(2,2)
    )

    override fun onDraw(canvas: Canvas?) {
        init()
        drawBoard(canvas)
        for (i in 0 until idxs.size) {
            currentPlayer = if (i % 2 == 0) Item.O else Item.X
            placeMarker(idxs[i][0], idxs[i][1])
        }
        drawBoardContent(canvas)
        drawWonCross(canvas, 0)

    }

}