package dev.theskidster.phys.scene;

import dev.theskidster.phys.main.GLProgram;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Apr 14, 2021
 */

/**
 * Abstract class which can be used to create dynamic objects within the {@linkplain dev.theskidster.phys.scene.Scene Scene}.
 */
abstract class Entity {

    private boolean remove;
    
    Vector3f position;
    
    /**
     * Creates a new entity object at the specified position within the 3D scene.
     * 
     * @param position the position that this entity will be placed initially
     */
    Entity(Vector3f position) {
        this.position = position;
    }
    
    /**
     * Used to organize the entities physics logic. Called exclusively through the 
     * {@linkplain dev.theskidster.phys.scene.Scene#update() Scene.update()} method.
     */
    abstract void update();
    
    /**
     * Used to organize the calls made to the graphics API by this entity. Called exclusively through the 
     * {@linkplain dev.theskidster.phys.scene.Scene#render(GLProgram) Scene.render()} method.
     * 
     * @param sceneProgram 
     */
    abstract void render(GLProgram sceneProgram);
    
    /**
     * Used to free all of the resources allocated by this entity once it is no longer needed. Calls to methods like 
     * {@linkplain dev.theskidster.phys.graphics.Graphics#freeBuffers() Graphics.freeBuffers()} should be made here.
     */
    abstract void destroy();
    
    /**
     * Checks whether the entity has made a request to be removed. If it has, the entity will free all of the resources its allocated and be removed from the
     * {@linkplain dev.theskidster.phys.scene.Scene Scene}.
     * 
     * @return true if the entities {@linkplain remove()} method has been invoked
     */
    boolean removalRequested() {
        if(remove) destroy();
        return remove;
    }
    
    /**
     * Requests the {@linkplain destroy() destruction} and {@linkplain removalRequested() removal} of this entity.
     */
    void remove() { remove = true; }
    
}