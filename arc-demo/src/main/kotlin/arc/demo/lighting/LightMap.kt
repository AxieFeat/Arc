package arc.demo.lighting

interface LightMap {

    fun get(x: Int, y: Int, z: Int, channel: LightChannel): UByte {
        return when(channel) {
            LightChannel.RED -> getRed(x, y, z)
            LightChannel.GREEN -> getGreen(x, y, z)
            LightChannel.BLUE -> getBlue(x, y, z)
            LightChannel.SUN -> getSun(x, y, z)
        }
    }

    fun set(x: Int, y: Int, z: Int, channel: LightChannel, value: UByte) {
        when(channel) {
            LightChannel.RED -> setRed(x, y, z, value)
            LightChannel.GREEN -> setGreen(x, y, z, value)
            LightChannel.BLUE -> setBlue(x, y, z, value)
            LightChannel.SUN -> setSun(x, y, z, value)
        }
    }

    fun getRed(x: Int, y: Int, z: Int): UByte
    fun setRed(x: Int, y: Int, z: Int, value: UByte)

    fun getGreen(x: Int, y: Int, z: Int): UByte
    fun setGreen(x: Int, y: Int, z: Int, value: UByte)

    fun getBlue(x: Int, y: Int, z: Int): UByte
    fun setBlue(x: Int, y: Int, z: Int, value: UByte)

    fun getSun(x: Int, y: Int, z: Int): UByte
    fun setSun(x: Int, y: Int, z: Int, value: UByte)

}