package dev.theskidster.phys.scene;

import dev.theskidster.shadercore.GLProgram;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author J Hoffman
 * Created: Apr 14, 2021
 */

/**
 * Abstract class which can be used to create dynamic objects within the {@linkplain dev.theskidster.phys.scene.Scene Scene}.
 */
abstract class Entity {

    protected final int vao = glGenVertexArrays();
    private final int vbo   = glGenBuffers();
    private final int ibo   = glGenBuffers();
    
    protected FloatBuffer vertices;
    protected IntBuffer indices;
    
    protected Matrix4f modelMatrix = new Matrix4f();
    
    private boolean remove;
    
    Vector3f position;
    
    /**
     * Creates a new entity object at the specified position within the 3D scene.
     * 
     * @param position the position that this entity will be placed initially
     */
    Entity(Vector3f position) {
        this.position = position;
    }
    
    /**
     * Convenience method provided to bind the default buffers initialized in this object. Implementing classes are expected to define vertex attribute 
     * layouts following this call in their constructors with methods like 
     * {@linkplain org.lwjgl.opengl.GL30#glVertexAttribPointer(int, int, int, boolean, int, java.nio.ByteBuffer) glVertexAttribPointer()}.
     */
    protected final void bindBuffers() {
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
    protected void freeBuffers() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ibo);
    }
    
    /**
     * Used to organize the entities physics logic. Called exclusively through the 
     * {@linkplain dev.theskidster.phys.scene.Scene#update() Scene.update()} method.
     */
    abstract void update();
    
    /**
     * Used to organize the calls made to the graphics API by this entity. Called exclusively through the 
     * {@linkplain dev.theskidster.phys.scene.Scene#render(GLProgram) Scene.render()} method.
     * 
     * @param sceneProgram the shader program that the scene will use to render objects 
     */
    abstract void render(GLProgram sceneProgram);
    
    /**
     * Used to free all of the resources allocated by this entity once it is no longer needed. Calls to methods like 
     * {@link freeBuffers()} should be made here.
     */
    abstract void destroy();
    
    /**
     * Checks whether the entity has made a request to be removed. If it has, the entity will free all of the resources its allocated and be removed from the
     * {@linkplain dev.theskidster.phys.scene.Scene Scene}.
     * 
     * @return true if the entities {@linkplain remove()} method has been invoked
     */
    boolean removalRequested() {
        if(remove) destroy();
        return remove;
    }
    
    /**
     * Requests the {@linkplain destroy() destruction} and {@linkplain removalRequested() removal} of this entity.
     */
    void remove() { remove = true; }
    
}