package dev.theskidster.phys.scene;

import dev.theskidster.phys.graphics.FreeTypeFont;
import dev.theskidster.phys.main.Camera;
import dev.theskidster.phys.main.GLProgram;
import dev.theskidster.phys.main.Window;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

/**
 * Represents a 3D space which can contain many {@linkplain Entity} objects.
 */
public abstract class Scene {
    
    protected static int vpWidth;
    protected static int vpHeight;
    
    public final String name;
    
    private final Map<String, Entity> entityMap = new LinkedHashMap();
    
    private static Camera camera;
    
    /**
     * Constructs a new 3D space. All resources that will be used by the scene should be initialized here.
     * 
     * @param name unique string that will be used to identify this scene
     */
    public Scene(String name) {
        this.name = name;
    }
    
    /**
     * Renders the custom HUD provided by the scene. Used to relay scene-specific information to the user, such as the controls for moving an entity with the
     * keyboard.
     * 
     * @param hudProgram the GLProgram to use
     * @param font       the font object supplied by the applications heads up display that's used to render text to the window
     */
    public abstract void renderHUD(GLProgram hudProgram, FreeTypeFont font);
    
    /**
     * Called when the scene is exited. Used in a similar fashion to {@linkplain Entity#destroy()}.
     */
    public abstract void exit();
    
    /**
     * Updates the internal logic of the scene including its {@linkplain Entity} objects.
     */
    public void update() {
        entityMap.values().forEach(entity -> entity.update());
        entityMap.values().removeIf(entity -> entity.removalRequested());
    }
    
    /**
     * Uses the shader program supplied to render the scene and its various {@linkplain Entity entities}.
     * 
     * @param sceneProgram the shader program that the scene will use to render objects 
     */
    public void render(GLProgram sceneProgram) {
        entityMap.values().forEach(entity -> entity.render(sceneProgram));
    }
    
    /**
     * Static method used to update fields that can be used to offset the position of text drawn by the 
     * {@linkplain renderHUD(GLProgram, FreeTypeFont) renderHUD()} method.
     * 
     * @param width  the current width of the applications window in pixels
     * @param height the current height of the applications window in pixels
     */
    public static void updateViewport(int width, int height) {
        vpWidth  = width;
        vpHeight = height;
    }
    
    /**
     * Sets a reference to the applications {@linkplain dev.theskidster.phys.main.Camera Camera} object so we can manipulate its position and direction 
     * within the scene.
     * 
     * @param reference the camera object initialized by the App class
     */
    public static void setCameraReference(Camera reference) {
        camera = reference;
    }
    
    /**
     * Sets the initial position of the applications camera when it enters the scene.
     * 
     * @param x the cameras new position along the x-axis
     * @param y the cameras new position along the y-axis
     * @param z the cameras new position along the z-axis
     */
    final void setCameraPosition(float x, float y, float z) {
        camera.setPosition(x, y, z);
    }
    
    /**
     * Sets the initial direction the applications camera will face when it enters the scene.
     * 
     * @param yaw   the left to right direction of the camera. Expects a value between -180 and 180 degrees.
     * @param pitch the up and down direction of the camera. Expects a value between -90 and 90 degrees.
     */
    final void setCameraDirection(float yaw, float pitch) {
        camera.setDirection(yaw, pitch, Window.getMouseX(), Window.getMouseY());
    }
    
    /**
     * Adds an entity to this scene.
     * 
     * @param name   the unique name that will be used to reference the entity being added
     * @param entity the entity to add to this scene
     */
    final void addEntity(String name, Entity entity) {
        entityMap.put(name, entity);
    }
    
}