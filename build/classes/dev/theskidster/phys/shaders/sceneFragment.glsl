#version 330 core

in vec3 ioColor;
in vec3 ioNormal;
in vec3 ioFragPos;
in vec4 ioLightFrag;

uniform int uType;
uniform vec3 uLightPos;

uniform sampler2D uShadowMap;

out vec4 ioResult;

float calcShadow(float dotLightNormal) {
    vec3 pos = ioLightFrag.xyz * 0.5 + 0.5;

    if(pos.z > 1) pos.z = 1;

    float depth = texture(uShadowMap, pos.xy).r;

    float bias = max(0.009 * (1 - dotLightNormal), 0.0003);
    return (depth + bias) < pos.z ? 0 : 1;
}

/**
 * Use this to view the frustrum of the shadow map.
 */
float calcShowShadow(float dotLightNormal) {
    vec3 pos = ioLightFrag.xyz * 0.5 + 0.5;
    if(pos.z > 1) pos.z = 1;
    float depth = texture(uShadowMap, pos.xy).r;

    return depth < pos.z ? 0 : 1;
}

void main() {
    switch(uType) {
        case 0: case 1: 
            //Normally I'd supply this via light struct- but theres only one so whatever.
            float lightBrightness = 0.25;
            vec3 lightPos         = vec3(uLightPos);
            vec3 lightDir         = normalize(lightPos);
            
            //Calculate output from world light source.
            vec3 norm    = normalize(ioNormal);
            float diff   = max(dot(norm, lightDir), 0);
            vec3 diffuse = diff * ioColor * ioColor;
            vec3 ambient = (ioColor + diffuse) * lightBrightness;

            //vec3 ambient = 0.3 * ioColor;

            //Calculate shadows.
            float dotLightNormal = dot(lightDir, norm);
            float shadow         = calcShadow(dotLightNormal);
            vec3 lighting        = (shadow * diffuse + ambient) * ioColor;
            
            ioResult = vec4(lighting, 1);
            break;
    }
}