#version 410

layout (location = 0) in vec3 Position;
layout (location = 1) in vec4 Color;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

out vec4 vertexColor;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(Position, 1.0);

    vertexColor = Color;
}