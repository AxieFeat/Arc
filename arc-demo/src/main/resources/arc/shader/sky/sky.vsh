#version 410

layout (location = 0) in vec3 Position;
layout (location = 1) in vec4 Color;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

out vec4 vertexColor;

void main()
{
    mat4 viewNoTranslation = mat4(mat3(viewMatrix));

    vec4 worldPos = vec4(Position, 1.0);
    gl_Position = projectionMatrix * viewNoTranslation * worldPos;

    vertexColor = Color;
}
