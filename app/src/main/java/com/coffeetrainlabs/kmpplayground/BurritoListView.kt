package com.coffeetrainlabs.kmpplayground

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import chat.quill.data.FooProvider
import flow.Flow
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class BurritoListView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {
  private val burritoAdapter = BurritoAdapter(context)
  init {
    adapter = burritoAdapter
    layoutManager = LinearLayoutManager(context)
    isNestedScrollingEnabled = true
  }

  private val fooProvider = FooProvider()

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    observeUntilDetach(immediateMainThread) {
      fooProvider.observe()
        .collect { foos ->
          burritoAdapter.set(foos)
          if (foos.size == 3) {
            Timber.d("HI I'm CRASHING ${foos[4]}")
          }
        }
    }
  }

  inner class BurritoAdapter(private val context: Context) : RecyclerView.Adapter<ViewHolder>() {
    private var items = listOf<String>()

    fun set(foos: List<String>) {
      items = foos
      notifyDataSetChanged()
    }

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
      fun set(value: String) {
        textView.text = value
        textView.setOnClickListener {
          if (value == "Add") {
            fooProvider.onClick()
          } else {
            Flow.get(textView).set(DetailScreen(value))
          }
        }
      }
    }
  }
}
