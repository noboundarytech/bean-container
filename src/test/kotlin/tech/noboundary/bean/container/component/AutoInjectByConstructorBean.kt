package tech.noboundary.bean.container.component

import tech.noboundary.bean.container.dao.AutoInjectDao
import tech.noboundary.bean.container.support.annotation.AutoInject
import tech.noboundary.bean.container.support.annotation.Bean

@Bean
class AutoInjectByConstructorBean @AutoInject constructor(val autoInjectDao: AutoInjectDao)