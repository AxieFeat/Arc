package arc.graphics

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.math.Point3d
import org.jetbrains.annotations.ApiStatus
import org.joml.Matrix4f
import org.joml.Quaternionf

@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Camera {

    @get:JvmName("windowHeight")
    val windowHeight: Float

    @get:JvmName("windowWidth")
    val windowWidth: Float

    @get:JvmName("view")
    var view: Matrix4f

    @get:JvmName("projection")
    var projection: Matrix4f

    @get:JvmName("position")
    var position: Point3d

    @get:JvmName("rotation")
    var rotation: Quaternionf

    @get:JvmName("fov")
    var fov: Float

    @get:JvmName("zNear")
    var zNear: Float

    @get:JvmName("zFar")
    var zFar: Float

    fun lookAt(target: Point3d)

    fun rotate(yaw: Float, pitch: Float, roll: Float)

    fun update()

    fun updateAspect(height: Int, width: Int)

    fun reset()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {
        fun create(fov: Float, height: Float, width: Float): Camera
    }

    companion object {

        @JvmStatic
        fun create(fov: Float, height: Float, width: Float): Camera {
            return Arc.factory<Factory>().create(fov, height, width)
        }

    }

}