package com.coffeetrainlabs.kmpplayground

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.coffeetrainlabs.kmpplayground.databinding.DetailScreenBinding
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailScreen(val value: String) : Screen {
  override fun layoutResId(): Int = R.layout.detail_screen
}

class DetailScreenView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
  init {
    orientation = VERTICAL
  }

  private val binding by viewBinding(DetailScreenBinding::bind)

  override fun onFinishInflate() {
    super.onFinishInflate()
    binding.myLabel.text = currentScreen<DetailScreen>().value.toString()
    binding.myAvatar.set(MyAvatarModel.Url(""))
  }
}
