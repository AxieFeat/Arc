package arc.demo.shader

import arc.assets.shader.FragmentShader
import arc.assets.shader.ShaderData
import arc.assets.shader.VertexShader
import arc.files.classpath
import arc.shader.ShaderInstance

object ShaderContainer {

    @JvmField
    val positionTex = ShaderInstance.of(
        vertexShader = VertexShader.from(classpath("arc/shader/position_tex/position_tex.vsh")),
        fragmentShader = FragmentShader.from(classpath("arc/shader/position_tex/position_tex.fsh")),
        shaderData = ShaderData.from(classpath("arc/shader/position_tex/position_tex.json")),
    ).also {
        it.compileShaders()
        it.addProvider(DefaultUniformProvider)
    }

    @JvmField
    val positionColor = ShaderInstance.of(
        vertexShader = VertexShader.from(classpath("arc/shader/position_color/position_color.vsh")),
        fragmentShader = FragmentShader.from(classpath("arc/shader/position_color/position_color.fsh")),
        shaderData = ShaderData.from(classpath("arc/shader/position_color/position_color.json")),
    ).also {
        it.compileShaders()
        it.addProvider(DefaultUniformProvider)
    }

    @JvmField
    val blitScreen = ShaderInstance.of(
        vertexShader = VertexShader.from(classpath("arc/shader/blit_screen/blit_screen.vsh")),
        fragmentShader = FragmentShader.from(classpath("arc/shader/blit_screen/blit_screen.fsh")),
        shaderData = ShaderData.from(classpath("arc/shader/blit_screen/blit_screen.json")),
    ).also {
        it.compileShaders()
        it.addProvider(DefaultUniformProvider)
    }

}