package tech.noboundary.bean.container.support.definition

data class BeanConstructorArg(val index: Int,
                              val type: String,
                              val value: BeanPropertyValue)