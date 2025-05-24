@file:JvmSynthetic
package arc.asset

import java.io.File

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
fun File.asFileAsset(): FileAsset = FileAsset.from(this)

/**
 * Create instance of [RuntimeAsset] from bytes of a file.
 */
@JvmSynthetic
fun File.asRuntimeAsset(): RuntimeAsset = RuntimeAsset.from(this.readBytes())