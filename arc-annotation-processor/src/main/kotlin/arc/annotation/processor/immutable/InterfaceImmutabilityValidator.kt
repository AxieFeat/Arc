package arc.annotation.processor.immutable

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference

object InterfaceImmutabilityValidator : ImmutabilityValidator {

    private val DEFAULT_BOOLEAN_REGEX = "is[A-Z].*".toRegex()

    override fun validateClass(type: KSClassDeclaration, resolver: Resolver) {
        if (type.classKind != ClassKind.INTERFACE) error("Expected interface for interface validation strategy, given $type!")
    }

    @OptIn(KspExperimental::class)
    override fun validateProperty(property: KSPropertyDeclaration, declaringType: KSClassDeclaration, resolver: Resolver) {
        val getter = property.getter ?: return
        val returnType = getter.returnType ?: return
        if (isBoolean(resolver, returnType) && property.simpleName.asString().matches(DEFAULT_BOOLEAN_REGEX)) return
        if (resolver.getJvmName(getter) != property.simpleName.asString()) {
            error("Expected JvmName for getter on property ${property.simpleName} in immutable type ${declaringType.simpleName} to be the same" +
                    " as the property name!")
        }
    }

    @JvmStatic
    private fun isBoolean(resolver: Resolver, type: KSTypeReference): Boolean = type.resolve().isAssignableFrom(resolver.builtIns.booleanType)

}
