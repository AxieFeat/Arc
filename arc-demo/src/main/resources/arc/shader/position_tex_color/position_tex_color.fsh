#version 410

uniform sampler2D Sampler;

in vec2 texCoord;
in vec4 color;
in vec3 worldPos;

out vec4 fragColor;

struct Light {
    vec4 color;
    vec3 position;
    float radius;
};

uniform Lights {
    Light lights[2048];
};

uniform int lightCount;

vec3 collect_light(vec3 fragPos, vec4 vertex_color) {
    vec3 lightColor = vec3(0.);
    for (int i = 0; i < lightCount; i++) {
        Light l = lights[i];
        if (l.color.w > 0.0) {
            float intensity = smoothstep(0., 1., 1. - distance(l.position, fragPos) / l.radius);
            lightColor += l.color.rgb * l.color.a * intensity;
        }
    }
    return lightColor;
}

vec3 jodieReinhardTonemap(vec3 c) {
    float l = dot(c, vec3(0.2126, 0.7152, 0.0722));
    vec3 tc = c / (c + 1.0);
    return mix(c / (l + 1.0), tc, tc);
}

vec4 color_light(vec3 pos, vec4 base_color) {
    vec3 lightColor = collect_light(pos, base_color);
    vec3 litColor = base_color.rgb + lightColor;
    vec3 finalColor = jodieReinhardTonemap(litColor);
    return vec4(finalColor, base_color.a) * 1.5f;
}

void main() {
    vec4 texColor = texture(Sampler, texCoord);
    vec4 baseColor = color * texColor;

    fragColor = color_light(worldPos, baseColor);
}