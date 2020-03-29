package e.ib.tictactoe.impl.ai

import e.ib.tictactoe.impl.Item
import java.io.Serializable
import java.lang.IllegalArgumentException

//algorytm rozwiązywania kólka i krzyżyk w sposób najbardziej optymalny
class PerfectAI(override val BOARD : Array<Array<Item>>, override val AImarker : Item, override val playerMarker: Item) : AI(), Serializable {

    companion object {
        const val serialVersionUID = 1L
    }

    override fun costMap(): Array<Array<Int>> {

        val output = arrayOf(
            arrayOf(0, 0, 0),arrayOf(0, 0, 0),arrayOf(0, 0, 0)
        )
        val myMarker = mapWhereAIMarker()
        val whereEmpty = mapWhereEmpty()
        val playerMarker = mapWherePlayerMarker()

        val horAI = analyzeArray(myMarker, 0)
        val verAI = analyzeArray(myMarker, 1)
        val crossAI = analyzeArray(myMarker, 2)

        val horPlayer = analyzeArray(playerMarker, 0)
        val verPlayer = analyzeArray(playerMarker, 1)
        val crossPlayer = analyzeArray(playerMarker, 2)

        /*
        1. Wykluczam pozycje, na które nie mogę postawić marker
        2. Wyszukuje pola, na które umieszczenie wrogiego markera spowoduje przegraną
        3. Wyszukuje pola o sekwencji podobnej do EMPTY, X, O (nieopłacalne do umieszczania markera)
        4. Wyznaczam wskaźnik dla pozostałych
         */

        for (i in 0 until 3) {
            for (j in 0 until 3) {
                var idx = if (i == j) 0 else 1 //to do tablic zaczynających się cross
                if (!whereEmpty[i][j]) {
                    output[i][j] = VALUE_WHERE_PLACED
                }
                else if (horAI[i] == 2 || verAI[j] == 2 || (if (isCross(i, j)) crossAI[idx] == 2 else false)) { //2 na 3 moje, czyli (X, X, EMPTY) gdzie X to marker AI
                    output[i][j] = MUST_PLACE_HERE + 1
                }//jeżeli jest po skosie    sprawdź po skosie     (crossPlayer 0 dla i == j, else crossplayer 1) czy jest 2, jak nie to false
                else if (horPlayer[i] == 2 || verPlayer[j] == 2 || (if (isCross(i, j)) crossPlayer[idx] == 2 else false)) { //sytuacja (O, O, EMPTY), gdzie O jest wrogie
                    output[i][j] = MUST_PLACE_HERE
                }
                else if ((horPlayer[i] == 1 && horAI[i] == 1) || (verPlayer[j] == 1 && verAI[j] == 1) || (if (isCross(i, j)) crossPlayer[idx] == 1 && crossAI[idx] == 1 else false) ) { //sytuacja (EMPTY, X, O)
                    output[i][j] = VALUE_WHERE_PLACED + 1 //najmniejsza wartosć wyłapywana przez program jako pusta komórka
                }
                else {
                    output[i][j] = 40* (horAI[i] + verAI[j] + (if (isCross(i, j)) crossPlayer[idx] else 1)) - 100 * (horAI[i] + verAI[j] + (if (isCross(i, j)) crossAI[idx] else 0))
                }
            }
        }
        return output
    }

    protected fun isCross(i : Int, j: Int) : Boolean {
        return i == j || i + j == 2
    }

    protected fun analyzeArray(array: Array<Array<Boolean>>, mode : Int) : Array<Int> {
        when (mode) {
            (0) -> {//vertical
                var out = arrayOf(0, 0, 0)
                for (i in 0 until 3) {
                    for (j in 0 until 3) {
                        out[i] += if (array[i][j]) 1 else 0
                    }
                }
                return out
            }
            (1) -> { //horizontal
                var out = arrayOf(0, 0, 0)
                for (i in 0 until 3) {
                    for (j in 0 until 3) {
                        out[i] += if (array[j][i]) 1 else 0
                    }
                }
                return out
            }
            (2) -> { //cross
                var out = arrayOf(0, 0)
                for (i in 0 until 3) {
                    out[0] += if (array[i][i]) 1 else 0
                    out[1] += if (array[2-i][i]) 1 else 0
                }
                return out
            }
            else -> throw IllegalArgumentException()
        }
    }
}