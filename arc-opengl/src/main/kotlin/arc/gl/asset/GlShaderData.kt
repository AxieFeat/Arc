package arc.gl.asset

import arc.assets.shader.ShaderData
import arc.shader.BlendMode
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.File

internal data class GlShaderData(
    override val file: File
) : ShaderData {

    object Factory : ShaderData.Factory {
        override fun create(file: File): ShaderData {
            return GlShaderData(file)
        }
    }

    override val uniforms: List<String> = run {
        if(!file.exists()) return@run emptyList()

        val text = file.readText()

        val jsonObject = JsonParser.parseString(text).asJsonObject

        return@run jsonObject.getAsJsonArray("uniforms")
            .map { it.asString }
    }

    override val blendMode: BlendMode? = run {
        if(!file.exists()) return@run null

        val text = file.readText()

        val jsonObject = JsonParser.parseString(text).asJsonObject

        if (!jsonObject.has("blend")) return@run null

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

        if(flag) return@run BlendMode.create()

        return@run if(flag1) BlendMode.create(
            srcColorFactor = srcColorFactor,
            dstColorFactor = dstColorFactor,
            srcAlphaFactor = srcAlphaFactor,
            dstAlphaFactor = dstAlphaFactor,
            blendFunc = func
        ) else BlendMode.create(
            separateBlend = false,
            opaque = false,
            srcColorFactor = srcColorFactor,
            srcAlphaFactor = srcColorFactor,
            dstColorFactor = dstColorFactor,
            dstAlphaFactor = dstAlphaFactor,
            blendFunc = func
        )
    }

    companion object {
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