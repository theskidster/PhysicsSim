#version 330 core

in vec3 ioColor;
in vec3 ioNear;
in vec3 ioFar;

uniform int uType;

out vec4 ioResult;

void main() {
    switch(uType) {
        case 0:
            ioResult = vec4(ioColor, 0);
            break;
    }
}