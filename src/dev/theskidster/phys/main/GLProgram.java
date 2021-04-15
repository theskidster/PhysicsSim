package dev.theskidster.phys.main;

import static dev.theskidster.phys.main.BufferType.*;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.system.MemoryStack;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

/**
 * Represents a completed shader program comprised of multiple {@linkplain Shader} objects that will specify how data will be processed at different 
 * rendering stages by the GPU while the program is active.
 */
public class GLProgram {

    final int handle;
    
    private final Map<String, UniformVariable> uniforms = new HashMap<>();
    private static final Map<BufferType, Integer> bufferSizes;
    
    static {
        bufferSizes = new HashMap<>() {{
            put(VEC2, 2);
            put(VEC3, 3);
            put(MAT3, 12);
            put(MAT4, 16);
        }};
    }
    
    /**
     * Creates a new shader program with the code supplied from the compiled .glsl source files.
     * 
     * @param shaders objects representing the compiled .glsl source code
     * @param name    the name that will display in the console should this program fail to link properly
     */
    GLProgram(LinkedList<Shader> shaders, String name) {
        handle = glCreateProgram();
        shaders.forEach(shader -> glAttachShader(handle, shader.handle));
        glLinkProgram(handle);
        
        if(glGetProgrami(handle, GL_LINK_STATUS) != GL_TRUE) {
            Logger.logSevere("Failed to link shader program: \"" + name + "\"" , null);
        }
    }
    
    /**
     * Creates a new {@linkplain UniformVariable uniform variable} object that contains data in a buffer which will later be supplied to the GPU during 
     * rendering operations.
     * 
     * @param name   the unique name used to identify the uniform variable as it appears in the .glsl source file
     * @param buffer the data buffer that stores the value which will be supplied to the GPU through a uniform variable
     * @return a new uniform variable object
     */
    private UniformVariable createUniform(String name, Buffer buffer) {
        return new UniformVariable(glGetUniformLocation(handle, name), buffer);
    }
    
    /**
     * Creates an association between a CPU-stored data buffer of the type specified to a uniform variable located inside one or more of the shader programs 
     * source code files.
     * 
     * @param type the data type of the uniform variable
     * @param name the unique name used to identify the uniform variable as it appears in the .glsl source file
     */
    void addUniform(BufferType type, String name) {
        if(glGetUniformLocation(handle, name) == -1) {
            Logger.logSevere("Failed to find uniform: \"" + name + "\" check " + 
                             "variable name or GLSL source file where it is declared.", 
                             null);
        }
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            switch(type) {
                case INT        -> uniforms.put(name, createUniform(name, stack.mallocInt(1)));
                case FLOAT      -> uniforms.put(name, createUniform(name, stack.mallocFloat(1)));
                case VEC2, VEC3 -> uniforms.put(name, createUniform(name, stack.mallocFloat(bufferSizes.get(type))));
                case MAT3, MAT4 -> uniforms.put(name, createUniform(name, stack.mallocFloat(bufferSizes.get(type))));
            }
        }
    }
    
    /**
     * Sets this as the current program that the GPU will use.
     */
    void use() {
        glUseProgram(handle);
    }
    
    /**
     * Supplies a uniform variable used by the current shader program with a new value.
     * 
     * @param name  the name used to identify the uniform variable
     * @param value the value to pass to the shader program
     */
    public void setUniform(String name, int value) {
        glUniform1i(uniforms.get(name).location, value);
    }
    
    /**
     * Supplies a uniform variable used by the current shader program with a new value.
     * 
     * @param name  the name used to identify the uniform variable
     * @param value the value to pass to the shader program
     */
    public void setUniform(String name, float value) {
        glUniform1f(uniforms.get(name).location, value);
    }
    
    /**
     * Supplies a uniform variable used by the current shader program with a new value.
     * 
     * @param name  the name used to identify the uniform variable
     * @param value the value to pass to the shader program
     */
    public void setUniform(String name, Vector2f value) {
        glUniform2fv(
                uniforms.get(name).location,
                value.get(uniforms.get(name).asFloatBuffer()));
    }
    
    /**
     * Supplies a uniform variable used by the current shader program with a new value.
     * 
     * @param name  the name used to identify the uniform variable
     * @param value the value to pass to the shader program
     */
    public void setUniform(String name, Vector3f value) {
        glUniform3fv(
                uniforms.get(name).location,
                value.get(uniforms.get(name).asFloatBuffer()));
    }
    
    /**
     * Supplies a uniform variable used by the current shader program with a new value.
     * 
     * @param name      the name used to identify the uniform variable
     * @param transpose if true, the matrix data provided in the value parameter will be transposed
     * @param value     the value to pass to the shader program
     */
    public void setUniform(String name, boolean transpose, Matrix3f value) {
        glUniformMatrix3fv(
                uniforms.get(name).location,
                transpose,
                value.get(uniforms.get(name).asFloatBuffer()));
    }
    
    /**
     * Supplies a uniform variable used by the current shader program with a new value.
     * 
     * @param name      the name used to identify the uniform variable
     * @param transpose if true, the matrix data provided in the value parameter will be transposed
     * @param value     the value to pass to the shader program
     */
    public void setUniform(String name, boolean transpose, Matrix4f value) {
        glUniformMatrix4fv(
                uniforms.get(name).location,
                transpose,
                value.get(uniforms.get(name).asFloatBuffer()));
    }
    
}