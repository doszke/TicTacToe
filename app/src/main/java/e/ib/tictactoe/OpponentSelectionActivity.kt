package e.ib.tictactoe

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class OpponentSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oponent_selection)
    }

    fun chosenPc(view : View) {
        val intent = Intent(applicationContext, DifficultyLvlSelectionActivity::class.java)
        startActivity(intent)
    }

    fun chosenTwoPlayers(view : View) {
        setContentView(R.layout.activity_game)
        val intent = Intent(applicationContext, TicTacToeActivity::class.java).apply{
            putExtra("mode", UserChoice.TWO_PLAYERS)
        }
        startActivity(intent)
    }


}