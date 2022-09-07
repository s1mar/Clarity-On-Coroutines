package com.clarity.android.interview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clarity.android.interview.ItemAdapter.ViewHolder
import kotlinx.android.synthetic.main.item_row.view.*
import java.util.ArrayList

class ItemAdapter : RecyclerView.Adapter<ViewHolder>() {

  private val items = ArrayList<ItemRow>()

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(row: ItemRow) {
        itemView.txtView.text = row.name
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    val view = LayoutInflater
        .from(parent.context)
        .inflate(R.layout.item_row, parent, false)

    return ViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: ViewHolder,
    position: Int
  ) {
    holder.bind(items[position])
  }

  override fun getItemCount(): Int {
    return items.size
  }

  fun update(newItems: List<ItemRow>) {
    items.clear()
    items.addAll(newItems)
    notifyDataSetChanged()
  }
}
