package tech.noboundary.bean.container.component

import tech.noboundary.bean.container.dao.AccountDao
import tech.noboundary.bean.container.dao.ItemDao

class ConstructorBean(val accountDao: AccountDao,
                      val itemDao: ItemDao,
                      val version: Int) {

    constructor(accountDao: AccountDao, itemDao: ItemDao)
            : this(accountDao, itemDao, -1)
}