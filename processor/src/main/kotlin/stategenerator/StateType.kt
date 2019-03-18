package com.github.rougsig.rxflux.processor.stategenerator

import com.github.rougsig.rxflux.processor.extensions.className
import com.github.rougsig.rxflux.processor.extensions.enclosedFields
import com.github.rougsig.rxflux.processor.extensions.enclosedMethods
import com.github.rougsig.rxflux.processor.extensions.error
import me.eugeniomarletti.kotlin.metadata.KotlinClassMetadata
import me.eugeniomarletti.kotlin.metadata.kotlinMetadata
import me.eugeniomarletti.kotlin.processing.KotlinProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

internal data class StateType(
  val fields: List<FieldType>,
  val stateElement: TypeElement,
  val stateName: String,
  val packageName: String
) {
  companion object {
    fun get(env: KotlinProcessingEnvironment, el: Element): StateType? {
      val typeMetadata = el.kotlinMetadata
      if (el !is TypeElement || typeMetadata !is KotlinClassMetadata) {
        env.error("@FluxState can't be applied to $el: must be kotlin class", el)
        return null
      }

      val fields = el.enclosedMethods.map { FieldType.get(env, it) }

      return StateType(
        fields = fields,
        stateElement = el,
        stateName = el.className.simpleName,
        packageName = el.className.packageName
      )
    }
  }
}
