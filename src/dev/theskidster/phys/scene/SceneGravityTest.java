package dev.theskidster.phys.scene;

import dev.theskidster.phys.main.Color;
import dev.theskidster.phys.main.FreeTypeFont;
import dev.theskidster.shadercore.GLProgram;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

public class SceneGravityTest extends Scene {
    
    public SceneGravityTest() {
        super("Gravity Test");
        
        setCameraPosition(6, 4, 10);
        setCameraDirection(-120, 20);
        
        dWorld.setGravity(0, -3, 0);
        dWorld.setERP(0.2);
        dWorld.setCFM(1e-5);
        dWorld.setContactMaxCorrectingVel(0.9);
        dWorld.setContactSurfaceLayer(0.001);
        dWorld.setAutoDisableFlag(true);
        
        addEntity("ground", new EntityGround(0, 1, 0, 0, dWorld, dSpace));
        addEntity("cube 1", new EntityCube(new Vector3f(0, 6, 0), 1, 1, 1, dWorld, dSpace));
        addEntity("cube 2", new EntityCube(new Vector3f(0.5f, 10, 0), 1, 1, 1, dWorld, dSpace));
        addEntity("cube 3", new EntityCube(new Vector3f(0, 12, 0.7f), 1, 1, 1, dWorld, dSpace));
        addEntity("cube 4", new EntityCube(new Vector3f(0, 6, 4f), 1, 1, 1, dWorld, dSpace));
        addEntity("cube 5", new EntityCube(new Vector3f(0, 2, 4f), 1, 1, 1, dWorld, dSpace));
        addEntity("cube 6", new EntityCube(new Vector3f(2, 12, 0.7f), 1, 1, 1, dWorld, dSpace));
        addEntity("cube 7", new EntityCube(new Vector3f(2.5f, 6, 0.7f), 1, 1, 1, dWorld, dSpace));
        addEntity("cube 8", new EntityCube(new Vector3f(1, 3, 0.9f), 1, 1, 1, dWorld, dSpace));
        addEntity("cube 9", new EntityCube(new Vector3f(1, 3, 0.9f), 1, 1, 1, dWorld, dSpace));
        addEntity("cube 10", new EntityCube(new Vector3f(1, 3, 0.9f), 1, 1, 1, dWorld, dSpace));
        addEntity("cube 11", new EntityCube(new Vector3f(1, 3, 0.9f), 1, 1, 1, dWorld, dSpace));
        addEntity("cube 12", new EntityCube(new Vector3f(1, 3, 0.9f), 1, 1, 1, dWorld, dSpace));
        addEntity("cube 13", new EntityCube(new Vector3f(1, 3, 0.9f), 1, 1, 1, dWorld, dSpace));
        addEntity("cube 14", new EntityCube(new Vector3f(1, 3.5f, 0.7f), 1, 1, 1, dWorld, dSpace));
        addEntity("cube 15", new EntityCube(new Vector3f(1, 3.5f, 0.9f), 1, 1, 1, dWorld, dSpace));
    }

    @Override
    public void renderHUD(GLProgram hudProgram, FreeTypeFont font) {
        font.drawString("bleh", 40, 40, Color.WHITE, hudProgram);
    }
    
    @Override
    public void exit() {
        //TODO: free resources
    }

}