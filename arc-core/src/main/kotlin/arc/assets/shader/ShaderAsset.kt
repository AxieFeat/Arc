package arc.assets.shader

import arc.annotations.ImmutableType
import arc.assets.Asset

/**
 * Represents a shader asset within the system.
 *
 * A shader asset encapsulates a shader file that defines a programmable rendering pipeline,
 * such as vertex or fragment shaders. Implementations of this interface include specialized shader
 * types like vertex shaders and fragment shaders, which are identified by their respective file types
 * (e.g., `.vsh` for vertex shaders and `.fsh` for fragment shaders).
 *
 * All implementations of this interface must uphold immutability. The `@ImmutableType` annotation ensures
 * that no fields can be modified after creation, aligning with the immutability requirements of assets in the system.
 *
 * Shader assets extend the base `Asset` interface, thus inheriting functionality related to handling
 * underlying asset files.
 */
@ImmutableType
interface ShaderAsset : Asset