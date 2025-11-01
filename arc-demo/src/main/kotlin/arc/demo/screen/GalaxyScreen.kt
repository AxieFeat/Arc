package arc.demo.screen

import arc.asset.asRuntimeAsset
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.input.mouse
import arc.shader.ShaderInstance
import org.joml.Vector2f
import org.joml.Vector3f

object GalaxyScreen : Screen("galaxy") {

    private val drawer = application.renderSystem.drawer

    val shader = ShaderInstance.of(
        vertexShader = """
            #version 300 es

            in vec3 Position;
            in vec2 uv;
            
            out vec2 vUv;
            
            void main() {
                vUv = uv;
                gl_Position = vec4(Position, 1.0);
            }
        """.trimIndent().asRuntimeAsset(),
        fragmentShader = """
            #version 300 es

            precision highp float;

            uniform float uTime;
            uniform vec3 uResolution;
            uniform vec2 uFocal;
            uniform vec2 uRotation;
            uniform float uStarSpeed;
            uniform float uDensity;
            uniform float uHueShift;
            uniform float uSpeed;
            uniform vec2 uMouse;
            uniform float uGlowIntensity;
            uniform float uSaturation;
            uniform bool uMouseRepulsion;
            uniform float uTwinkleIntensity;
            uniform float uRotationSpeed;
            uniform float uRepulsionStrength;
            uniform float uMouseActiveFactor;
            uniform float uAutoCenterRepulsion;
            uniform bool uTransparent;
            
            in vec2 vUv;
            
            #define NUM_LAYER 4.0
            #define STAR_COLOR_CUTOFF 0.2
            #define MAT45 mat2(0.7071, -0.7071, 0.7071, 0.7071)
            #define PERIOD 3.0
            
            out vec4 FragColor; 
            
            float Hash21(vec2 p) {
              p = fract(p * vec2(123.34, 456.21));
              p += dot(p, p + 45.32);
              return fract(p.x * p.y);
            }
            
            float tri(float x) {
              return abs(fract(x) * 2.0 - 1.0);
            }
            
            float tris(float x) {
              float t = fract(x);
              return 1.0 - smoothstep(0.0, 1.0, abs(2.0 * t - 1.0));
            }
            
            float trisn(float x) {
              float t = fract(x);
              return 2.0 * (1.0 - smoothstep(0.0, 1.0, abs(2.0 * t - 1.0))) - 1.0;
            }
            
            vec3 hsv2rgb(vec3 c) {
              vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
              vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
              return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
            }
            
            float Star(vec2 uv, float flare) {
              float d = length(uv);
              float m = (0.05 * uGlowIntensity) / d;
              float rays = smoothstep(0.0, 1.0, 1.0 - abs(uv.x * uv.y * 1000.0));
              m += rays * flare * uGlowIntensity;
              uv *= MAT45;
              rays = smoothstep(0.0, 1.0, 1.0 - abs(uv.x * uv.y * 1000.0));
              m += rays * 0.3 * flare * uGlowIntensity;
              m *= smoothstep(1.0, 0.2, d);
              return m;
            }
            
            vec3 StarLayer(vec2 uv) {
              vec3 col = vec3(0.0);
            
              vec2 gv = fract(uv) - 0.5; 
              vec2 id = floor(uv);
            
              for (int y = -1; y <= 1; y++) {
                for (int x = -1; x <= 1; x++) {
                  vec2 offset = vec2(float(x), float(y));
                  vec2 si = id + vec2(float(x), float(y));
                  float seed = Hash21(si);
                  float size = fract(seed * 345.32);
                  float glossLocal = tri(uStarSpeed / (PERIOD * seed + 1.0));
                  float flareSize = smoothstep(0.9, 1.0, size) * glossLocal;
            
                  float red = smoothstep(STAR_COLOR_CUTOFF, 1.0, Hash21(si + 1.0)) + STAR_COLOR_CUTOFF;
                  float blu = smoothstep(STAR_COLOR_CUTOFF, 1.0, Hash21(si + 3.0)) + STAR_COLOR_CUTOFF;
                  float grn = min(red, blu) * seed;
                  vec3 base = vec3(red, grn, blu);
                  
                  float hue = atan(base.g - base.r, base.b - base.r) / (2.0 * 3.14159) + 0.5;
                  hue = fract(hue + uHueShift / 360.0);
                  float sat = length(base - vec3(dot(base, vec3(0.299, 0.587, 0.114)))) * uSaturation;
                  float val = max(max(base.r, base.g), base.b);
                  base = hsv2rgb(vec3(hue, sat, val));
            
                  vec2 pad = vec2(tris(seed * 34.0 + uTime * uSpeed / 10.0), tris(seed * 38.0 + uTime * uSpeed / 30.0)) - 0.5;
            
                  float star = Star(gv - offset - pad, flareSize);
                  vec3 color = base;
            
                  float twinkle = trisn(uTime * uSpeed + seed * 6.2831) * 0.5 + 1.0;
                  twinkle = mix(1.0, twinkle, uTwinkleIntensity);
                  star *= twinkle;
                  
                  col += star * size * color;
                }
              }
            
              return col;
            }
            
            void main() {
              vec2 focalPx = uFocal * uResolution.xy;
              vec2 uv = (vUv * uResolution.xy - focalPx) / uResolution.y;
            
              vec2 mouseNorm = uMouse - vec2(0.5);
              
              if (uAutoCenterRepulsion > 0.0) {
                vec2 centerUV = vec2(0.0, 0.0); // Center in UV space
                float centerDist = length(uv - centerUV);
                vec2 repulsion = normalize(uv - centerUV) * (uAutoCenterRepulsion / (centerDist + 0.1));
                uv += repulsion * 0.05;
              } else if (uMouseRepulsion) {
                vec2 mousePosUV = (uMouse * uResolution.xy - focalPx) / uResolution.y;
                float mouseDist = length(uv - mousePosUV);
                vec2 repulsion = normalize(uv - mousePosUV) * (uRepulsionStrength / (mouseDist + 0.1));
                uv += repulsion * 0.05 * uMouseActiveFactor;
              } else {
                vec2 mouseOffset = mouseNorm * 0.1 * uMouseActiveFactor;
                uv += mouseOffset;
              }
            
              float autoRotAngle = uTime * uRotationSpeed;
              mat2 autoRot = mat2(cos(autoRotAngle), -sin(autoRotAngle), sin(autoRotAngle), cos(autoRotAngle));
              uv = autoRot * uv;
            
              uv = mat2(uRotation.x, -uRotation.y, uRotation.y, uRotation.x) * uv;
            
              vec3 col = vec3(0.0);
            
              for (float i = 0.0; i < 1.0; i += 1.0 / NUM_LAYER) {
                float depth = fract(i + uStarSpeed * uSpeed);
                float scale = mix(20.0 * uDensity, 0.5 * uDensity, depth);
                float fade = depth * smoothstep(1.0, 0.9, depth);
                col += StarLayer(uv * scale + i * 453.32) * fade;
              }
            
              if (uTransparent) {
                float alpha = length(col);
                alpha = smoothstep(0.0, 0.3, alpha); // Enhance contrast
                alpha = min(alpha, 1.0); // Clamp to maximum 1.0
                FragColor = vec4(col, alpha);
              } else {
                FragColor = vec4(col, 1.0);
              }
            }
        """.trimIndent().asRuntimeAsset()
    ).also { it.compileShaders() }

