package e.ib.tictactoe

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TableRow
import e.ib.tictactoe.impl.Item
import e.ib.tictactoe.impl.Item.*
import e.ib.tictactoe.impl.ai.AI
import java.util.*
import java.lang.Math.abs

class TicTacToeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
    }

    /*
    private fun game() {
        while(this.checkIfStillPlaying()) {
            if (isChosen) {
                val coords = getCoordsOfSelected()
                if (this.placeMarker(coords)) {
                    chosen?.setBackgroundResource(if (currentPlayer == O) R.drawable.o else R.drawable.x)
                }
                chosen = null
                isChosen = false
            }

        }
    }*/

    /*
    private fun getCoordsOfSelected() : Array<Int> {
        for(i in 0 until 3){
            val row = table.getChildAt(i) as TableRow
            for(j in 0 until 3){
                if (row.getChildAt(j) == chosen) return arrayOf(i, j)
            }
        }
        throw Exception()
    }
*/


}