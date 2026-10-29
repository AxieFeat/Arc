# Scoped graphics, targets and streaming migration

The public additions live in `arc.graphics`; they contain no UI types. `RenderContext.capabilities` reports scoped passes, scissor, typed state, render targets and stencil-mask support. Legacy implementations inherit explicit unsupported operations. Vulkan remains experimental: its existing drawer and empty state methods are not evidence of support.

```kotlin
import arc.graphics.BlendState
import arc.graphics.CullMode
import arc.graphics.DepthState
import arc.graphics.PixelRect

context.beginScopedPass(textureUnits = setOf(0)).use {
    context.setBlend(BlendState.PREMULTIPLIED_ALPHA)
    context.setDepth(DepthState())
    context.setCull(CullMode.NONE)
    context.setScissor(PixelRect(20, 30, 100, 50))
    // Draw without clearing or presenting.
}
```

Pixel rectangles use lower-left render-target pixels. Zero width or height rejects every fragment. Scoped passes close on the creating thread in reverse creation order; repeated close is harmless. They snapshot native state once at entry, allowing an application to mix existing Arc operations and raw graphics calls without a stale software state cache. Draw calls do not query the state. A null texture-unit set captures all units; a supplied set declares every unit the pass may modify, while also preserving the previously active unit. This declaration materially reduces driver queries. Arc's ordinary texture binding uses unit zero.

The OpenGL implementation restores draw/read targets, renderbuffer, program, VAO, array/element/uniform/pixel buffers, indexed uniform ranges, texture targets/samplers, active texture, viewport/scissor, blending, depth/cull/color writes, front/back stencil, polygon/rasterizer-discard/logic/sample/clip-distance state and relevant upload pixel-store state, including when the body throws. Resource object contents (uniform values, texture data and deleted resources) are not rolled back. Do not resize/delete resources referenced by an open snapshot. The context and resources remain thread/context bound.

`createRenderTarget(width, height, depthStencil)` creates owned RGBA8 storage and optionally a packed depth/stencil attachment. `colorTexture` is a borrowed attachment view; its cleanup does not delete the target. Resize requires an unbound target and discards contents; dimensions and attachment storage change together. Close the target before destroying its context when possible. `bindTarget` never clears. `clearTarget` is explicit, respects scissor/write masks and uses depth 1/stencil 0 when requested. Stencil masks require ownership of their target/bits; do not clear an application's stencil to implement an overlay.

`createSampler`/`bindSampler` separate filtering from borrowed texture parameters. Texture binding no longer forces nearest filtering on every bind; existing callers needing nearest filtering should bind a nearest sampler explicitly. `createRgbaTexture` copies top-row-first, tightly packed RGBA8 bytes, restores upload bindings/pixel-store state and releases temporary native upload memory. Returned textures are owned. `OpenGL.currentRenderContext()` supplies neutral services for an externally owned current context after `GL.createCapabilities`; polling/presentation remain with that host.

`DrawBuffer.bufferSize` retains its legacy allocation unit (four bytes), rather than pretending to count vertices. `activeBytes` is initialized vertex count times stride. Native buffers finalize only pending vertices, support empty/repeated builds, have per-instance scratch and idempotent cleanup. OpenGL/GLES vertex uploads honor input position/limit, upload active bytes, grow capacity, preserve the prior binding and orphan storage before writes. No unsynchronized in-flight overwrite or retained CPU buffer pointer is used. Uniform locations are cached after shader linking.

Legacy `FrameBuffer.render` now composites to the default target without an implicit color/depth clear, then restores prior state. Callers that depended on a clear must clear explicitly. Its geometry/sampler now belong to the framebuffer instance and release on cleanup; reported attachment dimensions follow resize. New integrations should prefer the scoped `RenderTarget` contract.

## Reproduction

Baseline was recorded at `419619a2ec7147e76d9b696af823d0ecdeb3e684`, branch `dev`; untracked `.idea/` and `backend/render/arc-vulkan/src/main/kotlin/arc/vk/vk.kt` were preserved. A forced offline compile of core/common/OpenGL/font-core/input-core passed with all 29 tasks executed. Empty font/input-core scripts receive their Arc dependency from `arc-extensions/build.gradle.kts`.

```powershell
.\gradlew.bat :arc-opengl:graphicsRegression :arc-opengles:compileKotlin --offline --no-daemon --console=plain
```

Verified on Windows 11/JDK 21.0.10/Gradle 9.6.1/Kotlin 2.4.10, OpenGL 4.1.0 NVIDIA 617.42/RTX 4060 Ti. The hidden GPU check validates active-byte readback, exceptional native state restoration, scene preservation, empty scissor, exact stencil-mask pixels, resize and cleanup. Full all-unit snapshots cost about 2 ms in an early 100-sample run; declared unit zero reduced that cost. These microtimings are environment dependent and are not a UI throughput result. GLES compiles, but native GLES/ANGLE execution remains unverified. Pixzel's companion graphics fixture additionally verifies two unshared OpenGL contexts with colliding resource IDs.

## Context generations

`RenderContext.resourceGeneration` is a live opaque context identity; `ContextResource.resourceGeneration` is the generation captured when a GPU wrapper was created. Zero means no current tracked context (or, for third-party resource implementations, no generation support). OpenGL/GLES identify the installed LWJGL capabilities object. Hosts must make their context current and install its capabilities; on context loss/destruction they must clear capabilities. Arc's Application close does so. Physical reset detection and reset notifications remain host responsibilities.

Owned targets, textures, samplers, vertex/vertex-array/uniform buffers and shader programs reject operations in a different or missing context. Cleanup remains idempotent and releases CPU state, but skips native deletion if the original context is not current. This prevents an old numeric name from deleting an unrelated object in a new, unshared context. Destruction of the old native context reclaims skipped native allocations. An open scoped pass cannot span a context replacement: close reports the change and discards the stale snapshot without issuing restoration calls. Close and cleanup still require their creating thread.

Generation identity is not a platform GPU-reset event stream and does not recreate application resources. Integrations should reopen their rendering session and rebuild owned resources from bounded CPU sources; borrowed resources require the application's explicit restoration hook. Returning to a still-live context requires reinstalling its original capabilities object to preserve that identity. The shared uploader caches a separate VAO for each live capabilities object rather than reusing another context's name.
`RasterState()` selects filled triangles and disables inherited rasterizer discard, color logic operations, sample coverage/masks and user clip distances. OpenGL snapshots/restores those enables, polygon mode, logic operation, sample coverage values/masks and polygon offset. Core contexts can report one polygon mode; compatibility contexts can report separate front/back values. GLES supports filled polygons, discard and its coverage/mask subset, and rejects unsupported requests. These controls allow an overlay to render even when the application was drawing wireframes or discarding primitives. `RenderCapabilities.maxTextureDimension` reports the actual current-device dimension limit when a context is available and refreshes on generation change.
