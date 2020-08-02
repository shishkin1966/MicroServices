package lib.shishkin.microservices.screen.net_xml

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lib.shishkin.common.recyclerview.AbsRecyclerViewAdapter
import lib.shishkin.microservices.R
import lib.shishkin.microservices.data.Valute


class ValCursRecyclerViewAdapter :
    AbsRecyclerViewAdapter<Valute, ValCursRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_valute,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val value: TextView = itemView.findViewById(R.id.value)

        fun bind(item: Valute) {
            name.text = item.name
            value.text = item.value
        }
    }

}
