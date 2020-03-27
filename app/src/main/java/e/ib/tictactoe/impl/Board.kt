package e.ib.tictactoe.impl

import e.ib.tictactoe.impl.Item
import e.ib.tictactoe.impl.TicTacToe

class Board(board : Array<Array<Item>>) {

    var layout_height = 300
    var layout_width = 300

    private var ttt : TicTacToe =
        TicTacToe()

    constructor(board : Array<Array<Item>>, width : Int, height : Int) : this(board) {
        this.layout_height = height
        this.layout_width = width
    }



}