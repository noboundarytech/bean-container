package tech.noboundary.bean.container.support.definition

import tech.noboundary.bean.container.BeanContainer
import tech.noboundary.bean.container.support.definition.BeanPropertyValueType.BASIC_TYPE
import tech.noboundary.bean.container.support.definition.BeanPropertyValueType.RUNTIME_BEAN_REFERENCE_TYPE

class BeanPropertyValueConverter(private val container: BeanContainer) {

    fun convert(value: BeanPropertyValue): Any = when (value.type) {
        RUNTIME_BEAN_REFERENCE_TYPE -> this.container.getBean(value.value)
        BASIC_TYPE -> value.value
    }

}