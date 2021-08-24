package dev.theskidster.phys.commands;

import dev.theskidster.phys.main.App;
import dev.theskidster.phys.scene.SceneGravityTest;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

/**
 * Aug 24, 2021
 */

/**
 * @author J Hoffman
 * @since  
 */
public class ResetScene extends Command {

    private final String sceneName;
    
    public ResetScene(String sceneName) {
        this.sceneName = sceneName;
    }
    
    @Override
    public void execute(int key, int action, int mods) {
        if(keyPressedOnce(key) && key == GLFW_KEY_SPACE) {
            switch(sceneName) {
                case "gravity test" -> { App.setScene(new SceneGravityTest(false)); }
            }
        }
    }

}
