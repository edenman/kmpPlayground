package com.coffeetrainlabs.kmpplayground

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.widget.FrameLayout
import coil.load
import com.coffeetrainlabs.kmpplayground.databinding.MyAvatarContentsBinding

sealed class MyAvatarModel {
  data class Url(val url: String) : MyAvatarModel()
  data class Letter(val letter: String) : MyAvatarModel()
}

class MyAvatarView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
  private val binding by viewBinding(MyAvatarContentsBinding::bind)
  init {
    inflate(context, R.layout.my_avatar_contents, this)
    binding.image.clipToOutline = true
  }

  fun set(model: MyAvatarModel) {
    when (model) {
      is MyAvatarModel.Url -> {
        binding.label.visibility = GONE
        // binding.image.backgroundTintList = ColorStateList.valueOf(getColor(R.color.error))
        // binding.image.load(model.avatarURL)
        binding.image.setImageDrawable(ColorDrawable(resources.getColor(R.color.white)))
      }
      is MyAvatarModel.Letter -> {
        binding.label.visibility = VISIBLE
        binding.label.text = model.letter
        val colorDrawable = ColorDrawable(resources.getColor(R.color.white))
        binding.image.load(colorDrawable)
      }
    }
  }
}
