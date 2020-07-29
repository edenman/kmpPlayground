package com.coffeetrainlabs.kmpplayground

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import flow.Flow

class BurritoListView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {
  init {
    adapter = BurritoAdapter(context)
    layoutManager = LinearLayoutManager(context)
    isNestedScrollingEnabled = true
  }

  class BurritoAdapter(private val context: Context) : RecyclerView.Adapter<ViewHolder>() {
    private val items = (0..80).toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val textView = TextView(context)
      textView.minHeight = 120
      textView.setPadding(40, 40, 40, 40)
      textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
      return RowViewHolder(textView)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      (holder as RowViewHolder).set(items[position])
    }

    inner class RowViewHolder(private val textView: TextView) : ViewHolder(textView) {
      fun set(value: Int) {
        textView.text = value.toString()
        textView.tag = value
        textView.setOnClickListener {
          Flow.get(textView).set(DetailScreen(value))
        }
      }
    }
  }
}
