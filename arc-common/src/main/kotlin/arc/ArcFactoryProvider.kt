package arc

import arc.asset.*
import arc.asset.ArcAssetStack
import arc.asset.ArcRuntimeAsset
import arc.audio.OggSound
import arc.audio.Sound
import arc.graphics.*
import arc.graphics.ArcCamera
import arc.graphics.vertex.ArcVertexFormat
import arc.graphics.vertex.ArcVertexFormatElement
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.lwamodel.ArcLwaModel
import arc.lwamodel.LwaModel
import arc.lwamodel.animation.*
import arc.lwamodel.animation.ArcLwamAnimation
import arc.lwamodel.cube.ArcLwamCube
import arc.lwamodel.cube.ArcLwamCubeFace
import arc.lwamodel.cube.LwamCube
import arc.lwamodel.cube.LwamCubeFace
import arc.lwamodel.group.ArcLwamElementGroup
import arc.lwamodel.group.LwamElementGroup
import arc.lwamodel.texture.ArcLwamTexture
import arc.lwamodel.texture.LwamTexture
import arc.math.*
import arc.profiler.ArcProfiler
import arc.profiler.Profiler
import arc.shader.ArcShaderSettings
import arc.shader.ShaderSettings
import arc.util.ArcColor
import arc.util.Color
import arc.util.factory.FactoryProvider
import arc.util.factory.TypeNotFoundException
import arc.window.ArcWindow
import arc.window.Window
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import java.lang.reflect.Field

object ArcFactoryProvider : FactoryProvider {

    private val factories = Object2ObjectOpenHashMap<Class<*>, Any>()

    @Suppress("UNCHECKED_CAST")
    override fun <T> provide(type: Class<T>): T = factories[type] as? T ?: throw TypeNotFoundException("Type $type has no factory registered!")

    override fun <T> register(type: Class<T>, factory: T, overwrite: Boolean) {
        require(!factories.contains(type) && overwrite) { "Duplicate registration for type $type!" }
        factories[type] = factory
    }

    @JvmStatic
    fun install() {
        modifyField(Arc::class.java, "factoryProvider", ArcFactoryProvider)
    }

    @JvmStatic
    fun bootstrap() {
        // Points
        register<Point2i.Factory>(ArcPoint2i.Factory)
        register<Point3i.Factory>(ArcPoint3i.Factory)
        register<Point2d.Factory>(ArcPoint2d.Factory)
        register<Point3d.Factory>(ArcPoint3d.Factory)

        register<AABB.Factory>(ArcAABB.Factory)
        register<Ray.Factory>(ArcRay.Factory)

        // Vectors
        register<Vec2f.Factory>(ArcVec2f.Factory)
        register<Vec3f.Factory>(ArcVec3f.Factory)

        // Misc
        register<Color.Factory>(ArcColor.Factory)
        register<Window.Factory>(ArcWindow.Factory)
        register<Configuration.Factory>(ArcConfiguration.Factory)
        register<Camera.Factory>(ArcCamera.Factory)
        register<Sound.Factory>(OggSound.Factory)

        register<VertexFormat.BuilderFactory>(ArcVertexFormat.BuilderFactory)
        register<VertexFormatElement.Factory>(ArcVertexFormatElement.Factory)
        register<DrawBuffer.Factory>(ArcDrawBuffer.Factory)
        register<ShaderSettings.Factory>(ArcShaderSettings.Factory)

        // Assets
        register<RuntimeAsset.Factory>(ArcRuntimeAsset.Factory)
        register<FileAsset.Factory>(ArcFileAsset.Factory)
        register<AssetStack.Factory>(ArcAssetStack.Factory)
        register<MutableAssetStack.Factory>(ArcAssetStack.MutableFactory)


        // Extensions

        // Profiler
        register<Profiler.Factory>(ArcProfiler.Factory)

        // Model
        register<LwaModel.Factory>(ArcLwaModel.Factory)
        register<LwamCube.Factory>(ArcLwamCube.Factory)
        register<LwamCubeFace.Factory>(ArcLwamCubeFace.Factory)
        register<LwamTexture.Factory>(ArcLwamTexture.Factory)
        register<LwamElementGroup.Factory>(ArcLwamElementGroup.Factory)
        register<LwamAnimation.Factory>(ArcLwamAnimation.Factory)
        register<LwamAnimator.Factory>(ArcLwamAnimator.Factory)
        register<LwamKeyframe.Factory>(ArcLwamKeyframe.Factory)
    }

    @Suppress("SameParameterValue")
    private fun modifyField(clazz: Class<*>, name: String, value: Any) {
        try {
            getField(clazz, name).set(null, value)
        } catch (exception: Exception) {
            throw exception
        }
    }

    private fun getField(clazz: Class<*>, name: String): Field {
        try {
            return clazz.getDeclaredField(name).apply { isAccessible = true }
        } catch (exception: Exception) {
            throw exception
        }
    }

}