package com.target.tools

fun attempt(block: () -> Unit): Throwable? {
  return try {
    block()
    null
  } catch (throwable: Throwable) {
    throwable
  }
}
