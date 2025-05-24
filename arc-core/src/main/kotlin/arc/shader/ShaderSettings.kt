package arc.shader

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.asset.StringAsset
import com.google.gson.JsonObject
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

    /**
     * Blending settings.
     */
    val blendMode: BlendMode

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(uniforms: List<String>, blendMode: BlendMode): ShaderSettings

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
            uniforms: List<String> = listOf(),
            blendMode: BlendMode = BlendMode.of()
        ): ShaderSettings {
            return Arc.factory<Factory>().create(uniforms, blendMode)
        }

        /**
         * Deserialize [ShaderSettings] from JSON file.
         *
         * @param asset Asset with text in JSON format.
         *
         * @return New instance of [ShaderSettings].
         */
        @JvmStatic
        fun of(asset: StringAsset): ShaderSettings {

            val jsonObject = JsonParser.parseString(asset.text).asJsonObject

            val uniforms = run {
                if(!jsonObject.has("uniforms")) return@run emptyList()

                return@run jsonObject.getAsJsonArray("uniforms")
                    .map { it.asString }
            }

            val blendMode = run {
                if (!jsonObject.has("blend")) return@run BlendMode.of()

                val blend = jsonObject.getAsJsonObject("blend")

                val func = stringToBlendFunc(blend.getAsJsonPrimitive("func").asString)
                var flag = true
                var flag1 = true
                val srcColorFactor = blend.getFactor("srcrgb", 1) {
                    if(this != 1) {
                        flag = false
                    }
                }
                val srcAlphaFactor = blend.getFactor("srcalpha", 0) {
                    if(this != 1) {
                        flag = false
                    }

                    flag1 = true
                }
                val dstColorFactor = blend.getFactor("dstrgb", 0) {
                    if(this != 0) {
                        flag = false
                    }
                }
                val dstAlphaFactor = blend.getFactor("dstalpha", 0) {
                    if(this != 0) {
                        flag = false
                    }

                    flag1 = true
                }

                if(flag) return@run BlendMode.of()

                return@run if(flag1) BlendMode.of(
                    srcColorFactor = srcColorFactor,
                    dstColorFactor = dstColorFactor,
                    srcAlphaFactor = srcAlphaFactor,
                    dstAlphaFactor = dstAlphaFactor,
                    blendFunc = func
                ) else BlendMode.of(
                    separateBlend = false,
                    opaque = false,
                    srcColorFactor = srcColorFactor,
                    srcAlphaFactor = srcColorFactor,
                    dstColorFactor = dstColorFactor,
                    dstAlphaFactor = dstAlphaFactor,
                    blendFunc = func
                )
            }

            return of(uniforms, blendMode)
        }

        private fun stringToBlendFunc(name: String): Int {
            return when(name.trim { it <= ' ' }.lowercase()) {
                "add" -> 32774
                "subtract" -> 32778
                "reversesubtract", "reverse_subtract" -> 32779
                "min" -> 32775
                "max" -> 32776

                else -> 32774
            }
        }

        private fun stringToBlendFactor(name: String): Int {
            return when (
                name.trim { it <= ' ' }.lowercase()
                    .replace("_".toRegex(), "")
                    .replace("one".toRegex(), "1")
                    .replace("zero".toRegex(), "0")
                    .replace("minus".toRegex(), "-")
            ) {
                "0" -> 0
                "1" -> 1
                "srccolor" -> 768
                "1-srccolor" -> 769
                "srcalpha" -> 770
                "1-srcalpha" -> 771
                "dstalpha" -> 772
                "1-dstalpha" -> 773

                else -> -1
            }
        }

        private fun JsonObject.getFactor(key: String, default: Int, execute: Int.() -> Unit): Int {
            if(this.get("key") == null) return default

            val result = stringToBlendFactor(this.getAsJsonPrimitive(key).asString)

            execute.invoke(result)

            return result
        }

    }

}