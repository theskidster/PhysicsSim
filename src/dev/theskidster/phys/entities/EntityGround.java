package dev.theskidster.phys.entities;

import dev.theskidster.phys.main.App;
import dev.theskidster.phys.main.Color;
import dev.theskidster.shadercore.GLProgram;
import org.joml.Matrix3f;
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

public class EntityGround extends Entity {

    DBody dBody;
    DGeom dGeom;
    DMass dMass;
    
    private final Matrix3f normal = new Matrix3f();
    
    public EntityGround(double x, double y, double z, double w, DWorld dWorld, DSpace dSpace) {
        super(new Vector3f());
        
        dGeom = OdeHelper.createPlane(dSpace, x, y, z, w);
        
        //dSpace.add(dGeom);
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            vertices = stack.mallocFloat(24);
            indices  = stack.mallocInt(6);
            
            //(vec3 position), (vec3 normal)
            vertices.put(-50).put(0).put(-50)     .put(0).put(1).put(0);
            vertices.put(-50).put(0) .put(50)     .put(0).put(1).put(0);
            vertices .put(50).put(0) .put(50)     .put(0).put(1).put(0);
            vertices. put(50).put(0).put(-50)     .put(0).put(1).put(0);
            
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
    public void update() {
        modelMatrix.translation(position);
    }

    @Override
    public void render(GLProgram program) {
        glEnable(GL_DEPTH_TEST);
        glBindVertexArray(vao);
        
        if(!program.name.equals("depth")) {
            program.setUniform("uType", 1);
            program.setUniform("uColor", Color.BLUE.asVec3());
            program.setUniform("uNormal", true, normal);
        }
        program.setUniform("uModel", false, modelMatrix);
        
        glDrawElements(GL_TRIANGLES, indices.capacity(), GL_UNSIGNED_INT, 0);
        glDisable(GL_DEPTH_TEST);
        
        App.checkGLError();
    }

    @Override
    public void destroy() {
        freeBuffers();
    }

}