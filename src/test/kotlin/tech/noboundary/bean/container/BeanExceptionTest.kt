package tech.noboundary.bean.container

import tech.noboundary.bean.container.support.DefaultBeanContainer
import tech.noboundary.bean.container.support.exception.BeanDefinitionException
import tech.noboundary.bean.container.service.CustomService
import org.junit.Test

class BeanExceptionTest : BaseTest() {

    private val errXmlConfigPath = "XXX.xml"
    private val invalidBeanConfigPath = "bean/invalid-bean.xml"
    private val errBeanId = "invalidBean"

    @Test
    fun testBeanDefinitionExceptionCausedByClass() {
        thrown.expect(BeanDefinitionException::class.java)
        thrown.expectMessage("not found class com.lpcoder.agile.invalid.invalidBean")
        DefaultBeanContainer(invalidBeanConfigPath).getBean(errBeanId) as CustomService
    }

    @Test
    fun testBeanDefinitionExceptionCausedByConfFile() {
        thrown.expect(BeanDefinitionException::class.java)
        thrown.expectMessage("XXX.xml cannot be opened")
        DefaultBeanContainer(errXmlConfigPath).getBean(beanId) as CustomService
    }

}