@file:JvmSynthetic
@file:Suppress("NOTHING_TO_INLINE")

package arc.files

import java.io.File

/**
 * Get file from path.
 *
 * @param loc Type of file location.
 * @param path Path to file.
 *
 * @return New instance of [File].
 */
@JvmSynthetic
inline fun file(loc: FileLocation, path: String): File = ArcLocationSpace.file(loc, path)

/**
 * Get file from classpath.
 *
 * @param path Path to file.
 *
 * @return New instance of [File].
 *
 * @see FileLocation.CLASSPATH
 */
@JvmSynthetic
inline fun classpath(path: String): File = ArcLocationSpace.classpath(path)

/**
 * Get file from absolute path.
 *
 * @param path Path to file.
 *
 * @return New instance of [File].
 *
 * @see FileLocation.ABSOLUTE
 */
@JvmSynthetic
inline fun absolute(path: String): File = ArcLocationSpace.absolute(path)

/**
 * Get file from directory of current application.
 *
 * @param path Path to file.
 *
 * @return New instance of [File].
 *
 * @see FileLocation.LOCAL
 */
@JvmSynthetic
inline fun local(path: String): File = ArcLocationSpace.local(path)