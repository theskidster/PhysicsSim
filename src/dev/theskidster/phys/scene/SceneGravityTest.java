package dev.theskidster.phys.scene;

import dev.theskidster.phys.commands.ResetScene;
import dev.theskidster.phys.main.FreeTypeFont;
import dev.theskidster.phys.main.Puppet;
import dev.theskidster.phys.main.Window;
import dev.theskidster.shadercore.GLProgram;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

public class SceneGravityTest extends Scene {
    
    public Puppet puppet = new Puppet();
    
    public SceneGravityTest(boolean setInitialCamera) {
        super("Gravity Test");
        
        if(setInitialCamera) {
            setCameraPosition(6, 4, 10);
            setCameraDirection(-120, 20);
        }
        
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
        
        puppet.commands.add(new ResetScene("gravity test"));
        
        Window.setPuppet(puppet);
    }

    @Override
    public void renderHUD(GLProgram hudProgram, FreeTypeFont font) {
    }
    
    @Override
    public void exit() {
    }

}