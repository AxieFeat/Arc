package arc

private typealias ApplicationFactory = () -> Application

/**
 * Use this class to register your implementation of [Application].
 */
object ArcApplicationFactory : Application.Factory {

    private val factories = mutableMapOf<String, ApplicationFactory>()

    @JvmStatic
    fun register(name: String, factory: ApplicationFactory) {
        factories[name] = factory
    }

    override fun find(impl: String): Application {
        return (
                factories[impl]
                    ?: throw NullPointerException("Can not found \"$impl\" implementation. Available values in current environment: ${factories.keys}")
                ).invoke()
    }

}