package dev.theskidster.phys.main;

import com.mlomb.freetypejni.FreeType;
import com.mlomb.freetypejni.Library;
import dev.theskidster.phys.graphics.FreeTypeFont;
import org.joml.Matrix4f;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

/**
 * The heads up display is responsible for providing the user with important information at runtime using the various elements it encapsulates, such as text.
 */
public class HUD {
    
    private final Library freeType;
    public final FreeTypeFont font;
    private final Matrix4f projMatrix = new Matrix4f();
    
    /**
     * Initializes elements which can be used to relay information to the user at runtime.
     * 
     * @param cwd the current working directory of the application
     */
    HUD(String cwd) {
        freeType = FreeType.newLibrary(cwd);
        font     = new FreeTypeFont(freeType, "fnt_inconsolata_regular.ttf", 20);
    }
    
    /**
     * Updates the HUDs projection matrix to reflect the current dimensions of the {@linkplain Window}.
     * 
     * @param width  the current width of the applications window in pixels
     * @param height the current height of the applications window in pixels
     */
    public void updateViewport(int width, int height) {
        projMatrix.setOrtho(0, width, 0, height, 0, Integer.MAX_VALUE);
    }
    
    /**
     * Supplies the value of the HUDs projection matrix to the current {@linkplain GLProgram GLProgram}.
     * 
     * @param hudProgram the GLProgram to use
     */
    public void setProjectionMatrix(GLProgram hudProgram) {
        hudProgram.setUniform("uProjection", false, projMatrix);
    }
    
}