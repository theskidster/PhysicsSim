package dev.theskidster.phys.scene;

import dev.theskidster.phys.graphics.Color;
import dev.theskidster.phys.graphics.Cube;
import dev.theskidster.phys.graphics.Graphics;
import dev.theskidster.phys.main.App;
import dev.theskidster.phys.main.GLProgram;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author J Hoffman
 * Created: Apr 16, 2021
 */

public class EntityCube extends Entity {

    private final Graphics g      = new Graphics();
    private final Matrix3f normal = new Matrix3f();
    
    public Color color = Color.WHITE;
    
    public EntityCube(Vector3f position, float width, float height, float depth) {
        super(position);
        
        Cube cube  = new Cube(width, height, depth);
        g.vertices = cube.vertices;
        g.indices  = cube.indices;
        
        g.bindBuffers();
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (6 * Float.BYTES), 0);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, (6 * Float.BYTES), (3 * Float.BYTES));
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(2);
    }

    float angle = 0;
    
    @Override
    void update() {
        angle += 0.5f;
        
        normal.set(g.modelMatrix.invert());
        g.modelMatrix.translation(position);
        //g.modelMatrix.rotateY((float) Math.toRadians(angle));
    }

    @Override
    void render(GLProgram sceneProgram) {
        glEnable(GL_CULL_FACE);
        glBindVertexArray(g.vao);
        
        sceneProgram.setUniform("uType", 1);
        sceneProgram.setUniform("uColor", color.asVec3());
        sceneProgram.setUniform("uNormal", true, normal);
        sceneProgram.setUniform("uModel", false, g.modelMatrix);
        
        glDrawElements(GL_TRIANGLES, g.indices.capacity(), GL_UNSIGNED_INT, 0);
        glDisable(GL_CULL_FACE);
        
        App.checkGLError();
    }

    @Override
    void destroy() {
    }

}