@file:JvmSynthetic

package arc.files

import arc.Application
import java.io.File

/**
 * You should use these functions only after the [Application] is initialized in the [arc.util.factory.FactoryProvider],
 * otherwise you may get unexpected errors.
 */
@JvmSynthetic
private val application = Application.find()

/**
 * Get file from path.
 *
 * @param loc Type of file location.
 * @param path Path to file.
 *
 * @return New instance of [File].
 */
@JvmSynthetic
fun file(loc: FileLocation, path: String): File = application.locationSpace.file(loc, path)

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
fun classpath(path: String): File = application.locationSpace.classpath(path)

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
fun absolute(path: String): File = application.locationSpace.absolute(path)

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
fun local(path: String): File = application.locationSpace.local(path)