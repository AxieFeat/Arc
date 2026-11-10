@file:JvmSynthetic

package arc.files

import kotlin.jvm.JvmSynthetic

import arc.Application
import kotlinx.io.files.Path

/**
 * You should use these functions only after the [Application] is initialized in the [arc.util.provider.ObjectProvider],
 * otherwise you may get unexpected errors.
 */
@JvmSynthetic
private val application = Application.find()

/**
 * Get a file from a path.
 *
 * You should use these functions only after the [Application] is initialized in the [arc.util.provider.ObjectProvider],
 * otherwise you may get unexpected errors.
 *
 * @param loc Type of file location.
 * @param path Path to file.
 *
 * @return New instance of [Path].
 */
@JvmSynthetic
fun file(loc: FileLocation, path: String): Path = application.locationSpace.file(loc, path)

/**
 * Get a file from classpath.
 *
 * You should use these functions only after the [Application] is initialized in the [arc.util.provider.ObjectProvider],
 * otherwise you may get unexpected errors.
 *
 * @param path Path to file.
 *
 * @return New instance of [Path].
 *
 * @see FileLocation.CLASSPATH
 */
@JvmSynthetic
fun classpath(path: String): Path = application.locationSpace.classpath(path)

/**
 * Get a file from an absolute path.
 *
 * You should use these functions only after the [Application] is initialized in the [arc.util.provider.ObjectProvider],
 * otherwise you may get unexpected errors.
 *
 * @param path Path to file.
 *
 * @return New instance of [Path].
 *
 * @see FileLocation.ABSOLUTE
 */
@JvmSynthetic
fun absolute(path: String): Path = application.locationSpace.absolute(path)

/**
 * Get a file from directory of current application.
 *
 * You should use these functions only after the [Application] is initialized in the [arc.util.provider.ObjectProvider],
 * otherwise you may get unexpected errors.
 *
 * @param path Path to file.
 *
 * @return New instance of [Path].
 *
 * @see FileLocation.LOCAL
 */
@JvmSynthetic
fun local(path: String): Path = application.locationSpace.local(path)
