#version 330 core

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec3 aNormal;

uniform int uType;
uniform vec3 uColor;
uniform mat3 uNormal;
uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;
uniform mat4 uLightSpace;

out vec3 ioColor;
out vec3 ioNormal;
out vec3 ioFragPos;
out vec4 ioLightFrag;

void main() {
    switch(uType) {
        case 0: case 1:
            ioColor     = uColor;
            ioFragPos   = vec3(uModel * vec4(aPosition, 1));
            ioNormal    = uNormal * aNormal;
            ioLightFrag = uLightSpace * vec4(ioFragPos, 1);
            gl_Position = uProjection * uView * uModel * vec4(aPosition, 1);
            break;
    }
}