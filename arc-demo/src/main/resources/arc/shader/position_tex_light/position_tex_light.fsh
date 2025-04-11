#version 410

uniform sampler2D Sampler;

in vec2 texCoord;
in vec2 texCoord2;

out vec4 fragColor;

void main() {
    vec4 texColor = texture(Sampler, texCoord);

    float blockLight = texCoord2.x / 15.0;
    float skyLight = texCoord2.y / 15.0;

    float lightLevel = clamp(blockLight * 0.8 + skyLight * 0.2, 0.0, 1.0);

    fragColor = texColor * lightLevel;
}
