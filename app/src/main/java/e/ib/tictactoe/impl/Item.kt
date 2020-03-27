package e.ib.tictactoe.impl

import android.widget.ImageButton

enum class Item(val value: Int) {

    O(1), X(-1), EMPTY(0);

    override fun toString(): String {
        return if (this != EMPTY) super.toString()
        else " "
    }

}
