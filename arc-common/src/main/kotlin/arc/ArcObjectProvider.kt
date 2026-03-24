package arc

import arc.asset.AssetStack
import arc.asset.FileAsset
import arc.asset.MutableAssetStack
import arc.asset.RuntimeAsset
import arc.asset.SimpleAssetStack
import arc.asset.SimpleFileAsset
import arc.asset.SimpleRuntimeAsset
import arc.graphics.JomlCamera
import arc.graphics.NativeDrawBuffer
import arc.graphics.Camera
import arc.graphics.DrawBuffer
import arc.graphics.vertex.SimpleVertexFormat
import arc.graphics.vertex.SimpleVertexFormatElement
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.math.AABB
import arc.math.JomlAABB
import arc.math.JomlRay
import arc.math.Ray
import arc.shader.SimpleShaderSettings
import arc.shader.ShaderSettings
import arc.util.SimpleColor
import arc.util.Color
import arc.util.provider.ObjectProvider
import arc.util.provider.TypeNotFoundException
import arc.util.provider.register
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import java.lang.reflect.Field

@Suppress("UNCHECKED_CAST")
object ArcObjectProvider : ObjectProvider {

    private val singletons = Object2ObjectOpenHashMap<Class<*>, Any>()
    private val factories = Object2ObjectOpenHashMap<Class<*>, () -> Any>()

    override fun <T> provideSingle(type: Class<T>): T {
        return singletons[type] as? T ?: throw TypeNotFoundException("Type $type has no object registered!")
    }

    override fun <T> provideFactory(type: Class<T>): T {
        return (factories[type] ?: throw TypeNotFoundException("Type $type has no factory registered!")).invoke() as T
    }

    override fun <T> register(type: Class<T>, obj: T, overwrite: Boolean) {
        require(overwrite || (!singletons.containsKey(type) && !factories.containsKey(type))) {
            "Duplicate registration for type $type!"
        }
        singletons[type] = obj
    }

    override fun <T> register(type: Class<T>, factory: () -> T, overwrite: Boolean) {
        require(overwrite || (!singletons.containsKey(type) && !factories.containsKey(type))) {
            "Duplicate registration for type $type!"
        }
        factories[type] = factory as (() -> Any)
    }

    @JvmStatic
    fun install() {
        modifyField(Arc::class.java, "objectProvider", this)
    }

    @JvmStatic
    fun bootstrap() {
        register<Ray.Factory>(JomlRay.Factory)
        register<AABB.Factory>(JomlAABB.Factory)

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
        getField(clazz, name).set(null, value)
    }

    private fun getField(clazz: Class<*>, name: String): Field {
        return clazz.getDeclaredField(name).apply { isAccessible = true }
    }
}
