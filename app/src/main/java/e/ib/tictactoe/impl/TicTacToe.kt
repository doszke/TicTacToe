package e.ib.tictactoe.impl

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import e.ib.tictactoe.impl.ai.AI
import e.ib.tictactoe.impl.ai.PerfectAI
import e.ib.tictactoe.impl.ai.RandomAI
import java.util.*
import kotlin.math.floor
import java.io.Serializable


open class TicTacToe
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    View(context, attrs, defStyle), Serializable {

    companion object {
        const val serialVersionUID = 1L
    }

    //protected open- potrzebne do renderu obrazka na mainActivity
    protected open var sideLen = width / 3
    protected open var padding = 0.1 * width
    protected open var _padding = 0.05 * width

    protected open var BOARD: Array<Array<Item>> = arrayOf(
        arrayOf(Item.EMPTY, Item.EMPTY, Item.EMPTY),
        arrayOf(Item.EMPTY, Item.EMPTY, Item.EMPTY),
        arrayOf(Item.EMPTY, Item.EMPTY, Item.EMPTY)
    )

    protected open lateinit var currentPlayer: Item


    private var selectionI: Float = -1f
    private var selectionJ: Float = -1f

    @Transient protected open val black = Paint(Color.BLACK)

    /**
     * Set to 0 for row win, 1 for column win, 2 for cross win
     */
    private var winType = -1

    /**
     * Set to the winning row, or winning column number. Set to 0 for diagonal from (0,0) to (2,2), for other 1.
     */
    private var winParam = -1

    private var ai: AI? = null
    private var queue: Queue<Item> = LinkedList<Item>()

    init {
        //zapis i odczyt stanu po obrocie ekranu
        isSaveEnabled = true
        this.setOnClickListener { this.clickPerformed(it) } //
        this.setOnTouchListener { view, event -> this.myOnTouchEvent(view, event) } //domyślny
    }

    fun start(){
        clickPerformed(this) //uruchamiam w ten sposób
    }


    fun restore(deserialized : TicTacToe?) : Unit {
        if (deserialized != null) {
            this.selectionI = deserialized.selectionI
        }
        if (deserialized != null) {
            this.selectionJ = deserialized.selectionJ
        }
        if (deserialized != null) {
            this.BOARD = deserialized.BOARD
        }
        if (deserialized != null) {
            this.queue = deserialized.queue
        }
        if (deserialized != null) {
            this.currentPlayer  = deserialized.currentPlayer
        }
        if (deserialized != null) {
            this.winType = deserialized.winType
        }
        if (deserialized != null) {
            this.winParam = deserialized.winParam
        }
        if (deserialized != null) {
            this.ai = deserialized.ai
        }
    }


    fun initializeGame(choice : UserChoice?) : TicTacToe {
        val r = Random()
        val firstPlayer = if (r.nextBoolean()) Item.X else Item.O
        val otherPlayer = if (firstPlayer == Item.X) Item.O else Item.X
        for (i in 0 until 9) {
            queue.add(if (i % 2 == 0) firstPlayer else otherPlayer)
        }
        currentPlayer = queue.poll()
        if (choice == UserChoice.TWO_PLAYERS || choice == null) {
            ai = null
        } else {
            val aiMarker = if (r.nextBoolean()) Item.X else Item.O
            val playerMarker = if (aiMarker == Item.X) Item.O else Item.X
            if (choice == UserChoice.COMPUTER_EASY) ai = RandomAI(BOARD, aiMarker, playerMarker)
            else if (choice == UserChoice.COMPUTER_HARD) ai = PerfectAI(BOARD, aiMarker, playerMarker)
        }
        return this
    }


    protected fun init() {
        black.style = Paint.Style.STROKE
        black.strokeWidth = width / 50f
        sideLen = width / 3
        padding = 0.1 * width
        _padding = padding / 2
    }


    /**
     * Idle onClickListener
     */
    protected fun idleOnClick(view: View) {

    }

    /**
     * OnCLickListener
     */
    private fun clickPerformed(view: View) {
        Log.d("TicTacToe.clickPerform" ,"begin")
        if (queue.size == 8 && ai?.AImarker == currentPlayer)
            playRoundAi() //gdy AI zaczyna
        if (selectionI != -1f && selectionJ != -1f) {
            if (ai != null) playRoundAi() else playRound()
        }
        Log.d("TicTacToe.clickPerform" ,"end")
        this.invalidate()
    }


    private fun playRound() {
        if (placeMarker(selectionI.toInt(), selectionJ.toInt())) { //czy umieszczono znak
            if (queue.isNotEmpty()) { //kolejka nie jest pusta
                if (validateBoard() != Item.EMPTY) { //jest wygrany
                    setPreciseWinnerInfo()
                    this.setOnClickListener { idleOnClick(it) }
                } else {  //nie ma wygranego
                    currentPlayer = queue.poll()
                }
            } else { //kolejka jest pusta
                if (validateBoard() != Item.EMPTY) { //jest wygrany
                    setPreciseWinnerInfo()
                } //nie ma wygranego i brak wolnych pozycji -> remis
            this.setOnClickListener { idleOnClick(it) }
            }
            //po każdym umieszczonym znaku odświeżam onDraw
        }
        this.invalidate()
    }

    /**
     * Method used to play with AI
     */
    private fun playRoundAi() {
        Log.d("TicTacToe.clickPerform" ,"playRoundAi")
        if (currentPlayer == ai?.AImarker) {
            Log.d("TicTacToe.clickPerform" ,"ai marker")
            this.setOnTouchListener { v, event -> idleOnTouchEvent(v, event) } //wyłączam sterowanie przez gracza (przechwytywanie wsp)
            val coords = ai?.getCoords() //pobieram położenie znaku AI
            if (coords != null) {
                selectionI = coords[0].toFloat() //ustawiam do kontenerów położenia na ekranie
                selectionJ = coords[1].toFloat()
            }
            playRound() //umieszczam na tablicy i waliduję
            this.setOnTouchListener { v, event -> myOnTouchEvent(v, event) } //ustawiam myOnTouchEventListener z powrotem
        } else {
            Log.d("TicTacToe.clickPerform" ,"player marker $selectionI $selectionJ")
            playRound()
        }
        this.invalidate()
    }

    fun idleOnTouchEvent(view: View, event: MotionEvent?): Boolean {
        return false
    }

    //[0-width/3) -> 0, [width/3, 2*width/3) -> 1, [2*width/3, width) -> 2, width -> 2
    //metoda mapuje współprzędne piksela naciśniętego przez użytkownika na indeksy tablicy przechowującej Item'y
    fun myOnTouchEvent(view: View, event: MotionEvent?): Boolean {
        val absCoords = IntArray(2)
        getLocationOnScreen(absCoords) //pozycja widoku na ekranie
        val (mX, mY) = absCoords
        var x : Float = 0f
        var y : Float = 0f
        if (event != null) {
            x = event.rawX - mX //rawX i rawY względem 0,0
            y = event.rawY - mY //po odjęciu od położenia view mamy x i y względem górnego lewego rogu view
        }
        val i = floor(x / sideLen)
        val j = floor(y / sideLen)
        selectionI = if (i == 3.0f) 2f else i //idealnie na krawędzi wyniesie 3
        selectionJ = if (j == 3.0f) 2f else j
        Log.d("MyCanvas\$onTouchEvent", "$i $j")
        performClick()
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()

        return true
    }

    fun placeMarker(i: Int, j: Int): Boolean {
        if (BOARD[i][j] == Item.EMPTY) {
            BOARD[i][j] = currentPlayer
            return true
        }
        return false
    }


    fun setPreciseWinnerInfo() {
        //currentPlayer to ostatni położony znak, w przypadku wygranej
        when (currentPlayer) {
            validateRows() -> {
                this.winType = 0
                for (i in 0 until 3) {
                    if ((BOARD[i][0] == BOARD[i][1]) && BOARD[i][1] == BOARD[i][2] && BOARD[i][0] != Item.EMPTY) {
                        this.winParam = i
                        break
                    }
                }
            }
            validateColumns() -> {
                this.winType = 1
                for (i in 0 until 3) {
                    if ((BOARD[0][i] == BOARD[1][i]) && BOARD[1][i] == BOARD[2][i] && BOARD[0][i] != Item.EMPTY) {
                        this.winParam = i
                        break
                    }
                }
            }
            else -> { //funkcja uruchamiana, gdy następuje wygrana
                this.winType = 2
                this.winParam = if ((BOARD[0][0] == BOARD[1][1]) && (BOARD[1][1] == BOARD[2][2])) 0 else 1
            }
        }
    }


    private fun validateBoard(): Item {
        val col = validateColumns()
        val cross = validateCross()
        val row = validateRows()
        return if (col != Item.EMPTY) col else if (cross != Item.EMPTY) cross else row //jak row też bedzie EMPTY to całośc będzie EMPTY, czyli nikt nie wygrał
    }

    private fun validateCross(): Item {
        var placeholder = arrayOf(
            BOARD[0][0].value + BOARD[1][1].value + BOARD[2][2].value,
            BOARD[0][2].value + BOARD[1][1].value + BOARD[2][0].value
        )
        var lastItems = arrayOf(BOARD[2][2], BOARD[2][0])
        return validate(placeholder, lastItems)
    }

    private fun validateColumns(): Item {
        val output = arrayOf(0, 0, 0)
        val lastItem = arrayOf<Item>(
            Item.EMPTY,
            Item.EMPTY,
            Item.EMPTY
        )
        for (i in 0..2) {
            for (j in 0..2)
                output[j] += BOARD[i][j].value
            lastItem[i] = BOARD[2][i]
        }
        return validate(output, lastItem)
    }

    private fun validateRows(): Item {
        val output = arrayOf(0, 0, 0)
        val lastItem = arrayOf<Item>(
            Item.EMPTY,
            Item.EMPTY,
            Item.EMPTY
        )
        for (i in 0..2) {
            BOARD[i].forEach { output[i] += it.value }
            lastItem[i] = BOARD[i][2]
        }
        return validate(output, lastItem)
    }

    private fun validate(output: Array<Int>, lastItem: Array<Item>): Item {
        for (i in 0 until output.size)
            if (Math.abs(output[i]) == 3) return lastItem[i]
        return Item.EMPTY
    }

    @Deprecated("Console use only")
    override fun toString(): String {
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


    /* draw util */
    override fun onDraw(canvas: Canvas?) {
        init()
        val bgColor = context.getColor(e.ib.tictactoe.R.color.bg)
        canvas?.drawColor(bgColor)
        this.drawBoard(canvas)
        when (winType) {
            0 -> //row win
                this.drawWonRow(canvas, winParam)
            1 -> //col win
                this.drawWonCol(canvas, winParam)
            2 -> //cross win
                this.drawWonCross(canvas, winParam)
            else -> return
        }
    }

    protected fun drawBoard(canvas: Canvas?) {
        canvas?.drawLine(sideLen.toFloat(), 0f, sideLen.toFloat(), width.toFloat(), black)
        canvas?.drawLine(2 * sideLen.toFloat(), 0f, 2 * sideLen.toFloat(), width.toFloat(), black)
        canvas?.drawLine(0f, sideLen.toFloat(), width.toFloat(), sideLen.toFloat(), black)
        canvas?.drawLine(0f, 2 * sideLen.toFloat(), width.toFloat(), 2 * sideLen.toFloat(), black)
        this.drawBoardContent(canvas)
    }

    protected fun drawBoardContent(canvas: Canvas?) {
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (BOARD[i][j] == Item.X) drawX(canvas, i, j) else if (BOARD[i][j] == Item.O) drawO(
                    canvas,
                    i,
                    j
                )
            }
        }
    }

    protected fun drawX(canvas: Canvas?, i: Int, j: Int) {
        val b = bounds(i, j)
        canvas?.drawLine(b[0], b[1], b[2], b[3], black)
        canvas?.drawLine(b[0], b[3], b[2], b[1], black)
    }

    protected fun drawO(canvas: Canvas?, i: Int, j: Int) {
        val b = bounds(i, j)
        val x = (b[0] + b[2]) / 2
        val y = (b[1] + b[3]) / 2
        canvas?.drawCircle(x, y, (sideLen / 2 - padding).toFloat(), black)
    }

    protected fun drawWonCol(canvas: Canvas?, row: Int) {
        canvas?.drawLine(
            _padding.toFloat(), (row * sideLen + sideLen / 2).toFloat(),
            (width - _padding).toFloat(), (row * sideLen + sideLen / 2).toFloat(), black
        )
    }

    protected fun drawWonRow(canvas: Canvas?, col: Int) {
        canvas?.drawLine(
            (col * sideLen + sideLen / 2).toFloat(), _padding.toFloat(),
            (col * sideLen + sideLen / 2).toFloat(), (width - _padding).toFloat(), black
        )
    }

    protected fun drawWonCross(canvas: Canvas?, param: Int) {
        if (param == 0) canvas?.drawLine(
            _padding.toFloat(), _padding.toFloat(), (width - _padding).toFloat(),
            (width - _padding).toFloat(), black
        )
        else if (param == 1) {
            canvas?.drawLine(
                _padding.toFloat(), (width - _padding).toFloat(),
                (width - _padding).toFloat(), _padding.toFloat(), black
            )
        }
    }

    private fun bounds(i: Int, j: Int): Array<Float> {
        val _x: Float = (i * sideLen + padding).toFloat()
        val _y: Float = (j * sideLen + padding).toFloat()
        val x_: Float = (i * sideLen + sideLen - padding).toFloat()
        val y_: Float = (j * sideLen + sideLen - padding).toFloat()
        return arrayOf(_x, _y, x_, y_)
    }

}