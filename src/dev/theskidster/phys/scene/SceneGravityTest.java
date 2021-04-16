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

public class SceneGravityTest extends Scene {

    private final DWorld dWorld = OdeHelper.createWorld();
    private final DSpace dSpace = OdeHelper.createHashSpace(null);
    
    public SceneGravityTest() {
        super("Gravity Test");
        
        setCameraPosition(6, 4, 10);
        setCameraDirection(-120f, 20);
        
        //https://www.alsprogrammingresource.com/basic_ode.html
        //http://ode.org/wiki/index.php?title=Manual
        //https://learnopengl.com/Advanced-Lighting/Shadows/Shadow-Mapping
        
        //TODO: might bundle these together
        addEntity("ground", new EntityGround());
        addEntity("test", new EntityTest(new Vector3f(0, 1, -5)));
        OdeHelper.createPlane(dSpace, 0, 0, 1, 0);
        
        //TODO: drop cube on plane.
    }

    @Override
    public void renderHUD(GLProgram hudProgram, FreeTypeFont font) {
        font.drawString("bleh", 40, 40, Color.WHITE, hudProgram);
    }
    
    @Override
    public void exit() {
    }

}