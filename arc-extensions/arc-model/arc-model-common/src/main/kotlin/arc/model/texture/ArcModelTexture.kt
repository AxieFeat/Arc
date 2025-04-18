package arc.model.texture

internal data class ArcModelTexture(
    override val base64Image: String = ""
) : ModelTexture {

   class Builder : ModelTexture.Builder {

       private var base64Image: String = ""

       override fun setImage(base64Image: String): Builder {
           this.base64Image = base64Image

           return this@Builder
       }

       override fun build(): ModelTexture {
           return ArcModelTexture(base64Image)
       }

   }

}