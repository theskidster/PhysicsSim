package dev.theskidster.phys.scene;

import dev.theskidster.phys.graphics.Color;
import dev.theskidster.phys.graphics.FreeTypeFont;
import dev.theskidster.phys.main.GLProgram;
import org.joml.Vector3f;
import org.ode4j.ode.DSpace;
import org.ode4j.ode.DWorld;
import org.ode4j.ode.OdeHelper;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

public class SceneTest extends Scene {

    private final DWorld dWorld = OdeHelper.createWorld();
    private final DSpace dSpace = OdeHelper.createHashSpace(null);
    
    public SceneTest() {
        super("Test Scene");
        
        addEntity("test entity", new EntityTest(new Vector3f(0, 0, -10)));
        addEntity("ground", new EntityGround());
        
        OdeHelper.createPlane(dSpace, 0, 0, 1, 0);
    }

    @Override
    public void renderHUD(GLProgram hudProgram, FreeTypeFont font) {
        font.drawString("bleh", 40, 40, Color.WHITE, hudProgram);
    }
    
    @Override
    public void exit() {
    }

}