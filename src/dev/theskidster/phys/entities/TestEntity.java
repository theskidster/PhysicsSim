package dev.theskidster.phys.entities;

import dev.theskidster.phys.graphics.Graphics;
import dev.theskidster.phys.main.App;
import dev.theskidster.phys.main.GLProgram;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryStack;

/**
 * @author J Hoffman
 * Created: Apr 14, 2021
 */

public class TestEntity extends Entity {

    private final Graphics g = new Graphics();
    
    public TestEntity(Vector3f position) {
        super(position);
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            g.vertices = stack.mallocFloat(18);
            
            //(vec3 position), (vec3 color)
            g.vertices.put(-8).put(-8).put(0)   .put(1).put(0).put(0);
            g.vertices .put(0) .put(8).put(0)   .put(0).put(1).put(0);
            g.vertices .put(8).put(-8).put(0)   .put(0).put(0).put(1);
            
            g.vertices.flip();
        }
        
        g.bindBuffers();
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (6 * Float.BYTES), 0);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, (6 * Float.BYTES), (3 * Float.BYTES));
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update() {
        g.modelMatrix.translation(position);
    }

    @Override
    public void render(GLProgram sceneProgram) {
        glEnable(GL_DEPTH_TEST);
        glBindVertexArray(g.vao);
        
        sceneProgram.setUniform("uType", 0);
        sceneProgram.setUniform("uModel", false, g.modelMatrix);
        
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glDisable(GL_DEPTH_TEST);
        
        App.checkGLError();
    }

    @Override
    public void destroy() {
        
    }
    
}