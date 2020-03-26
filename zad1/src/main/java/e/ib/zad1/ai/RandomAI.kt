package e.ib.zad1.ai

import e.ib.zad1.Item
import java.util.*

//losowe AI, może sprawiać problem, lub wręcz przeciwnie
class RandomAI(override val BOARD: Array<Array<Item>>, override val AImarker : Item, override val playerMarker: Item) : AI() {

    private val r = Random()

    override fun costMap(): Array<Array<Int>> {
        val output = arrayOf(
            arrayOf(0, 0, 0),arrayOf(0, 0, 0),arrayOf(0, 0, 0)
        )
        val emptyMap = this.mapWhereEmpty()
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                output[i][j] = if (emptyMap[i][j]) r.nextInt(10) else -5
            }
        }
        return output
    }
}