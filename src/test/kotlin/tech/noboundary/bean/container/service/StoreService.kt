package tech.noboundary.bean.container.service

import tech.noboundary.bean.container.dao.AutoInjectDao
import tech.noboundary.bean.container.support.annotation.AutoInject
import tech.noboundary.bean.container.support.annotation.Bean

@Bean
class StoreService {

    @AutoInject
    var autoInjectDao: AutoInjectDao? = null

    fun placeOrder() = println("place order")

}