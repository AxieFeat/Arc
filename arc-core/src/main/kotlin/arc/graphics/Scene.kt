package arc.graphics

import arc.annotations.MutableType

@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Scene {

    @get:JvmName("drawer")
    val drawer: Drawer

    @get:JvmName("camera")
    val camera: Camera

    @get:JvmName("fps")
    val fps: Int

    @get:JvmName("delta")
    val delta: Float

    @get:JvmName("inUse")
    val inUse: Boolean

    var isSkipRender: Boolean

    @get:JvmName("showCursor")
    var showCursor: Boolean

    fun render()

}