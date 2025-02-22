package arc.asset

import arc.assets.Asset
import arc.assets.AssetStack
import arc.assets.MutableAssetStack

data class ArcAssetStack<T : Asset>(
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
        override fun <T : Asset> create(assets: Set<T>): AssetStack<T> {
            return ArcAssetStack(assets.toMutableSet())
        }
    }

    object MutableFactory : MutableAssetStack.Factory {
        override fun <T : Asset> create(assets: Set<T>): MutableAssetStack<T> {
            return ArcAssetStack(assets.toMutableSet())
        }
    }
}