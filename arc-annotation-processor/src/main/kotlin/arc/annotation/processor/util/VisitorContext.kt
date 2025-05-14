package arc.annotation.processor.util

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver

class VisitorContext(val resolver: Resolver, val logger: KSPLogger)

