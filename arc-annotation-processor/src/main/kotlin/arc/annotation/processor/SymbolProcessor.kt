package arc.annotation.processor

import arc.annotation.processor.factory.FactoryChecker
import arc.annotation.processor.immutable.ImmutabilityChecker
import arc.annotation.processor.util.VisitorContext
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSVisitor
import kotlin.reflect.KClass

class SymbolProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val context = VisitorContext(resolver, environment.logger)
        visitAllAnnotatedWith(ImmutableType::class, context, ImmutabilityChecker)
        visitAllAnnotatedWith(TypeFactory::class, context, FactoryChecker)
        return emptyList()
    }

    private fun visitAllAnnotatedWith(annotation: KClass<*>, context: VisitorContext, visitor: KSVisitor<VisitorContext, Unit>) {
        val symbols = context.resolver.getSymbolsWithAnnotation(annotation.qualifiedName!!)
        var empty = true
        symbols.forEach {
            empty = false
            it.accept(visitor, context)
        }
        if (empty) context.logger.warn("No classes annotated with ${annotation.qualifiedName} found.")
    }
}
