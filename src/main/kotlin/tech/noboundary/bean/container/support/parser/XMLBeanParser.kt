package tech.noboundary.bean.container.support.parser

import tech.noboundary.bean.container.support.aspect.AbstractAspect
import tech.noboundary.bean.container.support.definition.BeanConstructorArg
import tech.noboundary.bean.container.support.definition.BeanDefinition
import tech.noboundary.bean.container.support.definition.BeanProperty
import tech.noboundary.bean.container.support.definition.BeanPropertyValue
import tech.noboundary.bean.container.support.definition.BeanPropertyValueType.BASIC_TYPE
import tech.noboundary.bean.container.support.definition.BeanPropertyValueType.RUNTIME_BEAN_REFERENCE_TYPE
import tech.noboundary.bean.container.support.exception.BeanDefinitionException
import tech.noboundary.bean.core.resource.Resource
import tech.noboundary.common.utils.StringUtil
import org.apache.commons.lang3.StringUtils
import org.dom4j.Element
import org.dom4j.io.SAXReader

class XMLBeanParser : BeanParser {

    override fun parseBeanDefinition(resource: Resource): Set<BeanDefinition> {
        checkResource(resource)
        val containerElement = resource.getInputStream().use { SAXReader().read(it).rootElement }
        val definitions = mutableSetOf<BeanDefinition>()
        parseAutoScans(containerElement, definitions)
        parseBeans(containerElement, definitions)
        return definitions
    }

    override fun parseAspect(resource: Resource): Set<AbstractAspect> {
        checkResource(resource)
        val containerElement = resource.getInputStream().use { SAXReader().read(it).rootElement }
        return scanAspect(getPackages(containerElement))
    }

    private fun parseAutoScans(containerElement: Element, definitions: MutableSet<BeanDefinition>) =
            scanBeanDefinition(getPackages(containerElement)).forEach { definitions.add(it) }

    private fun getPackages(containerElement: Element) =
            containerElement.element(autoScansKey)?.elements(scanKey)
                    ?.map { (it as Element).attributeValue(packageKey) }?.toList() ?: emptyList()

    private fun parseBeans(containerElement: Element, definitions: MutableSet<BeanDefinition>) {
        containerElement.element(beansKey).elements(beanKey).map { beanElement ->
            beanElement as Element
            val id = beanElement.attributeValue(idKey)
            val clazz = beanElement.attributeValue(classKey)
            val isSingletonStr = beanElement.attributeValue(isSingletonKey)
            val isSingleton = if (StringUtils.isEmpty(isSingletonStr)) true else isSingletonStr.toBoolean()
            val definition = BeanDefinition(id, clazz, isSingleton)
            parseConstructorArgs(beanElement, definition)
            parseProperties(beanElement, definition)
            return@map definition
        }.forEach { definitions.add(it) }
    }

    private fun parseConstructorArgs(element: Element, definition: BeanDefinition) {
        val iterator = element.elementIterator(constructorArgKey)
        while (iterator.hasNext()) {
            val argElement = iterator.next() as Element
            val indexStr = argElement.attributeValue(indexKey)
            if (indexStr.isNullOrBlank()) {
                throw BeanDefinitionException("Tag 'constructor-arg' must have a 'index' attribute")
            }
            if (!StringUtil.isDigit(indexStr)) {
                throw BeanDefinitionException("The 'index' attribute of Tag 'constructor-arg' must be beDigit")
            }
            val index = indexStr.toInt()
            val type = argElement.attributeValue(typeKey)
            if (type.isNullOrBlank()) {
                throw BeanDefinitionException("Tag 'constructor-arg' must have a 'type' attribute")
            }
            val constructorArgValue = beanConstructorArgValueOf(argElement, index)
            val constructorArg = BeanConstructorArg(index, type, constructorArgValue)
            definition.constructorArgs.add(constructorArg)
        }
        definition.constructorArgs.sortBy { it.index }
    }

    private fun beanConstructorArgValueOf(propElement: Element, index: Int) =
            beanValueOf(propElement, "<constructor-arg> element for index '$index'")

    private fun parseProperties(element: Element, definition: BeanDefinition) {
        val iterator = element.elementIterator(propertyKey)
        while (iterator.hasNext()) {
            val propElement = iterator.next() as Element
            val propertyName = propElement.attributeValue(nameKey)
            if (propertyName.isNullOrBlank()) {
                throw BeanDefinitionException("Tag 'property' must have a 'name' attribute")
            }
            val propertyValue = beanPropertyValueOf(propElement, propertyName)
            val property = BeanProperty(propertyName, propertyValue)
            definition.properties.add(property)
        }
    }

    private fun beanPropertyValueOf(propElement: Element, propertyName: String) =
            beanValueOf(propElement, "<property> element for property '$propertyName'")

    private fun beanValueOf(propElement: Element, elementDesc: String): BeanPropertyValue {
        val isRefAttr = propElement.attribute(refKey) != null
        val isValueAttr = propElement.attribute(valueKey) != null
        return when {
            isRefAttr -> {
                val refName = propElement.attributeValue(refKey)
                if (refName.isNullOrBlank()) {
                    throw BeanDefinitionException("$elementDesc contains beEmpty 'ref' attribute")
                }
                BeanPropertyValue(refName, RUNTIME_BEAN_REFERENCE_TYPE)
            }
            isValueAttr -> {
                BeanPropertyValue(propElement.attributeValue(valueKey), BASIC_TYPE)
            }
            else -> throw BeanDefinitionException("$elementDesc must specify a ref or value")
        }
    }

}