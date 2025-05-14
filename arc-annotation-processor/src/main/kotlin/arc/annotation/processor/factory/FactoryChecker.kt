package arc.annotation.processor.factory

import arc.annotation.processor.util.ContextualVisitor
import arc.annotation.processor.util.VisitorContext
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import org.jetbrains.annotations.ApiStatus

object FactoryChecker : ContextualVisitor() {

    @OptIn(KspExperimental::class)
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: VisitorContext) {
        val parent = classDeclaration.parentDeclaration
        if (parent == null || parent !is KSClassDeclaration) fail(classDeclaration, "Must be a member type")
        if (classDeclaration.classKind != ClassKind.INTERFACE) fail(classDeclaration, "Must be an interface")
        if (!classDeclaration.isAnnotationPresent(ApiStatus.Internal::class)) fail(classDeclaration, "Must be marked with @ApiStatus.Internal")
        if (classDeclaration.getDeclaredProperties().count() != 0) fail(classDeclaration, "Must not have properties")
        if (classDeclaration.getDeclaredFunctions().count() == 0) fail(classDeclaration, "Must have at least one factory function")
    }

    private fun fail(type: KSClassDeclaration, message: String) {
        error("Type factory ${type.simpleName.asString()} is invalid! $message!")
    }

}
