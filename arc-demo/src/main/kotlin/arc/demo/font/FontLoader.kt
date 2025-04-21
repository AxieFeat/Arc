package arc.demo.font

import arc.asset.FileAsset
import arc.demo.VoxelGame
import arc.font.Glyph
import arc.font.GlyphFont
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object FontLoader {

    private val gson = Gson()

    fun load(asset: FileAsset): GlyphFont {
        return GlyphFont.from(
            VoxelGame.application.renderSystem.drawer,
            loadGlyphsFromJson(asset.file)
        )
    }

    private fun loadGlyphsFromJson(file: File): List<Glyph> {
        val gson = Gson()

        val jsonString = file.readText()

        val type = object : TypeToken<List<GlyphJson>>() {}.type

        val glyphJsonList: List<GlyphJson> = gson.fromJson(jsonString, type)

        return glyphJsonList.map { json ->
            Glyph(
                char = json.character,
                name = json.name,
                codepoint = json.codepoint,
                pattern = json.pixels.map { row ->
                    row.map { it != 0 }
                }
            )
        }
    }

    private fun fixGlyphs(file: File) {
        val gson = Gson()
        val jsonString = file.readText()
        val type = object : TypeToken<List<GlyphJson>>() {}.type
        val glyphJsonList: MutableList<GlyphJson> = gson.fromJson(jsonString, type)

        var modified = false

        glyphJsonList.forEach { glyph ->
            val pixels = glyph.pixels
            if (pixels.isNotEmpty()) {
                val needsPadding = pixels.any { row ->
                    row.isNotEmpty() && row[0] != 0
                }

                if (needsPadding) {
                    pixels.forEach { row ->
                        row.add(0, 0)
                    }
                    modified = true
                }
            }
        }

        if (modified) {
            val updatedJson = gson.toJson(glyphJsonList)
            file.writeText(updatedJson)
        }
    }


    private data class GlyphJson(
        val character: Char,
        val name: String,
        val codepoint: Int,
        val pixels: MutableList<MutableList<Int>>
    )


}