package e.ib.tictactoe

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import e.ib.tictactoe.impl.TicTacToe
import e.ib.tictactoe.impl.UserChoice

class TicTacToeActivity : AppCompatActivity() {

    private var choice : UserChoice? = null
    private lateinit var ttt : TicTacToe


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        choice = intent.getSerializableExtra("mode") as UserChoice
        ttt = findViewById(R.id.board)
        ttt.initializeGame(choice)
            .start()
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable("ttt", ttt)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val serialized = savedInstanceState?.getSerializable("ttt")
        if (serialized is TicTacToe){
            ttt.restore(serialized)
        }
    }


    fun reset(view : View) {
        val intent = Intent(applicationContext, TicTacToeActivity::class.java).apply {
            putExtra("mode", choice)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) //czysty rozruch aktywności
        }
        startActivity(intent)
    }

    fun main(view : View) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) //czysty rozruch aktywności
        }
        startActivity(intent)
    }


}