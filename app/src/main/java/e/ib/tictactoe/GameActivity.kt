package e.ib.tictactoe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import e.ib.tictactoe.impl.TicTacToe

class GameActivity : AppCompatActivity() {

    private val ttt : TicTacToe = TicTacToe()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        ttt.play()
    }


}