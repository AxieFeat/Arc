package arc.graphics

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.graphics.vertex.VertexConsumer
import org.jetbrains.annotations.ApiStatus

@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Scene {

    @get:JvmName("drawer")
    val drawer: Drawer

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

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {
        fun create(drawer: Drawer): Scene
    }

    companion object {

        @JvmStatic
        fun create(drawer: Drawer): Scene = Arc.factory<Factory>().create(drawer)

    }

}