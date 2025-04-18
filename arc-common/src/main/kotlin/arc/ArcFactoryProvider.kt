package arc

import arc.asset.*
import arc.graphics.ArcCamera
import arc.graphics.ArcDrawBuffer
import arc.graphics.Camera
import arc.graphics.DrawBuffer
import arc.graphics.vertex.ArcVertexFormat
import arc.graphics.vertex.ArcVertexFormatElement
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.math.*
import arc.shader.ArcShaderSettings
import arc.shader.ShaderSettings
import arc.util.ArcColor
import arc.util.Color
import arc.util.factory.FactoryProvider
import arc.util.factory.TypeNotFoundException
import arc.util.factory.register
import arc.window.ArcWindow
import arc.window.Window
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
        // Оригинальные регистрации — синглтоны
        register<Point2i.Factory>(ArcPoint2i.Factory)
        register<Point3i.Factory>(ArcPoint3i.Factory)
        register<Point2d.Factory>(ArcPoint2d.Factory)
        register<Point3d.Factory>(ArcPoint3d.Factory)

        register<AABB.Factory>(ArcAABB.Factory)
        register<Ray.Factory>(ArcRay.Factory)

        register<Vec2f.Factory>(ArcVec2f.Factory)
        register<Vec3f.Factory>(ArcVec3f.Factory)

        register<Color.Factory>(ArcColor.Factory)
        register<Window.Factory>(ArcWindow.Factory)
        register<Configuration.Factory>(ArcConfiguration.Factory)
        register<Camera.Factory>(ArcCamera.Factory)

        register<VertexFormat.BuilderFactory>(ArcVertexFormat.BuilderFactory)
        register<VertexFormatElement.Factory>(ArcVertexFormatElement.Factory)
        register<DrawBuffer.Factory>(ArcDrawBuffer.Factory)
        register<ShaderSettings.Factory>(ArcShaderSettings.Factory)

        register<RuntimeAsset.Factory>(ArcRuntimeAsset.Factory)
        register<FileAsset.Factory>(ArcFileAsset.Factory)
        register<AssetStack.Factory>(ArcAssetStack.Factory)
        register<MutableAssetStack.Factory>(ArcAssetStack.MutableFactory)
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
