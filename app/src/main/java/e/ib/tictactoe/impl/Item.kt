package e.ib.tictactoe.impl

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

enum class Item(val value: Int) {

    O(1), X(-1), EMPTY(0);


    override fun toString(): String {
        return if (this != EMPTY) super.toString()
        else " "
    }

    companion object CREATOR : Creator<Item> {
        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }

        override fun createFromParcel(source: Parcel?): Item {
            val _value = source?.readInt()
            for (item in values()) {
                if (item.value == _value) {
                    return item
                }
            }
            return EMPTY
        }

    }

}
