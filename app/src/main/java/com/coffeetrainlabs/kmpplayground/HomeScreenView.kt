package com.coffeetrainlabs.kmpplayground

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coffeetrainlabs.kmpplayground.databinding.DetailScreenBinding
import com.coffeetrainlabs.kmpplayground.databinding.HomeScreenBinding
import flow.Flow
import kotlinx.android.parcel.Parcelize

@Parcelize
object HomeScreen : Screen {
  override fun layoutResId() = R.layout.home_screen
}

class HomeScreenView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
  private val binding by viewBinding(HomeScreenBinding::bind)

  override fun onFinishInflate() {
    super.onFinishInflate()
    binding.recycler.adapter = HomeScreenAdapter(context)
    binding.recycler.layoutManager = LinearLayoutManager(context)
    binding.contentsFoo.error.text = "foo"
  }

  var clickedRowIdx: Int? = null

  val clickedRow: View?
    get() {
      return children.map { child -> child as? TextView }
        .filterNotNull()
        .find { textView -> textView.tag == clickedRowIdx }
    }

  inner class HomeScreenAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = (0..80).toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
      val textView = TextView(context)
      textView.minHeight = 120
      textView.setPadding(40, 40, 40, 40)
      textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
      return RowViewHolder(textView)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
      (holder as RowViewHolder).set(items[position])
    }

    inner class RowViewHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView) {
      fun set(value: Int) {
        textView.text = value.toString()
        textView.tag = value
        textView.setOnClickListener {
          clickedRowIdx = value
          Flow.get(textView).set(DetailScreen(value))
        }

      }
    }
  }
}
