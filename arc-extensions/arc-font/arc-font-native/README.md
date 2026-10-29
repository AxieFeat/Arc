# Native font provider

`NativeFontProvider.of()` creates a thread-owned, explicitly supplied `FontProvider`.
`load(id, ttfOrOtfBytes, faceIndex = 0, version = 0)` keeps its own native copy of the font data until cleanup.
The provider preserves the existing bitmap `GlyphFont` API.

```kotlin
import arc.font.FontDirection
import arc.font.NativeFontProvider

NativeFontProvider.of().use { provider ->
    val face = provider.load(
        id = "default",
        data = fontBytes,
        version = 1
    )
    val run = face.shape(
        text = "Hello",
        size = 16f,
        direction = FontDirection.LTR,
        language = "en"
    )

    for (glyph in run.glyphs) {
        val coverage = face.rasterize(
            glyphId = glyph.glyphId,
            size = run.size,
            pixelsPerUnit = 1.25f
        )
        // Upload or copy coverage while the provider remains alive.
    }
}
```

The example assumes `fontBytes` contains a complete font file. Raster snapshots remain
valid after provider cleanup; retain the provider while shaping or rasterizing more glyphs.

`FontFace.shape` uses HarfBuzz and UTF-16 source clusters; `metrics` uses FreeType's
typographic metrics; `rasterize` renders those same glyph IDs into grayscale coverage.
Rasterization can use an independent pixels-per-logical-unit scale. Shape coordinates
are logical units, Y down; raster bearings and dimensions are physical pixels.

HarfBuzz shapes one directional/script run. Paragraph bidi ordering, font fallback,
grapheme/line breaking, alignment and overflow belong to a higher-level text layout
service. HarfBuzz shaping alone does not provide those facilities.

Call `close` on the provider's creating thread after all consumers stop using its
faces. Faces and returned font bytes are provider-owned; shaped run data and raster
coverage are managed immutable snapshots. Cleanup is idempotent. Live font identities
cannot be reloaded. Use a new identity, or recreate the provider and increment the nonnegative
version when replacing bytes under the same ID. Higher-level FontFamily references must use
that version; invalidate old layouts and caches. A distinct text-service provider identity also
prevents a new renderer from silently accepting an old layout with unrelated font resources.

LWJGL 3.4.2's FreeType native library embeds and exports HarfBuzz. The implementation
sets `Configuration.HARFBUZZ_LIBRARY_NAME` to `FreeType.getLibrary()` before using
HarfBuzz. Load this provider before independently initializing another HarfBuzz native.
The standalone HarfBuzz native classifier is unnecessary and does not export `hb_ft_*`.

Dependencies are `lwjgl`, `lwjgl-freetype`, and `lwjgl-harfbuzz`, with matching LWJGL
core/FreeType native classifiers. Packaged classifiers do not establish runtime support
on untested operating systems.

Licenses: LWJGL uses BSD 3-clause, HarfBuzz uses the Old MIT license, and FreeType is
used under the FreeType License (FTL). Include the bundled dependency notices when
redistributing. This software uses the FreeType project (https://freetype.org/).
The test font is unmodified Noto Sans Regular under SIL OFL 1.1; its license is in
`src/test/resources/fonts/OFL.txt`. Source commit and SHA-256 are recorded in the
test fixture's adjacent `SOURCE.md`.

Primary API and packaging references:

- [LWJGL Configuration](https://javadoc.lwjgl.org/org/lwjgl/system/Configuration.html)
- [HarfBuzz FreeType integration](https://harfbuzz.github.io/integration-freetype.html)
- [LWJGL FreeType bindings](https://javadoc.lwjgl.org/org/lwjgl/util/freetype/FreeType.html)
- [LWJGL HarfBuzz bindings](https://javadoc.lwjgl.org/org/lwjgl/util/harfbuzz/HarfBuzz.html)
- [FreeType licensing](https://freetype.org/license)
