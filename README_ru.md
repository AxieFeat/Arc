# Arc Engine

[English](README.md) | [Русский](README_ru.md)

Arc Engine - это гибкий 3D/2D игровой движок, разработанный с учетом независимости от реализации, что позволяет ему работать практически с любой технологией и платформой.

### Особенности
- Современный Kotlin API
- Архитектура, независимая от реализации
- Готовые к использованию реализации OpenGL и Vulkan
- Легкий и простой - создавайте игры с минимальным кодом и знанием шейдеров
- Несколько реализаций движка в одной игре*
- Обширная система расширений (Аудио, Ввод, Дисплей, Шрифт, Модель, Профилировщик)

\* Только одна реализация может использоваться во время выполнения

### Структура проекта
- **arc-core**: Основные интерфейсы API движка
- **arc-common**: Общая реализация основных функций (математика, окно и т.п.)
- **arc-opengl**: Реализация движка на основе OpenGL
- **arc-vulkan**: Реализация движка на основе Vulkan
- **arc-extensions**: Различные расширения движка
  - **arc-audio**: Звуковой движок с реализацией на OpenAL
  - **arc-display**: Виртуальные экраны
  - **arc-font**: Движок для рендера шрифтов
  - **arc-input**: Движок ввода с реализацией на GLFW
  - **arc-model**: Формат моделей LWAM
  - **arc-profiler**: Простой профайлер
- **arc-annotations**: Утилитарные аннотации для лучшего понимания API
- **arc-annotation-processor**: Процессор для аннотаций
- **arc-demo**: Демонстрационные приложения и примеры

### Требования
- JDK 21 или выше
- Любой язык для JVM (Крайне рекомендуется Kotlin)
- Видеокарта, совместимая с OpenGL/Vulkan

### Установка

1. Укажите репозиторий проекта Arc:
```kotlin
repositories {
    maven("https://maven.pkg.github.com/AxieFeat/Arc") {
        credentials {
            // GitHub Packages требует авторизации :(
            
            username = System.getenv("GITHUB_ACTOR") // Переменная окружения с вашим логином GitHub
            password = System.getenv("GITHUB_TOKEN") // Переменная с вашим токеном GitHub
        }
    }
}
```

2. Добавьте зависимость в ваш проект:
```kotlin
dependencies {
    // Хеш коммита можно взять из истории - https://github.com/AxieFeat/Arc/commits/master/
    
    // Если вы хотите использовать API в уже готовой игре.
    implementation("arc.engine:arc-core:<первые 7 символов хеша коммита>")
  
  
    // Или если вы хотите создать свою игру - выберите бэкенд-реализацию движка.
    implementation("arc.engine:arc-opengl:<первые 7 символов хеша коммита>") // Для OpenGL
    implementation("arc.engine:arc-vulkan:<первые 7 символов хеша коммита>") // Для Vulkan
}
```

3. Добавьте желаемые расширения:
```kotlin
dependencies {
    implementation("arc.engine:arc-input-glfw:<первые 7 символов хеша коммита>") // Для управления через GLFW.
    implementation("arc.engine:arc-audio-openal:<первые 7 символов хеша коммита>") // Для звуковой системы.
    // Добавьте другие расширения по необходимости
}
```

### Пример базового использования
Вот простой пример, который отображает цветной треугольник:

