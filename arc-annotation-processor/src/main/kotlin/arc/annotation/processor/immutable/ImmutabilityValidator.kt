package arc.annotation.processor.immutable

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

interface ImmutabilityValidator {

    fun validateClass(type: KSClassDeclaration, resolver: Resolver)

    fun validateProperty(property: KSPropertyDeclaration, declaringType: KSClassDeclaration, resolver: Resolver)

}
