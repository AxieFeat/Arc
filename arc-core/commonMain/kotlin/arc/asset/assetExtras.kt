@file:JvmSynthetic
package arc.asset

import kotlin.jvm.JvmSynthetic

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray

/**
 * Create instance of [RuntimeAsset] from a text.
 */
@JvmSynthetic
fun String.asRuntimeAsset(): RuntimeAsset = RuntimeAsset.from(this)

/**
 * Create instance of [RuntimeAsset] from bytes.
 */
@JvmSynthetic
fun ByteArray.asRuntimeAsset(): RuntimeAsset = RuntimeAsset.from(this)

/**
 * Create instance of [FileAsset] from a file.
 */
@JvmSynthetic
fun Path.asFileAsset(): FileAsset = FileAsset.from(this)

/**
 * Create instance of [RuntimeAsset] from bytes of a file.
 */
@JvmSynthetic
fun Path.asRuntimeAsset(): RuntimeAsset = RuntimeAsset.from(SystemFileSystem.source(this).buffered().use { it.readByteArray() })

/**
 * Create a new stack of assets.
 *
 * @param T Type of asset.
 * @param asset Assets of stack.
 *
 * @return New instance of [AssetStack].
 */
@JvmSynthetic
fun <T : AssetLike> assetStackOf(vararg asset: T): AssetStack<T> = AssetStack.of(asset.toSet())

/**
 * Create a new mutable stack of assets.
 *
 * @param T Type of asset.
 * @param asset Assets of stack.
 *
 * @return New instance of [MutableAssetStack].
 */
@JvmSynthetic
fun <T : AssetLike> mutableAssetStackOf(vararg asset: T): MutableAssetStack<T> = MutableAssetStack.of(asset.toSet())
