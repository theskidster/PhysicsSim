package dev.theskidster.phys.scene;

import dev.theskidster.phys.entities.EntityGround;
import dev.theskidster.phys.entities.EntityCube;
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
    }

    @Override
    public void renderHUD(GLProgram hudProgram, FreeTypeFont font) {
    }
    
    @Override
    public void exit() {
    }

}