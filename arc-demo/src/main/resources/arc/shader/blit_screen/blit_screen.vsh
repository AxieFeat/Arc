#version 410

layout (location = 0) in vec3 Position;
layout (location = 1) in vec2 UV0;

out vec2 texCoord;

void main() {
    gl_Position = vec4(Position, 1.0);

    texCoord = UV0;
}