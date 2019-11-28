package tech.noboundary.bean.container.support.annotation

@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
// todo: 自定义bean_id
annotation class AutoInject