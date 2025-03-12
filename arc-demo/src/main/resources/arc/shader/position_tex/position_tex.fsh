#version 410

uniform sampler2D Sampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler, texCoord);
    fragColor = color;
}
