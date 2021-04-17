package dev.theskidster.phys.scene;

import dev.theskidster.phys.graphics.Color;
import dev.theskidster.phys.graphics.Cube;
import dev.theskidster.phys.graphics.Graphics;
import dev.theskidster.phys.main.App;
import dev.theskidster.phys.main.GLProgram;
import java.util.Arrays;
import org.joml.AxisAngle4f;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL30.*;
import org.ode4j.math.DMatrix3;
import org.ode4j.math.DMatrix3C;
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
    
    private final Graphics g      = new Graphics();
    private final Matrix3f normal = new Matrix3f();
    
    private final Matrix3f rotation = new Matrix3f();
    private final AxisAngle4f axisAngle = new AxisAngle4f();
    
    public Color color = Color.WHITE;
    
    public EntityCube(Vector3f position, float width, float height, float depth, DWorld dWorld, DSpace dSpace) {
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
        
        Cube cube  = new Cube(width, height, depth);
        g.vertices = cube.vertices;
        g.indices  = cube.indices;
        
        g.bindBuffers();
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (6 * Float.BYTES), 0);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, (6 * Float.BYTES), (3 * Float.BYTES));
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(2);
    }
    
    @Override
    void update() {
        position.x = (float) (dGeom.getPosition().get0());
        position.y = (float) (dGeom.getPosition().get1());
        position.z = (float) (dGeom.getPosition().get2());
        
        normal.set(g.modelMatrix.invert());
        g.modelMatrix.translation(position);
        
        float[] rotArray = dGeom.getRotation().toFloatArray12();
        
        /*
        Couldnt figure out how to make this play nice- so we'll just have to
        deal with it for now.
        */
        g.modelMatrix.m00(rotArray[0]);
        g.modelMatrix.m01(rotArray[4]);
        g.modelMatrix.m02(rotArray[8]);
        g.modelMatrix.m03(0);
        g.modelMatrix.m10(rotArray[1]);
        g.modelMatrix.m11(rotArray[5]);
        g.modelMatrix.m12(rotArray[9]);
        g.modelMatrix.m13(0);
        g.modelMatrix.m20(rotArray[2]);
        g.modelMatrix.m21(rotArray[6]);
        g.modelMatrix.m22(rotArray[10]);
        g.modelMatrix.m23(0);
    }

    @Override
    void render(GLProgram sceneProgram) {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glBindVertexArray(g.vao);
        
        sceneProgram.setUniform("uType", 1);
        sceneProgram.setUniform("uColor", color.asVec3());
        sceneProgram.setUniform("uNormal", true, normal);
        sceneProgram.setUniform("uModel", false, g.modelMatrix);
        
        glDrawElements(GL_TRIANGLES, g.indices.capacity(), GL_UNSIGNED_INT, 0);
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        
        App.checkGLError();
    }

    @Override
    void destroy() {
    }

}