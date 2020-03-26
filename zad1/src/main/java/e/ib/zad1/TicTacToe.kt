package e.ib.zad1

import e.ib.zad1.Item.*
import e.ib.zad1.ai.*
import java.util.*
import java.lang.Math.abs

class TicTacToe {

    private lateinit var playerItem : Item
    private lateinit var ai : AI
    private var mode = -1
    private val r = Random()
    private val scanner = Scanner(System.`in`)
    private val BOARD : Array<Array<Item>> = arrayOf(
        arrayOf(EMPTY, EMPTY, EMPTY),
        arrayOf(EMPTY, EMPTY, EMPTY),
        arrayOf(EMPTY, EMPTY, EMPTY)
    )


    @Deprecated(message = "Only console app use")
    fun play() : Unit {
        //println("Witaj w grze \"Kółko i krzyżyk\"!")
        parsePlayerItem()
        parseModeSelection()
        if (mode == 0) {
            parseAISelection()
            gameAI()
        } else gamePlayer()
    }

    private fun parseAISelection() {
        //println("Wpisz 0 jak wybierasz łatwy poziom truności, a 1 jak wybierasz trudny")
        when (scanner.nextLine()) {
            "0" -> this.ai = RandomAI(this.BOARD, if (playerItem == O) X else O, playerItem)
            "1" -> this.ai = PerfectAI(this.BOARD, if (playerItem == O) X else O, playerItem)
            else -> {
                //println("Podałeś złą wartość")
                parseAISelection()
            } //ten sam wyjątek wyrzucany przez próbę wpisania stringa przy metodzie nextInt
        }
        return
    }

    private fun parseModeSelection() {
        //println("Wpisz 0 jak chcesz grać z komputerem, a 1 jak chcesz grać z kolegą przed komputerem")
        when (scanner.nextLine()) {
            "0" -> mode = 0
            "1" -> mode = 1
            else -> {
                //println("Podałeś złą wartość")
                parseModeSelection()
            } //ten sam wyjątek wyrzucany przez próbę wpisania stringa przy metodzie nextInt
        }
        return
    }

    private fun parsePlayerItem() : Unit {
        //println("Wpisz 0 jeżeli chcesz być kółkiem, lub 1 jeżeli chcesz być krzyżykiem")
        val value = scanner.nextLine()
        when (value) {
            "0" -> this.playerItem = O
            "1" -> this.playerItem = X
            else -> {
                //println("Podałeś złą wartość.")
                parsePlayerItem()
            } //ten sam wyjątek wyrzucany przez próbę wpisania stringa przy metodzie nextInt
        }
        return
    }

    private fun gamePlayer (): Unit {
        var turn = r.nextBoolean()
        var currentPlayer = X
        var placesLeft = 9
        //println(this)
        while (validateBoard() == EMPTY && placesLeft != 0) {
            currentPlayer = if (turn) playerItem else if (playerItem == O) X else O
            //println("Kolej gracza $currentPlayer")
            val coord = parseCoords()
            placeMarker(coord, currentPlayer)
            //println(this)
            placesLeft--
            turn = !turn
        }
        var winner = validateBoard()
        if (winner == EMPTY) println("Remis!")
        else println("Wygrał $currentPlayer!") //TODO przerób
    }

    private fun parseCoords() : Array<Int> {
        println("Podaj współrzędne w formacie \"x,y\", gdzie x to współrzędna wiersza, a y to współrzędna kolumny ")
        //wczytaj, podziel na tablice stringów, przemapuj na kolekcje intów i do tablicy
        return try {
            var coord = scanner.next("[0-2],[0-2]").split(",").map { e -> e.toInt() }.toTypedArray()
            coord
        } catch (e : Exception) {
            println("Podano złe wartości")
            parseCoords()
        }
    }

    private fun gameAI (): Item {
        var placesLeft = 9
        while (validateBoard() == EMPTY && placesLeft != 0) {
            var coord = parseCoords()
            placeMarker(coord, playerItem)
            placesLeft--
            //jak wygrana to brak
            if (validateBoard() != EMPTY || placesLeft == 0) break
            val idx = ai.getCoords()
            placeMarker(idx, if (playerItem == O) X else O)
            placesLeft--
        }
        return validateBoard() // dla remisu zwróci EMPTY
    }

    private fun placeMarker(coords : Array<Int>, marker : Item) : Unit {
        if (BOARD[coords[0]][coords[1]] != EMPTY) throw Exception()
        else BOARD[coords[0]][coords[1]] = marker
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