package arc.graphics.g3d.model

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.assets.ModelAsset
import arc.graphics.g3d.model.texture.ModelTexture
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a 3D model with associated assets, elements, and textures.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Model {

    /**
     * Retrieves the [ModelAsset] associated with this [Model].
     *
     * The asset represents the underlying 3D model data and associated metadata,
     * serving as the immutable source for the model's elements, textures, and other components.
     */
    @get:JvmName("asset")
    val asset: ModelAsset

    /**
     * Retrieves the name of the model.
     *
     * The name is a human-readable identifier that represents this [Model]. It is typically
     * used for distinguishing between different models in a collection or for display purposes.
     * The name is immutable and is set during the creation of the model.
     */
    @get:JvmName("name")
    val name: String

    /**
     * Retrieves the list of elements associated with this model.
     *
     * Elements represent individual components or sections of the 3D model, such as geometric
     * shapes, lighting attributes, and other structural data. These elements collectively
     * define the physical structure and attributes of the model.
     */
    @get:JvmName("elements")
    val elements: List<Element>

    /**
     * Retrieves the list of textures associated with this model.
     *
     * Textures define the visual surface properties of a 3D model. Each texture in the list
     * represents a unique visual layer or mapping applied to the model. The list typically
     * includes details such as the dimensions of each texture, their UV mappings, and
     * references to the renderable texture data.
     *
     * The textures are immutable and cannot be modified after the model's creation.
     */
    @get:JvmName("textures")
    val textures: List<ModelTexture>

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create [Model] from [ModelAsset].
         *
         * @param asset Asset for Model.
         *
         * @return New instance of [Model].
         */
        fun create(asset: ModelAsset): Model

    }

    companion object {

        /**
         * Create [Model] from [ModelAsset].
         *
         * @param asset Asset for Model.
         *
         * @return New instance of [Model].
         */
        @JvmStatic
        fun create(asset: ModelAsset): Model {
            return Arc.factory<Factory>().create(asset)
        }

    }

}