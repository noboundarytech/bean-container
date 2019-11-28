package tech.noboundary.bean.container.support.aspect

import tech.noboundary.common.utils.ClassUtil
import org.aspectj.weaver.tools.PointcutParser
import org.aspectj.weaver.tools.PointcutPrimitive

abstract class MatchByExpressionAspect(order: Int = 1,
                                             expression: String)
    : BaseAspect(order,
        { method ->
            val pointcutParser = PointcutParser
                    .getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
                            setOf(PointcutPrimitive.EXECUTION), ClassUtil.getDefaultClassLoader())
            val pointcutExpression = pointcutParser.parsePointcutExpression("execution($expression)")
            val shadowMatch = pointcutExpression.matchesMethodExecution(method)
            shadowMatch.alwaysMatches()
        })