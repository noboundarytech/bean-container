package tech.noboundary.bean.container.support.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Bean(val id: String = "", val isSingleton: Boolean = true)