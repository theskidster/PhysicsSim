package dev.theskidster.phys.scene;

import dev.theskidster.phys.main.FreeTypeFont;
import dev.theskidster.shadercore.GLProgram;

/**
 * @author J Hoffman
 * Created: Apr 24, 2021
 */

public class SceneBouncingBall extends Scene {

    public SceneBouncingBall() {
        super("Bouncing Ball");
        
        ////http://ode.org/wiki/index.php?title=Manual
    }

    @Override
    public void renderHUD(GLProgram hudProgram, FreeTypeFont font) {
    }

    @Override
    public void exit() {
    }

}