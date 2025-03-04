# Arc Engine

Русский | English

Игровой движок для создания 3D/2D игр, который не привязан
к конкретной реализации. То есть его можно использовать практически с любой технологией и платформой

#### Плюсы Arc:
1. Современное API на Kotlin
2. Нет привязки к конкретной реализации
3. Поддержка Vulkan и OpenGL
4. Легковесность и простота. Для создания игры необходимо лишь пару строчек кода и умение создавать шейдеры
5. Две графические API в одной игре*

\* Только одна графическая API может использоваться в runtime

#### Минусы Arc:
1. Отсутствие поддержки. Не надейтесь на частые обновления и своевременные фиксы багов
2. Нет гарантии, что код будет полностью кросс-платформенным
3. Нет прямого доступа к низкоуровневым методам

Простейший пример использования Arc:

```kotlin
fun main() {
    
    // Предзагружаем OpenGL.
    GlApplication.preload()
    
    // Находим приложение в текущем контексте (Это будет OpenGL реализация)
    val application: Application = Application.find()
    application.init() // Инициализируем все системы. Это так же создаст окно.

    // Получаем шейдер и компилируем его
    val shader: ShaderInstance = getShaderInstance(application.locationSpace)
    shader.compileShaders()

    val renderSystem: RenderSystem = application.renderSystem

    while (true) {
        // Начинаем кадр рендера
        renderSystem.beginFrame()

        shader.bind() // Устанавливаем шейдер в текущем контексте
        
        renderSystem.setShaderColor(1f, 1f, 1f, 1f) // Устанавливаем цвет шейдера

        val buffer: DrawBuffer = createBuffer(application)
        application.renderSystem.drawer.draw(buffer) // Рисуем наш квадрат

        shader.unbind()

        // Заканчиваем кадр
        renderSystem.endFrame()
    }
}

private fun getShaderInstance(locationSpace: LocationSpace): ShaderInstance {
    val vertex: File = classpath("arc/shader/example.vsh")
    val fragment: File = classpath("arc/shader/example.fsh")
    val uniform: File = classpath("arc/shader/example.json")

    return ShaderInstance.of(
        VertexShader.from(vertex),
        FragmentShader.from(fragment),
        ShaderUniforms.from(UniformAsset.from(uniform))
    )
}

private fun createBuffer(application: Application): DrawBuffer {
    val buffer: DrawBuffer = application.drawer.begin(
        DrawerMode.QUADS,
        VertexFormat.builder()
            .add(VertexFormatElement.POSITION)
            .add(VertexFormatElement.COLOR)
            .build()
    )

    // Пример, который нарисует красный квадрат.
    quad(buffer, 0, 0, 1920, 1080, 1, Color.RED)

    return buffer
}

private fun quad(buffer: DrawBuffer, x0: Int, y0: Int, x1: Int, y1: Int, z: Int, color: Color) {
    // Устанавлием каждую точку квадрата и её цвет. 
    
    buffer.addVertex(x0, y0, z).setColor(color)
    buffer.addVertex(x0, y1, z).setColor(color)
    buffer.addVertex(x1, y1, z).setColor(color)
    buffer.addVertex(x1, y0, z).setColor(color)
    buffer.end()
}

class Handler : WindowHandler {
    // Можем реализовать любой метод интерфейса для обработки ивентов.
}
```