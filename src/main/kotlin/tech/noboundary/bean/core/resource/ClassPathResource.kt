package tech.noboundary.bean.core.resource

import tech.noboundary.bean.container.support.exception.BeanDefinitionException
import tech.noboundary.common.utils.ClassUtil
import java.io.InputStream

class ClassPathResource(override val filePath: String,
                        private var classLoader: ClassLoader)
    : Resource {

    constructor(path: String) : this(path, ClassUtil.getDefaultClassLoader())

    override fun getInputStream(): InputStream {
        return this.classLoader.getResourceAsStream(this.filePath)
                ?: throw BeanDefinitionException(filePath + " cannot be opened")
    }
}