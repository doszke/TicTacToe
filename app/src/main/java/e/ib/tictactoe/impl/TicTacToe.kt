package e.ib.tictactoe.impl

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import e.ib.tictactoe.TicTacToeActivity
import e.ib.tictactoe.impl.ai.AI
import java.util.*
import kotlin.math.floor

class TicTacToe : View {


    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {init()}
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    var currentPlayer : Item
    private lateinit var ai : AI
    private val queue : Queue<Item> =  LinkedList<Item>()

    private var sideLen = width/3
    private var padding = 0.1*width

    private var selection_i : Float = -1f
    private var selection_j : Float = -1f


    private val black = Paint(Color.BLACK)
    private val white = Paint(Color.WHITE)

    var BOARD : Array<Array<Item>> = arrayOf(
        arrayOf(Item.O, Item.EMPTY, Item.EMPTY),
        arrayOf(Item.O, Item.EMPTY, Item.EMPTY),
        arrayOf(Item.O, Item.EMPTY, Item.EMPTY)
    )


    init {
        val r  = Random()
        val firstPlayer = if (r.nextBoolean()) Item.X else Item.O
        val otherPlayer = if (firstPlayer == Item.X) Item.O else Item.X
        for (i in 0 until 9) {
            queue.add(if(i%2==0) firstPlayer else otherPlayer)
        }
        currentPlayer = queue.poll()
        init()

        this.setOnClickListener {
            Log.d("SELECTION_COORDS", "$selection_i $selection_j ")
            if (selection_i != -1f && selection_j !=- 1f) if (placeMarker(selection_i.toInt(), selection_j.toInt())) this.invalidate()

        }

    }

    private fun init() {
        black.style = Paint.Style.STROKE
        black.strokeWidth = width/50f
        sideLen = width/3
        padding = 0.1*width
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.rawX
        val y = event?.rawY
        if (x is Float && y is Float) {
            var i = floor(x / sideLen)
            var j = floor(y / sideLen)
            selection_i = if (i == 3.0f) 2f else i
            selection_j = if (j == 3.0f) 2f else j
            Log.d("MyCanvas\$onTouchEvent", "$i $j")
        }
        performClick()
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()

        return true
    }

    fun placeMarker(i : Int, j : Int) : Boolean{
        Log.d("XDDDDDD", "IM HEREEEEEEEEEEEEEEEEEEEEEEEe")
        if (BOARD[i][j] == Item.EMPTY) {
            BOARD[i][j] = currentPlayer
            currentPlayer = queue.poll()
            Log.d("XDDDDD", currentPlayer.toString())
            return true
        }
        return false
    }


    fun checkIfStillPlaying() : Boolean {
        return validateBoard() == Item.EMPTY && !isDraw() //nikt nie wygrał i nie ma remisu
    }

    private fun isDraw() : Boolean {
        return validateBoard() == Item.EMPTY && queue.isEmpty()
    }

    private fun validateBoard() : Item {
        val col = validateColumns()
        val cross = validateCross()
        val row = validateRows()
        return if (col != Item.EMPTY) col else if (cross != Item.EMPTY) return cross else row //jak row też bedzie EMPTY to całośc będzie EMPTY, czyli nikt nie wygrał
    }

    private fun validateCross() : Item {
        var placeholder = arrayOf(BOARD[0][0].value + BOARD[1][1].value + BOARD[2][2].value, BOARD[0][2].value + BOARD[1][1].value + BOARD[2][0].value)
        var lastItems = arrayOf(BOARD[2][2], BOARD[2][0])
        return validate(placeholder, lastItems)
    }

    private fun validateColumns() : Item {
        val output = arrayOf(0, 0, 0)
        val lastItem = arrayOf<Item>(Item.EMPTY, Item.EMPTY, Item.EMPTY)
        for (i in 0..2) {
            for (j in 0..2)
                output[j] += BOARD[i][j].value
            lastItem[i] = BOARD[2][i]
        }
        return validate(output, lastItem)
    }

    private fun validateRows() : Item {
        val output = arrayOf(0, 0, 0)
        val lastItem = arrayOf<Item>(Item.EMPTY, Item.EMPTY, Item.EMPTY)
        for (i in 0..2) {
            BOARD[i].forEach { output[i] += it.value }
            lastItem[i] = BOARD[i][2]
        }
        return validate(output, lastItem)
    }

    private fun validate(output: Array<Int>, lastItem: Array<Item>) : Item {
        for (i in 0 until output.size)
            if (Math.abs(output[i]) == 3) return lastItem[i]
        return Item.EMPTY
    }

    @Deprecated("Console use only")
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





    override fun onDraw(canvas: Canvas?) {
        init()
        canvas?.drawColor(Color.WHITE)
        this.drawBoard(canvas)
        this.drawWonVert(canvas, 0)
    }

    private fun drawBoard(canvas : Canvas?) {
        canvas?.drawLine(sideLen.toFloat(), 0f, sideLen.toFloat(), width.toFloat(), black)
        canvas?.drawLine(2* sideLen.toFloat(), 0f, 2*sideLen.toFloat(), width.toFloat(), black)
        canvas?.drawLine(0f, sideLen.toFloat(), width.toFloat(), sideLen.toFloat(), black)
        canvas?.drawLine(0f, 2* sideLen.toFloat(), width.toFloat(), 2*sideLen.toFloat(), black)
        this.drawBoardContent(canvas)
    }

    private fun drawBoardContent(canvas: Canvas?) {
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (BOARD[i][j] == Item.X) drawX(canvas, i, j) else if (BOARD[i][j] == Item.O) drawO(canvas, i, j)
            }
        }
    }


    private fun drawX(canvas : Canvas?, i : Int, j : Int){
        val b = bounds(i, j)
        canvas?.drawLine(b[0], b[1], b[2], b[3], black)
        canvas?.drawLine(b[0], b[3], b[2], b[1], black)
    }

    private fun drawO(canvas : Canvas?, i : Int, j : Int) {
        val b = bounds(i, j)
        val x = (b[0] + b[2])/2
        val y = (b[1] + b[3])/2
        canvas?.drawCircle(x, y, (sideLen/2 - padding).toFloat(), black)
    }

    private fun drawWonHor(canvas : Canvas?, row : Int) {
        canvas?.drawLine(padding.toFloat(), (row*sideLen + sideLen/2).toFloat(),
            (width - padding).toFloat(), (row*sideLen + sideLen/2).toFloat(), black)
    }

    private fun drawWonVert(canvas : Canvas?, col : Int){
        canvas?.drawLine((col*sideLen + sideLen/2).toFloat(),padding.toFloat(),
            (col*sideLen + sideLen/2).toFloat(), (width - padding).toFloat(),  black)
    }

    private fun drawWonCross(canvas : Canvas?, param : Int) {
        if (param == 0) canvas?.drawLine(padding.toFloat(), padding.toFloat(), (width - padding).toFloat(),
            (width - padding).toFloat(), black)
        else if (param == 1) {
            Log.d("testing", "jestem tu")
            canvas?.drawLine(padding.toFloat(), (width - padding).toFloat(),
                (width - padding).toFloat(),padding.toFloat(), black)
        }
    }

    private fun bounds(i : Int, j : Int) : Array<Float>{
        val _x : Float = (i*sideLen + padding).toFloat()
        val _y : Float = (j*sideLen + padding).toFloat()
        val x_ : Float = (i*sideLen + sideLen - padding).toFloat()
        val y_ : Float = (j*sideLen + sideLen - padding).toFloat()
        return arrayOf(_x, _y, x_, y_)
    }

}