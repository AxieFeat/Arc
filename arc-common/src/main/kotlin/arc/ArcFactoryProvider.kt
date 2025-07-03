package arc

import arc.asset.*
import arc.graphics.JomlCamera
import arc.graphics.NativeDrawBuffer
import arc.graphics.Camera
import arc.graphics.DrawBuffer
import arc.graphics.vertex.SimpleVertexFormat
import arc.graphics.vertex.SimpleVertexFormatElement
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.math.*
import arc.shader.SimpleShaderSettings
import arc.shader.ShaderSettings
import arc.util.SimpleColor
import arc.util.Color
import arc.util.factory.FactoryProvider
import arc.util.factory.TypeNotFoundException
import arc.util.factory.register
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import java.lang.reflect.Field

object ArcFactoryProvider : FactoryProvider {

    private val singletons = Object2ObjectOpenHashMap<Class<*>, Any>()
    private val providers = Object2ObjectOpenHashMap<Class<*>, () -> Any>()

    @Suppress("UNCHECKED_CAST")
    override fun <T> provide(type: Class<T>): T {
        providers[type]?.let { return it.invoke() as T }

        return singletons[type] as? T ?: throw TypeNotFoundException("Type $type has no factory registered!")
    }

    override fun <T> register(type: Class<T>, factory: T, overwrite: Boolean) {
        require(overwrite || (!singletons.containsKey(type) && !providers.containsKey(type))) {
            "Duplicate registration for type $type!"
        }
        singletons[type] = factory
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> register(type: Class<T>, provider: () -> T, overwrite: Boolean) {
        require(overwrite || (!singletons.containsKey(type) && !providers.containsKey(type))) {
            "Duplicate registration for type $type!"
        }
        providers[type] = provider as (() -> Any)
    }

    @JvmStatic
    fun install() {
        modifyField(Arc::class.java, "factoryProvider", this)
    }

    @JvmStatic
    fun bootstrap() {
        register<Point2i.Factory>(SimplePoint2i.Factory)
        register<Point3i.Factory>(SimplePoint3i.Factory)
        register<Point2d.Factory>(SimplePoint2d.Factory)
        register<Point3d.Factory>(SimplePoint3d.Factory)

        register<AABB.Factory>(SimpleAABB.Factory)
        register<Ray.Factory>(SimpleRay.Factory)

        register<Vec2f.Factory>(SimpleVec2f.Factory)
        register<Vec3f.Factory>(SimpleVec3f.Factory)

        register<Color.Factory>(SimpleColor.Factory)
        register<Camera.Factory>(JomlCamera.Factory)

        register<VertexFormat.Builder>({ SimpleVertexFormat.Builder() })
        register<VertexFormatElement.Factory>(SimpleVertexFormatElement.Factory)
        register<DrawBuffer.Factory>(NativeDrawBuffer.Factory)
        register<ShaderSettings.Factory>(SimpleShaderSettings.Factory)

        register<RuntimeAsset.Factory>(SimpleRuntimeAsset.Factory)
        register<FileAsset.Factory>(SimpleFileAsset.Factory)
        register<AssetStack.Factory>(SimpleAssetStack.Factory)
        register<MutableAssetStack.Factory>(SimpleAssetStack.MutableFactory)
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
