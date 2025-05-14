package arc.annotation.processor.immutable

import arc.annotation.processor.util.ContextualVisitor
import arc.annotation.processor.util.VisitorContext
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

@OptIn(KspExperimental::class)
object ImmutabilityChecker : ContextualVisitor() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: VisitorContext) {
        val validator = when (classDeclaration.classKind) {
            ClassKind.CLASS -> ClassImmutabilityValidator
            ClassKind.INTERFACE -> InterfaceImmutabilityValidator
            else -> return
        }
        validator.validateClass(classDeclaration, data.resolver)
        classDeclaration.getDeclaredProperties().map { visitProperty(it, classDeclaration, data, validator) }
    }

    private fun visitProperty(property: KSPropertyDeclaration, type: KSClassDeclaration, context: VisitorContext, validator: ImmutabilityValidator) {
        if (property.isMutable) {
            error("Property ${property.simpleName.asString()} in immutable type ${type.simpleName.asString()} is mutable!")
        }

        validator.validateProperty(property, type, context.resolver)
    }

}
