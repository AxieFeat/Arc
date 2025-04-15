#version 410

uniform sampler2D Sampler;

in vec2 texCoord;
in vec4 color;

out vec4 fragColor;

void main() {
    fragColor = color * texture(Sampler, texCoord);
}

