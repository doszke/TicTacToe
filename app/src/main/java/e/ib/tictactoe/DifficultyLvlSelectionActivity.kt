package e.ib.tictactoe

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import e.ib.tictactoe.impl.UserChoice

class DifficultyLvlSelectionActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_difficulty_lvl_selection)
    }

    fun onClickEasy(view : View) {
        val intent = Intent(applicationContext, TicTacToeActivity::class.java).apply{
            putExtra("mode", UserChoice.COMPUTER_EASY)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }

    fun onClickHard(view : View) {
        val intent = Intent(applicationContext, TicTacToeActivity::class.java).apply{
            putExtra("mode", UserChoice.COMPUTER_HARD)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)

    }

}