#version 410

layout (location = 0) in vec3 Position;
layout (location = 1) in vec2 UV0;
layout (location = 2) in vec2 UV2;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

out vec2 texCoord;
out vec2 texCoord2;

void main() {
    gl_Position = projectionMatrix * viewMatrix * vec4(Position, 1.0);

    texCoord = UV0;
    texCoord2 = UV2;
}
