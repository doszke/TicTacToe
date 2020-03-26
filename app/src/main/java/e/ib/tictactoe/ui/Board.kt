package e.ib.tictactoe.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import e.ib.zad1.Item
import e.ib.zad1.TicTacToe
import org.jetbrains.annotations.Nullable

class Board(context : Context, board : Array<Array<Item>>) : View(context) {

    var layout_height = 300
    var layout_width = 300

    private var ttt : TicTacToe = TicTacToe()

    constructor(context : Context, board : Array<Array<Item>>, width : Int, height : Int) : this(context, board) {
        this.layout_height = height
        this.layout_width = width
    }

    override fun onSizeChanged(w : Int, h : Int, oldw : Int, oldh : Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.layout_height = h
        this.layout_width = w
    }

    /*
    companion object {
        class Builder(private var context: Context, private var board: Array<Array<Item>>) {

            private var width : Double = 300.0
            private var height : Double = 300.0
            private var attributeSet : AttributeSet? = null


            fun width(width : Double) : Builder {
                this.width  = width
                return this
            }

            fun attributeSet(attributeSet : AttributeSet) : Builder {
                this.attributeSet = attributeSet
                return this
            }

            fun height(height : Double) : Builder {
                this.height  = height
                return this
            }

            fun squareSideLength(value : Double) : Builder {
                this.height = value
                this.width = value
                return this
            }

            fun build() : Board {
                return Board(context, board, width, height)
            }

        }
    }
*/
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

}