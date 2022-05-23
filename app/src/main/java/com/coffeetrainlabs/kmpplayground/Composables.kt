package com.coffeetrainlabs.kmpplayground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Composabull(text: String = "Here is some text this\n\nhas some newlines") {
  Text(
    text = text,
    color = Color.White,
    modifier = Modifier.background(Color.Black),
    maxLines = 1,
    overflow = TextOverflow.Ellipsis
  )
}

@Preview(widthDp = 300)
@Composable
fun WithColumnPreview() {
  Column {
    Composabull()
  }
}

@Preview(widthDp = 300)
@Composable
fun WithoutColumnPreview() {
  Composabull()
}

@Preview
@Composable
fun OtherEllipsizePreview() {
  Column {
    Composabull("Here is a single line of text with no wrap")
    Composabull("Here is some text that will surely wrap after one line but no newlines")
  }
}
