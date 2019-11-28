package tech.noboundary.bean.container.component

import tech.noboundary.bean.container.dao.AutoInjectDao
import tech.noboundary.bean.container.support.annotation.AutoInject
import tech.noboundary.bean.container.support.annotation.Bean

@Bean
class AutoInjectBySetterBean {

    @AutoInject
    var autoInjectDao: AutoInjectDao? = null
}