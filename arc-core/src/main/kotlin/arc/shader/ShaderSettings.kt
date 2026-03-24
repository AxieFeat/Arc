package arc.shader

import arc.Arc.single
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.asset.StringAsset
import com.google.gson.JsonParser
import org.jetbrains.annotations.ApiStatus

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
    @ApiStatus.Internal
    interface Factory {

        fun create(uniforms: List<String>): ShaderSettings
    }

    companion object {

        /**
         * Create instance of [ShaderSettings].
         *
         * @param uniforms Uniforms for shaders.
         * @param blendMode Blending mode for shaders.
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
            val jsonObject = JsonParser.parseString(asset.text).asJsonObject

            val uniforms = run {
                if(!jsonObject.has("uniforms")) return@run emptyList()

                return@run jsonObject.getAsJsonArray("uniforms")
                    .map { it.asString }
            }

            return of(uniforms)
        }
    }
}
