package tech.noboundary.bean.container

import org.junit.Before
import org.junit.Rule
import org.junit.rules.ExpectedException
import tech.noboundary.bean.container.support.DefaultBeanContainer

open class BaseTest {

    @get:Rule
    val thrown: ExpectedException = ExpectedException.none()

    protected val notSingletonBeanId = "notSingletonBean"
    protected val beanId = "customService"
    protected val beanClassName = "tech.noboundary.bean.container.service.CustomService"

    protected var containerOfYAML: BeanContainer? = null
    protected var containerOfXML: BeanContainer? = null

    private val yamlConfigPath = "bean/agile-bean.yaml"
    private val xmlConfigPath = "bean/agile-bean.xml"

    @Before
    fun before() {
        containerOfYAML = DefaultBeanContainer(yamlConfigPath)
        containerOfXML = DefaultBeanContainer(xmlConfigPath)
    }

}