```kotlin
// Примечание: Необходимые импорты из пакетов arc.* опущены для краткости
// Полный пример смотрите в модуле arc-demo

fun main() {
    // Инициализация реализаций
    ArcFactoryProvider.install()
    ArcFactoryProvider.bootstrap()

    // Предзагрузка OpenGL
    OpenGL.preload()

    // Найти приложение в текущем контексте (реализация OpenGL)
    val application: Application = Application.find()
    application.init(Configuration.create())

    // Получить шейдер и скомпилировать его
    val shader: ShaderInstance = getShaderInstance()
    shader.compileShaders()

    val renderSystem: RenderSystem = application.renderSystem

    // Создать буфер с нашими вершинами
    val buffer: VertexBuffer = createBuffer(application)

    while (!application.window.shouldClose()) {
        // Начать кадр рендеринга
        renderSystem.beginFrame()

        shader.bind()
        renderSystem.drawer.draw(buffer)
        shader.unbind()

        // Завершить кадр
        renderSystem.endFrame()
    }
}

private fun getShaderInstance(): ShaderInstance {
    val vertexShader = """
        #version 410

        layout (location = 0) in vec3 Position;
        layout (location = 1) in vec4 Color;

        out vec4 vertexColor;

        void main()
        {
            gl_Position = vec4(Position, 1.0);

            vertexColor = Color;
        } 
        """.trimIndent().asRuntimeAsset()

    val fragmentShader = """
        #version 410
  
        in vec4 vertexColor;
        out vec4 FragColor;
  
        void main()
        {
            FragColor = vertexColor;
        }
        """.trimIndent().asRuntimeAsset()

    return ShaderInstance.of(
        vertexShader,
        fragmentShader
    )
}

private fun createBuffer(application: Application): VertexBuffer {
    val buffer: DrawBuffer = application.renderSystem.drawer.begin(
        DrawerMode.TRIANGLES,
        VertexFormat.builder()
            .add(VertexFormatElement.POSITION)
            .add(VertexFormatElement.COLOR)
            .build()
    )

    buffer.addVertex(0f, 0.5f, 0f).setColor(Color.BLUE)
    buffer.addVertex(-0.5f, -0.5f, 0f).setColor(Color.RED)
    buffer.addVertex(0.5f, -0.5f, 0f).setColor(Color.GREEN)

    return buffer.build()
}
```

Для более подробных примеров, смотрите модуль `arc-demo`.

# Философия движка

Движок не подразумевает прямое использование методов LWJGL и внутренних реализаций, по этой причине все реализации
являются **internal** классами. Пользователь должен использовать **только функционал `arc-core`** *(или других core-модулей)*. Использование
внутренних реализаций крайне нежелательно и зависимости рода `arc.engine:arc-opengl:1.0` должны
быть указаны только в ИГРЕ, чтобы единожды одной строкой инициализировать OpenGL (в данном случае) контекст.

### Структура проекта

(*Здесь исключёны утилитарные модули `arc-annotations` и `arc-annotation-processor`*)

***arc-core*** - Главная API движка, она представляет собой интерфейсы, которые в последствии реализуются другими
модулями. Важно понимать, что API в этом модуле должна быть обобщённой и подходить под любую технологию рендера,
к тому же вы НЕ можете делать нейминг в этом модуле, который будет отсылать на какую-то конкретную реализацию.

***arc-common*** - Базовая реализация arc-core, которая содержит общие для всех технологий рендера функции. Так например там
реализована математика, окно и звук. Если вы создаёте класс, который не является абстрактным и служит только чтобы,
реализовать интерфейс из arc-core - делайте его `internal`. Здесь так же не желательно использовать нейминг,
который так или иначе будет отсылать на какую-то конкретную реализацию.

***arc-opengl*** - Реализация движка, использующая OpenGL. В этом модуле желательно писать код,
который будет относиться только к OpenGL. В этом модуле следует использовать нейминг,
который отсылает на то, что классы относятся к OpenGL (Например приставка Gl в начале названия классов). Так же, по возможности,
все классы должны быть `internal`.

***arc-vulkan*** - Реализация движка, использующая Vulkan. В этом модуле желательно писать код,
который будет относиться только к Vulkan. В этом модуле следует использовать нейминг,
который отсылает на то, что классы относятся к Vulkan (Например приставка Vk в начале названия классов). Так же, по возможности,
все классы должны быть `internal`.

***arc-extensions*** - Различные расширения для движка. По-возможности необходимо разделять расширения на подобную структуру,
что и у самого движка. То есть модуль `core` с описанием через интерфейсы, модуль `common` с обобщёнными реализациями и все остальные
модули (Например `opengl`, `vulkan`, `openal`, `glfw` и т.п.), которые будут реализацией через конкретную технологию.