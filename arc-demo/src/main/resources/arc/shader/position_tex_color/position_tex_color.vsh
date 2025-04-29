#version 410

layout (location = 0) in vec3 Position;
layout (location = 1) in vec2 UV;
layout (location = 2) in vec4 Color;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

out vec2 texCoord;
out vec4 color;
out vec3 worldPos;

void main() {
    vec4 world = modelMatrix * vec4(Position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * world;

    texCoord = UV;
    color = Color;
    worldPos = world.xyz;
}
