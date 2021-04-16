#version 330 core

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec3 aColor;

uniform int uType;
uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;

out vec3 ioColor;
out vec3 ioNear;
out vec3 ioFar;

vec3 gridPlane[6] = vec3[] (
    vec3(-1,  1, 0), 
    vec3(-1, -1, 0), 
    vec3( 1, -1, 0),
    vec3( 1, -1, 0), 
    vec3( 1,  1, 0), 
    vec3(-1,  1, 0)
);

vec3 unProject(float x, float y, float z, mat4 view, mat4 proj) {
    mat4 newView = inverse(view);
    mat4 newProj = inverse(proj);
    vec4 result  = newView * newProj * vec4(x, y, z, 1);
    
    return result.xyz / result.w;
}

void main() {
    switch(uType) {
        case 0:
            ioColor     = aColor;
            gl_Position = uProjection * uView * uModel * vec4(aPosition, 1);
            break;
    }
}