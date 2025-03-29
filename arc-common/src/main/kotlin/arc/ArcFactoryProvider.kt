package arc

import arc.asset.ArcAssetStack
import arc.asset.ArcSoundAsset
import arc.asset.shader.ArcFragmentShader
import arc.asset.shader.ArcVertexShader
import arc.asset.AssetStack
import arc.asset.MutableAssetStack
import arc.asset.SoundAsset
import arc.asset.shader.FragmentShader
import arc.asset.shader.VertexShader
import arc.audio.OggSound
import arc.audio.Sound
import arc.graphics.ArcCamera
import arc.graphics.Camera
import arc.graphics.vertex.ArcVertexFormat
import arc.graphics.vertex.ArcVertexFormatElement
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.lwamodel.ArcLWAModel
import arc.lwamodel.LWAModel
import arc.lwamodel.cube.ArcLWAModelCube
import arc.lwamodel.cube.ArcLWAModelCubeFace
import arc.lwamodel.cube.LWAModelCube
import arc.lwamodel.cube.LWAModelCubeFace
import arc.lwamodel.texture.ArcLWAModelTexture
import arc.lwamodel.texture.LWAModelTexture
import arc.math.*
import arc.profiler.ArcProfiler
import arc.profiler.Profiler
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

        // Assets
        register<SoundAsset.Factory>(ArcSoundAsset.Factory)
        register<FragmentShader.Factory>(ArcFragmentShader.Factory)
        register<VertexShader.Factory>(ArcVertexShader.Factory)
        register<AssetStack.Factory>(ArcAssetStack.Factory)
        register<MutableAssetStack.Factory>(ArcAssetStack.MutableFactory)


        // Extensions

        // Profiler
        register<Profiler.Factory>(ArcProfiler.Factory)

        // Model
        register<LWAModel.Factory>(ArcLWAModel.Factory)
        register<LWAModelCube.Factory>(ArcLWAModelCube.Factory)
        register<LWAModelCubeFace.Factory>(ArcLWAModelCubeFace.Factory)
        register<LWAModelTexture.Factory>(ArcLWAModelTexture.Factory)
    }

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