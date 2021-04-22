package dev.theskidster.phys.scene;

import dev.theskidster.phys.main.App;
import dev.theskidster.phys.main.GLProgram;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryStack;
import org.ode4j.ode.DBody;
import org.ode4j.ode.DGeom;
import org.ode4j.ode.DMass;
import org.ode4j.ode.DSpace;
import org.ode4j.ode.DWorld;
import org.ode4j.ode.OdeHelper;

/**
 * @author J Hoffman
 * Created: Apr 14, 2021
 */

class EntityGround extends Entity {

    DBody dBody;
    DGeom dGeom;
    DMass dMass;
    
    public EntityGround(double x, double y, double z, double w, DWorld dWorld, DSpace dSpace) {
        super(new Vector3f());
        
        dGeom = OdeHelper.createPlane(dSpace, x, y, z, w);
        
        //dSpace.add(dGeom);
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            vertices = stack.mallocFloat(24);
            indices  = stack.mallocInt(6);
            
            //(vec3 position), (vec3 color)
            vertices.put(-100).put(0).put(-100)     .put(0.1f).put(0.3f).put(0.6f);
            vertices.put(-100).put(0) .put(100)     .put(0.1f).put(0.3f).put(0.6f);
            vertices .put(100).put(0) .put(100)     .put(0.1f).put(0.3f).put(0.6f);
            vertices. put(100).put(0).put(-100)     .put(0.1f).put(0.3f).put(0.6f);
            
            indices.put(0).put(1).put(2);
            indices.put(2).put(3).put(0);
            
            vertices.flip();
            indices.flip();
        }
        
        bindBuffers();
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (6 * Float.BYTES), 0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, (6 * Float.BYTES), (3 * Float.BYTES));
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }

    @Override
    void update() {
        modelMatrix.translation(position);
    }

    @Override
    void render(GLProgram sceneProgram) {
        glEnable(GL_DEPTH_TEST);
        glBindVertexArray(vao);
        
        sceneProgram.setUniform("uType", 0);
        sceneProgram.setUniform("uModel", false, modelMatrix);
        
        glDrawElements(GL_TRIANGLES, indices.capacity(), GL_UNSIGNED_INT, 0);
        glDisable(GL_DEPTH_TEST);
        
        App.checkGLError();
    }

    @Override
    void destroy() {
        freeBuffers();
    }

}