package arc.annotation.processor.util

import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.visitor.KSEmptyVisitor

open class ContextualVisitor : KSEmptyVisitor<VisitorContext, Unit>() {

    override fun defaultHandler(node: KSNode, data: VisitorContext) {
        // Nothing by default
    }

}
