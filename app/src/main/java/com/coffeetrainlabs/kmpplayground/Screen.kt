package com.coffeetrainlabs.kmpplayground

import android.os.Parcelable
import androidx.annotation.LayoutRes

interface Screen : Parcelable {
  @LayoutRes
  fun layoutResId(): Int
}
