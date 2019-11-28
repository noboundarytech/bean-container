package tech.noboundary.bean.container.service

import tech.noboundary.bean.container.dao.AccountDao
import tech.noboundary.bean.container.dao.ItemDao

class CustomService {
    var accountDao: AccountDao? = null
    var itemDao: ItemDao? = null
    var author: String? = null
}
