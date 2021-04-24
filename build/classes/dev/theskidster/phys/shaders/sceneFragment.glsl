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

    float bias = max(0.05 * (1 - dotLightNormal), 0.005);
    return (depth + bias) < pos.z ? 0 : 1;

    //return depth < pos.z ? 0 : 1;
}

void main() {
    switch(uType) {
        case 0: case 1:
            vec3 lightPos = vec3(uLightPos);
            
            vec3 ambient = 0.3 * ioColor;
            
            vec3 norm    = normalize(ioNormal);
            float diff   = max(dot(norm, normalize(lightPos)), -0.6);
            vec3 diffuse = (diff * ioColor * ioColor);
            
            vec3 lightDir = normalize(lightPos - ioFragPos);
            float dotLightNormal = dot(lightDir, norm);

            float shadow  = calcShadow(dotLightNormal);
            vec3 lighting = (shadow * (diffuse) + ambient) * ioColor;

            ioResult = vec4(lighting, 1);
            break;

        /*
        case 1: //Used for cube entities.
            vec3 lightPos = vec3(1, 4, 2.5);
            vec3 lightDir = normalize(lightPos);
            
            vec3 norm    = normalize(ioNormal);
            float diff   = max(dot(norm, lightDir), -0.6);
            vec3 diffuse = (diff * ioColor * ioColor) + 0.3;
            
            ioResult = vec4(ioColor + diffuse, 1) * 0.5;
            break;
        */
    }
}