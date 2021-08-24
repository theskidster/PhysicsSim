package dev.theskidster.phys.scene;

import dev.theskidster.phys.main.Color;
import dev.theskidster.phys.main.App;
import dev.theskidster.shadercore.GLProgram;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryUtil;
import org.ode4j.math.DMatrix3;
import org.ode4j.ode.DBody;
import org.ode4j.ode.DGeom;
import org.ode4j.ode.DMass;
import static org.ode4j.ode.DMisc.dRandReal;
import org.ode4j.ode.DRotation;
import org.ode4j.ode.DSpace;
import org.ode4j.ode.DWorld;
import org.ode4j.ode.OdeHelper;

/**
 * @author J Hoffman
 * Created: Apr 16, 2021
 */

public class EntityCube extends Entity {

    DBody dBody;
    DGeom dGeom;
    DMass dMass;
    
    private final Matrix3f normal = new Matrix3f();
    
    EntityCube(Vector3f position, float width, float height, float depth, DWorld dWorld, DSpace dSpace) {
        super(position);
        
        dBody = OdeHelper.createBody(dWorld);
        dBody.setPosition(position.x, position.y, position.z);
        dBody.setLinearVel(0, 0, 0);
        
        DMatrix3 dMat = new DMatrix3();
        DRotation.dRFromAxisAndAngle(dMat, dRandReal() * 2.0 - 1.0, 
                                           dRandReal() * 2.0 - 1.0, 
                                           dRandReal() * 2.0 - 1.0,
                                           dRandReal() * 10.0 - 5.0);
        dBody.setRotation(dMat);
        
        dMass = OdeHelper.createMass();
        dMass.setBox(0.5, width, height, depth);
        
        dBody.setMass(dMass);
        
        dGeom = OdeHelper.createBox(dSpace, width, height, depth);
        dGeom.setBody(dBody);
        
        float w = width / 2;
        float h = height / 2;
        float d = depth / 2;
        
        vertices = MemoryUtil.memAllocFloat(144);
        indices  = MemoryUtil.memAllocInt(36);
        
        //Front
        vertices.put(-w) .put(h).put(-d).put(0).put(0).put(-1);   //0
        vertices .put(w) .put(h).put(-d).put(0).put(0).put(-1);   //1
        vertices .put(w).put(-h).put(-d).put(0).put(0).put(-1);   //2
        vertices.put(-w).put(-h).put(-d).put(0).put(0).put(-1);   //3
        
        //Back
        vertices .put(w) .put(h).put(d).put(0).put(0).put(1);     //4
        vertices.put(-w) .put(h).put(d).put(0).put(0).put(1);     //5
        vertices.put(-w).put(-h).put(d).put(0).put(0).put(1);     //6
        vertices .put(w).put(-h).put(d).put(0).put(0).put(1);     //7
        
        //Top
        vertices.put(-w).put(h) .put(d).put(0).put(1).put(0);     //8
        vertices .put(w).put(h) .put(d).put(0).put(1).put(0);     //9
        vertices .put(w).put(h).put(-d).put(0).put(1).put(0);     //10
        vertices.put(-w).put(h).put(-d).put(0).put(1).put(0);     //11
        
        //Bottom
        vertices.put(-w).put(-h).put(-d).put(0).put(-1).put(0);   //12
        vertices .put(w).put(-h).put(-d).put(0).put(-1).put(0);   //13
        vertices .put(w).put(-h) .put(d).put(0).put(-1).put(0);   //14
        vertices.put(-w).put(-h) .put(d).put(0).put(-1).put(0);   //15
        
        //Left
        vertices.put(-w) .put(h) .put(d).put(-1).put(0).put(0);   //16
        vertices.put(-w) .put(h).put(-d).put(-1).put(0).put(0);   //17
        vertices.put(-w).put(-h).put(-d).put(-1).put(0).put(0);   //18
        vertices.put(-w).put(-h) .put(d).put(-1).put(0).put(0);   //19
        
        //Right
        vertices.put(w) .put(h).put(-d).put(1).put(0).put(0);     //20
        vertices.put(w) .put(h) .put(d).put(1).put(0).put(0);     //21
        vertices.put(w).put(-h) .put(d).put(1).put(0).put(0);     //22
        vertices.put(w).put(-h).put(-d).put(1).put(0).put(0);     //23
        
        indices.put(0).put(1).put(2).put(2).put(3).put(0);       //Front
        indices.put(4).put(5).put(6).put(6).put(7).put(4);       //Back
        indices.put(8).put(9).put(10).put(10).put(11).put(8);    //Top
        indices.put(12).put(13).put(14).put(14).put(15).put(12); //Bottom
        indices.put(16).put(17).put(18).put(18).put(19).put(16); //Left
        indices.put(20).put(21).put(22).put(22).put(23).put(20); //Right
        
        vertices.flip();
        indices.flip();
        
        bindBuffers();
        
        MemoryUtil.memFree(vertices);
        MemoryUtil.memFree(indices);
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (6 * Float.BYTES), 0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, (6 * Float.BYTES), (3 * Float.BYTES));
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }
    
    @Override
    void update() {
        position.x = (float) (dGeom.getPosition().get0());
        position.y = (float) (dGeom.getPosition().get1());
        position.z = (float) (dGeom.getPosition().get2());
        
        normal.set(modelMatrix.invert());
        modelMatrix.translation(position);
        
        float[] rotArray = dGeom.getRotation().toFloatArray12();
        
        /*
        Couldnt figure out how to make this play nice- so we'll just have to
        deal with it for now.
        */
        modelMatrix.m00(rotArray[0]);
        modelMatrix.m01(rotArray[4]);
        modelMatrix.m02(rotArray[8]);
        modelMatrix.m03(0);
        modelMatrix.m10(rotArray[1]);
        modelMatrix.m11(rotArray[5]);
        modelMatrix.m12(rotArray[9]);
        modelMatrix.m13(0);
        modelMatrix.m20(rotArray[2]);
        modelMatrix.m21(rotArray[6]);
        modelMatrix.m22(rotArray[10]);
        modelMatrix.m23(0);
    }

    @Override
    void render(GLProgram program) {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glBindVertexArray(vao);
        
        if(!program.name.equals("depth")) {
            program.setUniform("uType", 1);
            program.setUniform("uColor", Color.WHITE.asVec3());
            program.setUniform("uNormal", true, normal);
        }
        program.setUniform("uModel", false, modelMatrix);
        
        glDrawElements(GL_TRIANGLES, indices.capacity(), GL_UNSIGNED_INT, 0);
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        
        App.checkGLError();
    }

    @Override
    void destroy() {
        freeBuffers();
    }

}