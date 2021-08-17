package com.target.diff.configuration

import kotlin.reflect.KClass

internal class ConfigurationBuilder {

  internal val valueTypes = mutableListOf<KClass<*>>()

  fun addValueType(value: KClass<*>) {
    valueTypes.add(value)
  }

  fun addValueTypes(vararg classes: KClass<*>) {
    valueTypes.addAll(classes)
  }
}

internal fun configuration(init: ConfigurationBuilder.() -> Unit): IntelliDiffConfiguration {
  val builder = ConfigurationBuilder()
  builder.init()
  return object : IntelliDiffConfiguration() {
    override fun valueTypes(): List<KClass<*>> {
      return builder.valueTypes
    }
  }
}
