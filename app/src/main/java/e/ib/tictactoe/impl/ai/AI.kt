package e.ib.tictactoe.impl.ai


import android.util.Log
import e.ib.tictactoe.impl.Item
import java.util.*

abstract class AI()  {



    protected val VALUE_WHERE_PLACED = -50_000
    protected val MUST_PLACE_HERE = 50_000

    abstract val BOARD : Array<Array<Item>>
    abstract val AImarker : Item
    abstract val playerMarker : Item


    protected abstract fun costMap() : Array<Array<Int>>

    fun mapWhereEmpty() : Array<Array<Boolean>> {
        val output = arrayOf(
            arrayOf(false, false, false),
            arrayOf(false, false, false),
            arrayOf(false, false, false)
        )
        for (i in 0 until 3)
            for (j in 0 until 3)
                output[i][j] = BOARD[i][j] == Item.EMPTY
        return output
    }

    fun mapWherePlayerMarker() : Array<Array<Boolean>> {
        val output = arrayOf(
            arrayOf(false, false, false),
            arrayOf(false, false, false),
            arrayOf(false, false, false)
        )
        for (i in 0 until 3)
            for (j in 0 until 3)
                output[i][j] = BOARD[i][j] == playerMarker
        return output
    }

    fun mapWhereAIMarker() : Array<Array<Boolean>> {
        val output = arrayOf(
            arrayOf(false, false, false),
            arrayOf(false, false, false),
            arrayOf(false, false, false)
        )
        for (i in 0 until 3)
            for (j in 0 until 3)
                output[i][j] = BOARD[i][j] == AImarker
        return output
    }

    fun getCoords() : Array<Int> {
        val output = ArrayList<Array<Int>>()
        val map = costMap()

        var currentHighest = VALUE_WHERE_PLACED +1
        for (i in 0 until 3){
            for (j in 0 until 3) {
                if (currentHighest < map[i][j]) {
                    currentHighest = map[i][j]
                    output.clear()
                    output.add(arrayOf(i, j))
                } else if (currentHighest == map[i][j]) {
                    output.add(arrayOf(i, j))
                }
            }
        }
        //jeżeli jest kilka najwyżej puntkowanych miejsc, wybierz losowe
        Log.d("output.size", output.size.toString())
        return output[Random().nextInt(output.size)]
    }

}