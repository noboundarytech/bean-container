package tech.noboundary.bean.container.support.parser

import tech.noboundary.bean.container.support.annotation.AutoInject
import tech.noboundary.bean.container.support.annotation.Bean
import tech.noboundary.bean.container.support.aspect.AbstractAspect
import tech.noboundary.bean.container.support.definition.BeanConstructorArg
import tech.noboundary.bean.container.support.definition.BeanDefinition
import tech.noboundary.bean.container.support.definition.BeanProperty
import tech.noboundary.bean.container.support.definition.BeanPropertyValue
import tech.noboundary.bean.container.support.definition.BeanPropertyValueType.BASIC_TYPE
import tech.noboundary.bean.container.support.definition.BeanPropertyValueType.RUNTIME_BEAN_REFERENCE_TYPE
import tech.noboundary.common.check.must
import tech.noboundary.common.check.ruler.support.AnyRuler
import tech.noboundary.bean.core.resource.Resource
import tech.noboundary.common.utils.ClassUtil
import java.beans.Introspector
import java.lang.reflect.Modifier
import kotlin.streams.toList

interface BeanParser {
    fun parseBeanDefinition(resource: Resource): Set<BeanDefinition>
    fun parseAspect(resource: Resource): Set<AbstractAspect>
}

fun checkResource(resource: Resource) {
    resource must AnyRuler.notNull
    resource.getInputStream() must AnyRuler.notNull
}

fun scanBeanDefinition(packages: List<String>): Set<BeanDefinition> {
    val classes = packages.map { ClassUtil.getClassSet(it) }.flatMap { it }.toSet()
    return classes.stream().filter { it.isAnnotationPresent(Bean::class.java) }.map {
        val id = if (it.getAnnotation(Bean::class.java).id.isBlank()) {
            Introspector.decapitalize(it.simpleName)
        } else {
            it.getAnnotation(Bean::class.java).id
        }
        val className = it.name
        val isSingleton = it.getAnnotation(Bean::class.java).isSingleton
        val definition = BeanDefinition(id, className, isSingleton)
        definition.constructorArgs.addAll(parseConstructorInjectionInfo(it))
        definition.properties.addAll(parsePropertyInjectionInfo(it))
        definition
    }.toList().toSet()
}

fun scanAspect(packages: List<String>): Set<AbstractAspect> {
    val classes = packages.map { ClassUtil.getClassSet(it) }.flatMap { it }.toSet()
    return classes.stream().filter {
        AbstractAspect::class.java.isAssignableFrom(it)
                && it != AbstractAspect::class.java
                && !it.isInterface
                && !Modifier.isAbstract(it.modifiers)
    }.map { it.newInstance() as AbstractAspect }.toList().toSet()
}

private fun parseConstructorInjectionInfo(clazz: Class<*>): List<BeanConstructorArg> {
    val constructorToBeInjected = clazz.constructors.firstOrNull {
        it.isAnnotationPresent(AutoInject::class.java)
    } ?: return emptyList()
    return constructorToBeInjected.parameterTypes.mapIndexed { index, paramClazz ->
        val value = if (ClassUtil.isBasicType(paramClazz)) {
            BeanPropertyValue(ClassUtil.getBasicTypeDefaultValue(paramClazz).toString(), BASIC_TYPE)
        } else {
            BeanPropertyValue(Introspector.decapitalize(paramClazz.simpleName), RUNTIME_BEAN_REFERENCE_TYPE)
        }
        BeanConstructorArg(index, paramClazz.name, value)
    }.toList()
}

private fun parsePropertyInjectionInfo(clazz: Class<*>): List<BeanProperty> {
    val fieldsToBeInjected = clazz.declaredFields.filter {
        it.isAnnotationPresent(AutoInject::class.java)
    }.toList()
    if (fieldsToBeInjected.isEmpty()) {
        return emptyList()
    }
    return fieldsToBeInjected.map {
        val value = if (ClassUtil.isBasicType(it.type)) {
            BeanPropertyValue(ClassUtil.getBasicTypeDefaultValue(it.type).toString(), BASIC_TYPE)
        } else {
            BeanPropertyValue(Introspector.decapitalize(it.type.simpleName), RUNTIME_BEAN_REFERENCE_TYPE)
        }
        BeanProperty(it.name, value)
    }.toList()
}


val containerKey = "container"

val autoScansKey = "auto-scans"

val scanKey = "scan"

val packageKey = "package"

val beansKey = "beans"

val beanKey = "bean"

val idKey = "id"

val classKey = "class"

val isSingletonKey = "isSingleton"

val propertyKey = "property"

val nameKey = "name"

val refKey = "ref"

val valueKey = "value"

val constructorArgKey = "constructor-arg"

val indexKey = "index"

val typeKey = "type"