package arc.gl.asset

import arc.assets.shader.ShaderData
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

}