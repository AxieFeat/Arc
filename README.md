# Arc Engine

Русский | English

Игровой движок для создания 3D/2D игр, который не привязан
к конкретной реализации. То есть его можно использовать практически с любой технологией и платформой

#### Плюсы Arc:
1. Современное API на Kotlin
2. Нет привязки к конкретной реализации
3. Готовые реализации движка под OpenGL и Vulkan.
4. Легковесность и простота. Для создания игры необходимо лишь пару строчек кода и умение создавать шейдеры
5. Несколько реализаций движка в одной игре*

\* Только одна из реализаций может использоваться в runtime

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
    val shader: ShaderInstance = getShaderInstance()
    shader.compileShaders()

    val renderSystem: RenderSystem = application.renderSystem

    // Создадим буфер с нашими вершинами.
    val buffer: DrawBuffer = createBuffer(application)

    while (true) {
        // Начинаем кадр рендера
        renderSystem.beginFrame()

        shader.bind() // Устанавливаем шейдер в текущем контексте
        
        application.renderSystem.drawer.draw(buffer) // Рисуем наш треугольник

        shader.unbind()

        // Заканчиваем кадр
        renderSystem.endFrame()
    }
}

private fun getShaderInstance(): ShaderInstance {
    val vertex: File = classpath("arc/shader/example.vsh")
    val fragment: File = classpath("arc/shader/example.fsh")
    val uniform: File = classpath("arc/shader/example.json")

    return ShaderInstance.of(
        VertexShader.from(vertex),
        FragmentShader.from(fragment),
        ShaderData.from(uniform)
    )
}

private fun createBuffer(application: Application): DrawBuffer {
    val buffer: DrawBuffer = application.drawer.begin(
        DrawerMode.TRIANGLES,
        VertexFormat.builder()
            .add(VertexFormatElement.POSITION)
            .add(VertexFormatElement.COLOR)
            .build()
    )

    buffer.addVertex(x0, y0, z).setColor(Color.RED)
    buffer.addVertex(x0, y1, z).setColor(Color.YELLOW)
    buffer.addVertex(x1, y1, z).setColor(Color.GREEN)
    buffer.end()

    return buffer
}
```

## Что следует знать

arc-annotations - Утилитарный модуль, содержит лишь аннотации, которые могут помочь понять API лучше.

arc-core - Главная API движка, она представляет собой интерфейсы, которые в последствии реализуются другими 
модулями. Важно понимать, что API в этом модуле должна быть обобщённой и подходить под любую технологию рендера,
к тому же вы НЕ можете делать нейминг в этом модуле, который будет отсылать на какую-то конкретную реализацию.

arc-common - Базовая реализация arc-core, которая содержит общие для всех технологий рендера функции. Так например там
реализована математика, окно и звук. Если вы создаёте класс, который не является абстрактным и служит только чтобы,
реализовать интерфейс из arc-core - делайте его internal. Здесь так же не желательно использовать нейминг,
который так или иначе будет отсылать на какую-то конкретную реализацию.

arc-opengl - Реализация движка, использующая OpenGL. В этом модуле желательно писать код,
который будет относиться только к OpenGL. В этом модуле следует использовать нейминг,
который отсылает на то, что классы относятся к OpenGL (Например приставка Gl в начале названия классов). Так же, по возможности,
все классы должны быть internal.

arc-vulkan - Реализация движка, использующая Vulkan. В этом модуле желательно писать код,
который будет относиться только к Vulkan. В этом модуле следует использовать нейминг,
который отсылает на то, что классы относятся к Vulkan (Например приставка Vk в начале названия классов). Так же, по возможности,
все классы должны быть internal.

arc-extensions - Различные расширения для движка, например профайлер. По возможности используйте только интерфейсы, для
описания API, а реализацию пишите в отдельном модуле или в arc-common/opengl/vulkan.

arc-demo - Демонстрация движка, здесь можно писать абсолютно любой код для тестов и не только.
