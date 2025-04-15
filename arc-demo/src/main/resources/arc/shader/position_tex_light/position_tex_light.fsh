#version 410

#define MAX_LIGHT_CELLS 64

struct LightCell {
    vec2 uvMin;
    vec2 uvMax;
    vec4 color;
};

uniform sampler2D Sampler;
uniform int u_LightCellCount;
uniform LightCell u_LightCells[MAX_LIGHT_CELLS];

in vec2 texCoord;
in vec2 texCoord2;

out vec4 fragColor;

void main() {
    vec4 texColor = texture(Sampler, texCoord);

    float blockLight = texCoord2.x / 15.0;
    float skyLight = texCoord2.y / 15.0;

    float lightLevel = clamp(blockLight * 0.8 + skyLight * 0.2, 0.0, 1.0);
    float steps = 32.0;
    lightLevel = floor(lightLevel * steps) / steps;

    vec4 finalColor = texColor * lightLevel;

    for (int i = 0; i < u_LightCellCount; i++) {
        LightCell cell = u_LightCells[i];
        if (texCoord.x >= cell.uvMin.x && texCoord.x <= cell.uvMax.x &&
        texCoord.y >= cell.uvMin.y && texCoord.y <= cell.uvMax.y) {
            finalColor = mix(finalColor, finalColor * cell.color, cell.color.a);
        }
    }

    fragColor = finalColor;
}

//#version 410
//
//uniform sampler2D Sampler;
//
//in vec2 texCoord;
//in vec2 texCoord2;
//
//out vec4 fragColor;
//
//void main() {
//    vec4 texColor = texture(Sampler, texCoord);
//
//    float blockLight = texCoord2.x / 15.0;
//    float skyLight = texCoord2.y / 15.0;
//
//    float steps = 32.0;
//
//    float flatSky = floor(skyLight * steps) / steps;
//
//    float lightLevel = clamp(flatSky + blockLight * 0.4, 0.0, 1.0);
//
//    fragColor = texColor * lightLevel;
//}