    val buffer = drawer.begin(
        DrawerMode.TRIANGLES,
        VertexFormat.builder()
            .add(VertexFormatElement.POSITION)
            .add(VertexFormatElement.UV)
            .build()
    ).use { buffer ->
        buffer.addVertex(-1f, 1f, 0f).setTexture(0f, 1f)
        buffer.addVertex(-1f, -1f, 0f).setTexture(0f, 0f)
        buffer.addVertex(1f, -1f, 0f).setTexture(1f, 0f)

        buffer.addVertex(-1f, 1f, 0f).setTexture(0f, 1f)
        buffer.addVertex(1f, -1f, 0f).setTexture(1f, 0f)
        buffer.addVertex(1f, 1f, 0f).setTexture(1f, 1f)

        buffer.build()
    }

    private val start = System.currentTimeMillis()

    override fun doRender() {
        shader.bind()

        val width = application.window.width.toFloat()
        val height = application.window.height.toFloat()

        val time = (System.currentTimeMillis() - start) / 1000f * 0.5f

        val mousePosition = Vector2f(application.mouse.position)
        mousePosition.set(mousePosition.x / width, 1f - mousePosition.y / height)

        shader.setUniform("uTime", time)
        shader.setUniform("uResolution", Vector3f(width, height, width / height))
        shader.setUniform("uFocal", Vector2f(0.5f, 0.5f))
        shader.setUniform("uRotation", Vector2f(1.0f, 0.0f))
        shader.setUniform("uStarSpeed", 0.5f)
        shader.setUniform("uDensity", 2f)
        shader.setUniform("uHueShift", 240f)
        shader.setUniform("uSpeed", 1f)
        shader.setUniform("uMouse", mousePosition)
        shader.setUniform("uGlowIntensity", 0.3f)
        shader.setUniform("uSaturation", 0.6f)
        shader.setUniform("uMouseRepulsion", true)
        shader.setUniform("uTwinkleIntensity", 0.3f)
        shader.setUniform("uRotationSpeed", 0.1f)
        shader.setUniform("uRepulsionStrength", 2f)
        shader.setUniform("uMouseActiveFactor", 1f)
        shader.setUniform("uAutoCenterRepulsion", 0f)
        shader.setUniform("uTransparent", true)

        drawer.draw(buffer)

        shader.unbind()
    }

    override fun onFpsUpdate(fps: Int) {
        name = "FPS: $fps, Frame time: $frameTime ms"
    }

}