#version 330 core

in vec3 ioColor;
in vec3 ioNormal;
in vec3 ioFragPos;

uniform int uType;

out vec4 ioResult;

void main() {
    switch(uType) {
        case 0:
            ioResult = vec4(ioColor, 0);
            break;

        case 1: //Used for cube entities.
            vec3 lightPos = vec3(1, 4, 2.5);
            vec3 lightDir = normalize(lightPos);
            
            vec3 norm    = normalize(ioNormal);
            float diff   = max(dot(norm, lightDir), -0.6);
            vec3 diffuse = (diff * ioColor * ioColor) + 0.3;
            
            ioResult = vec4(ioColor + diffuse, 1) * 0.5;
            break;
    }
}