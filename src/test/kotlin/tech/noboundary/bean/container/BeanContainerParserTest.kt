package tech.noboundary.bean.container

import tech.noboundary.bean.container.service.CustomService
import org.junit.Assert.*
import org.junit.Test

class BeanContainerParserTest : BaseTest() {

    @Test
    fun testParser() {
        testParser(containerOfYAML!!)
        testParser(containerOfXML!!)
    }

    private fun testParser(container: BeanContainer) {
        val beanDef = container.getBeanDefinition(beanId)
        assertEquals(beanId, beanDef.id)
        assertEquals(beanClassName, beanDef.beanClassName)
        assertTrue(beanDef.isSingleton)
        val beanNotSingletonDef = container.getBeanDefinition(notSingletonBeanId)
        assertFalse(beanNotSingletonDef.isSingleton)
    }

    @Test
    fun testGetBean() {
        containerOfYAML!!.getBean(beanId) as CustomService
        containerOfXML!!.getBean(beanId) as CustomService
    }

}