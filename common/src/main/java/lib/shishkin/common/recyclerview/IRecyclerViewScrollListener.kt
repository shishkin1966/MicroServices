package lib.shishkin.common.recyclerview

import androidx.recyclerview.widget.RecyclerView

interface IRecyclerViewScrollListener {

    fun idle(recyclerView: RecyclerView)

    fun scrolled(recyclerView: RecyclerView)
}
