package arc.asset

internal data class ArcAssetStack<T : AssetLike>(
     override val assets: MutableSet<T>
) : MutableAssetStack<T> {

    override fun add(asset: T) {
        assets.add(asset)
    }

    override fun remove(asset: T) {
        assets.remove(asset)
    }

    override fun iterator() = assets.iterator()

    object Factory : AssetStack.Factory {
        override fun <T : AssetLike> create(assets: Set<T>): AssetStack<T> {
            return ArcAssetStack(assets.toMutableSet())
        }
    }

    object MutableFactory : MutableAssetStack.Factory {
        override fun <T : AssetLike> create(assets: Set<T>): MutableAssetStack<T> {
            return ArcAssetStack(assets.toMutableSet())
        }
    }
}