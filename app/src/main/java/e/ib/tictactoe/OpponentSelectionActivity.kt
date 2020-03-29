package e.ib.tictactoe

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import e.ib.tictactoe.impl.UserChoice

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
        val intent = Intent(applicationContext, TicTacToeActivity::class.java).apply{
            putExtra("mode", UserChoice.TWO_PLAYERS)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }


}