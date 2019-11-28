package tech.noboundary.bean.container

import org.junit.Assert.assertNotNull
import org.junit.Test
import tech.noboundary.bean.container.component.AutoInjectByConstructorBean
import tech.noboundary.bean.container.component.AutoInjectBySetterBean
import tech.noboundary.bean.container.support.DefaultBeanContainer

class BeanAutoScanTest {

    @Test
    fun testAutoScan() {
        val yamlConfigPath = "bean/auto-scan-bean.yaml"
        val container = DefaultBeanContainer(yamlConfigPath)
        val autoInjectByConstructorBean = container.getBean("autoInjectByConstructorBean")
                as AutoInjectByConstructorBean
        val autoInjectBySetterBean = container.getBean("autoInjectBySetterBean")
                as AutoInjectBySetterBean
        assertNotNull(autoInjectByConstructorBean.autoInjectDao)
        assertNotNull(autoInjectBySetterBean.autoInjectDao)
    }

}