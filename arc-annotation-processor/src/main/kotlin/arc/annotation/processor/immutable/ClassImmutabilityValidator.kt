package arc.annotation.processor.immutable

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.Modifier

object ClassImmutabilityValidator : ImmutabilityValidator {

    @OptIn(KspExperimental::class)
    override fun validateClass(type: KSClassDeclaration, resolver: Resolver) {
        if (type.classKind != ClassKind.CLASS) error("Expected class for record validation strategy, given $type!")
        if (!type.modifiers.contains(Modifier.DATA)) error("Expected type $type to be a data class!")
        if (!type.isAnnotationPresent(JvmRecord::class)) error("Expected JvmRecord annotation to be present for class type $type!")
    }

    override fun validateProperty(property: KSPropertyDeclaration, declaringType: KSClassDeclaration, resolver: Resolver) {
        // Nothing to do
    }

}
