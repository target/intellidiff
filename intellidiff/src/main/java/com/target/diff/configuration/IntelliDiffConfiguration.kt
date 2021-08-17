package com.target.diff.configuration

import kotlin.reflect.KClass

abstract class IntelliDiffConfiguration {

  open fun valueTypes(): List<KClass<*>> {
    return emptyList()
  }

  companion object {

    fun default(): IntelliDiffConfiguration {
      return configuration {
      }
    }
  }
}
