package arc.assets

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.graphics.g3d.model.Model
import org.jetbrains.annotations.ApiStatus
import java.io.File

/**
 * Represents a 3D model asset in the system. This interface extends the [Asset] interface,
 * providing specific functionality for managing 3D models, including access to the associated model data.
 *
 * Model assets are immutable and must be created using the provided factory or companion object methods.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface ModelAsset : Asset {

    /**
     * Provides access to the underlying 3D model associated with this asset.
     *
     * This property returns an immutable instance of [Model], representing the 3D model
     * and its associated elements, textures, and other metadata.
     *
     * The model data is intrinsic to the asset and cannot be modified once created.
     */
    @get:JvmName("model")
    val model: Model

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create instance of [ModelAsset] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [ModelAsset].
         */
        fun create(file: File): ModelAsset

    }

    companion object {

        /**
         * Create instance of [ModelAsset] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [ModelAsset].
         */
        @JvmStatic
        fun from(file: File): ModelAsset {
            return Arc.factory<Factory>().create(file)
        }

    }

}