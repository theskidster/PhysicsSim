package dev.theskidster.phys.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author J Hoffman
 * Created: Apr 14, 2021
 */

/**
 * Component object that supplies implementing classes with various OpenGL utilities required for rendering.
 */
public class Graphics {

    public final int vao = glGenVertexArrays();
    public final int vbo = glGenBuffers();
    public final int ibo = glGenBuffers();
    
    public FloatBuffer vertices;
    public IntBuffer indices;
    
    public Matrix4f modelMatrix = new Matrix4f();
    
    /**
     * Convenience method provided to bind the default buffers initialized in this object. Implementing classes are expected to define vertex attribute 
     * layouts following this call in their constructors with methods like 
     * {@linkplain org.lwjgl.opengl.GL30#glVertexAttribPointer(int, int, int, boolean, int, java.nio.ByteBuffer) glVertexAttribPointer()}.
     */
    public void bindBuffers() {
        glBindVertexArray(vao);
        
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        
        if(indices != null) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        }
    }
    
    /**
     * Convenience method which frees the default buffer objects initialized by this object.
     */
    public void freeBuffers() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ibo);
    }
    
}