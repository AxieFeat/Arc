package arc.demo.shader

import arc.asset.asFileAsset
import arc.files.classpath
import arc.shader.ShaderInstance
import arc.shader.ShaderSettings

object ShaderContainer {

    @JvmField
    val positionTex = ShaderInstance.of(
        vertexShader = classpath("arc/shader/position_tex/position_tex.vsh").asFileAsset(),
        fragmentShader = classpath("arc/shader/position_tex/position_tex.fsh").asFileAsset(),
        shaderSettings = ShaderSettings.of(classpath("arc/shader/position_tex/position_tex.json").asFileAsset()),
    ).also {
        it.compileShaders()
        it.addProvider(DefaultUniformProvider)
    }

    @JvmField
    val positionTexLight = ShaderInstance.of(
        vertexShader = classpath("arc/shader/position_tex_light/position_tex_light.vsh").asFileAsset(),
        fragmentShader = classpath("arc/shader/position_tex_light/position_tex_light.fsh").asFileAsset(),
        shaderSettings = ShaderSettings.of(classpath("arc/shader/position_tex_light/position_tex_light.json").asFileAsset()),
    ).also {
        it.compileShaders()
        it.addProvider(DefaultUniformProvider)
    }

    @JvmField
    val positionColor = ShaderInstance.of(
        vertexShader = classpath("arc/shader/position_color/position_color.vsh").asFileAsset(),
        fragmentShader = classpath("arc/shader/position_color/position_color.fsh").asFileAsset(),
        shaderSettings = ShaderSettings.of(classpath("arc/shader/position_color/position_color.json").asFileAsset()),
    ).also {
        it.compileShaders()
        it.addProvider(DefaultUniformProvider)
    }

    @JvmField
    val blitScreen = ShaderInstance.of(
        vertexShader = classpath("arc/shader/blit_screen/blit_screen.vsh").asFileAsset(),
        fragmentShader = classpath("arc/shader/blit_screen/blit_screen.fsh").asFileAsset(),
        shaderSettings = ShaderSettings.of(classpath("arc/shader/blit_screen/blit_screen.json").asFileAsset()),
    ).also {
        it.compileShaders()
        it.addProvider(DefaultUniformProvider)
    }

    @JvmField
    val blitPositionColor = ShaderInstance.of(
        vertexShader = classpath("arc/shader/blit_position_color/blit_position_color.vsh").asFileAsset(),
        fragmentShader = classpath("arc/shader/blit_position_color/blit_position_color.fsh").asFileAsset(),
        shaderSettings = ShaderSettings.of(classpath("arc/shader/blit_position_color/blit_position_color.json").asFileAsset()),
    ).also {
        it.compileShaders()
        it.addProvider(DefaultUniformProvider)
    }

}