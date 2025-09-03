package arc.demo.screen

import arc.asset.asRuntimeAsset
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.input.mouse
import arc.shader.ShaderInstance
import org.joml.Vector2f
import org.joml.Vector3f

object PlasmaScreen : Screen("plasma") {

    private val drawer = application.renderSystem.drawer

    val shader = ShaderInstance.of(
        vertexShader = """
            #version 410

            layout (location = 0) in vec3 Position;
            
            void main(){
                gl_Position = vec4(Position, 1.0);
            }
        """.trimIndent().asRuntimeAsset(),
        fragmentShader = """
            #version 410

            precision highp float;
            uniform vec2 iResolution;
            uniform float iTime;
            uniform vec3 uCustomColor;
            uniform float uUseCustomColor;
            uniform float uSpeed;
            uniform float uDirection;
            uniform float uScale;
            uniform float uOpacity;
            uniform vec2 uMouse;
            uniform float uMouseInteractive;
            
            out vec4 FragColor; 
            
            void mainImage(out vec4 o, vec2 C) {
              vec2 center = iResolution.xy * 0.5;
              C = (C - center) / uScale + center;
              
              vec2 mouseOffset = (uMouse - center) * 0.0002;
              C += mouseOffset * length(C - center) * step(0.5, uMouseInteractive);
              
              float i, d, z, T = iTime * uSpeed * uDirection;
              vec3 O, p, S;
            
              for (vec2 r = iResolution.xy, Q; ++i < 60.; O += o.w/d*o.xyz) {
                p = z*normalize(vec3(C-.5*r,r.y)); 
                p.z -= 4.; 
                S = p;
                d = p.y-T;
                
                p.x += .4*(1.+p.y)*sin(d + p.x*0.1)*cos(.34*d + p.x*0.05); 
                Q = p.xz *= mat2(cos(p.y+vec4(0,11,33,0)-T)); 
                z+= d = abs(sqrt(length(Q*Q)) - .25*(5.+S.y))/3.+8e-4; 
                o = 1.+sin(S.y+p.z*.5+S.z-length(S-p)+vec4(2,1,0,8));
              }
              
              o.xyz = tanh(O/1e4);
            }
            
            bool finite1(float x){ return !(isnan(x) || isinf(x)); }
            vec3 sanitize(vec3 c){
              return vec3(
                finite1(c.r) ? c.r : 0.0,
                finite1(c.g) ? c.g : 0.0,
                finite1(c.b) ? c.b : 0.0
              );
            }
            
            void main() {
              vec4 o = vec4(0.0);
              mainImage(o, gl_FragCoord.xy);
              vec3 rgb = sanitize(o.rgb);
              
              float intensity = (rgb.r + rgb.g + rgb.b) / 3.0;
              vec3 customColor = intensity * uCustomColor;
              vec3 finalColor = mix(rgb, customColor, step(0.5, uUseCustomColor));
              
              float alpha = length(rgb) * uOpacity;
              FragColor = vec4(finalColor, alpha);
            }
        """.trimIndent().asRuntimeAsset()
    ).also { it.compileShaders() }

    val buffer = drawer.begin(
        DrawerMode.TRIANGLES,
        VertexFormat.builder()
            .add(VertexFormatElement.POSITION)
            .build()
    ).use { buffer ->
        buffer.addVertex(-1f, 1f, 0f)
        buffer.addVertex(-1f, -1f, 0f)
        buffer.addVertex(1f, -1f, 0f)

        buffer.addVertex(-1f, 1f, 0f)
        buffer.addVertex(1f, -1f, 0f)
        buffer.addVertex(1f, 1f, 0f)

        buffer.build()
    }

    private val start = System.currentTimeMillis()

    override fun doRender() {
        shader.bind()

        val width = application.window.width.toFloat()
        val height = application.window.height.toFloat()

        shader.setUniform("iResolution", Vector2f(width, height))
        shader.setUniform("iTime", (System.currentTimeMillis() - start) / 1000f * 0.5f)
        shader.setUniform("uCustomColor", Vector3f())
        shader.setUniform("uUseCustomColor", 0f)
        shader.setUniform("uSpeed", 1f)
        shader.setUniform("uDirection", 1f)
        shader.setUniform("uScale", 1f)
        shader.setUniform("uOpacity", 1f)
        shader.setUniform("uMouse", application.mouse.position)
        shader.setUniform("uMouseInteractive", 1f)

        drawer.draw(buffer)

        shader.unbind()
    }

    override fun onFpsUpdate(fps: Int) {
        name = "FPS: $fps, Frame time: $frameTime ms"
    }

}