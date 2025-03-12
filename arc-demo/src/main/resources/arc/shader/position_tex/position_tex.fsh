#version 410

uniform sampler2D Sampler;

in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler, texCoord0);
    fragColor = color;
}
