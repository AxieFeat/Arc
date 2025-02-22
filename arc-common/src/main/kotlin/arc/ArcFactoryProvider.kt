package arc

import arc.asset.ArcAssetStack
import arc.asset.ArcSoundAsset
import arc.asset.shader.ArcFragmentShader
import arc.asset.shader.ArcVertexShader
import arc.assets.AssetStack
import arc.assets.MutableAssetStack
import arc.assets.SoundAsset
import arc.assets.shader.FragmentShader
import arc.assets.shader.VertexShader
import arc.math.*
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
        register<Point2i.Factory>(ArcPoint2I.Factory)
        register<Point3i.Factory>(ArcPoint3I.Factory)

        // Vectors
        register<Vec2f.Factory>(ArcVec2f.Factory)
        register<Vec3f.Factory>(ArcVec3f.Factory)

        // Misc
        register<Color.Factory>(ArcColor.Factory)
        register<Window.Factory>(ArcWindow.Factory)
        register<Application.Factory>(ArcApplicationFactory)
        register<Configuration.Factory>(ArcConfiguration.Factory)

        // Assets
        register<SoundAsset.Factory>(ArcSoundAsset.Factory)
        register<FragmentShader.Factory>(ArcFragmentShader.Factory)
        register<VertexShader.Factory>(ArcVertexShader.Factory)
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