package arc.shader

import arc.Arc.single
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.asset.StringAsset
import arc.annotations.InternalApi
import kotlin.jvm.JvmStatic
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * This interface represents some settings for the shader.
 */
@ImmutableType
interface ShaderSettings {

    /**
     * List of uniforms for shader.
     * You can use this list for providing uniforms to shader.
     */
    val uniforms: List<String>

    @TypeFactory
    @InternalApi
    interface Factory {

        fun create(uniforms: List<String>): ShaderSettings
    }

    companion object {

        /**
         * Create instance of [ShaderSettings].
         *
         * @param uniforms Uniforms for shaders.
         *
         * @return New instance of [ShaderSettings].
         */
        @JvmStatic
        fun of(
            uniforms: List<String> = listOf()
        ): ShaderSettings {
            return single<Factory>().create(uniforms)
        }

        /**
         * Deserialize [ShaderSettings] from JSON file.
         *
         * @param asset Asset with text in JSON format.
         *
         * @return New instance of [ShaderSettings].
         */
        // TODO Refactor this function... or remove it?
        @JvmStatic
        fun of(asset: StringAsset): ShaderSettings {
            val jsonObject = Json.parseToJsonElement(asset.text).jsonObject

            val uniforms = run {
                val uniforms = jsonObject["uniforms"] ?: return@run emptyList()

                return@run uniforms.jsonArray.map { it.jsonPrimitive.content }
            }

            return of(uniforms)
        }
    }
}
