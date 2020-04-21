package com.coffeetrainlabs.kmpplayground

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

val mainThread = CoroutineScope(Dispatchers.Main)
val ioThread = CoroutineScope(Dispatchers.IO)
