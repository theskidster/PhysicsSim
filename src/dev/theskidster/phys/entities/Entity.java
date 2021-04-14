package dev.theskidster.phys.entities;

import dev.theskidster.phys.main.GLProgram;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Apr 14, 2021
 */

public abstract class Entity {

    private boolean remove;
    
    public Vector3f position;
    
    public Entity(Vector3f position) {
        this.position = position;
    }
    
    public abstract void update();
    
    public abstract void render(GLProgram sceneProgram);
    
    public abstract void destroy();
    
    public boolean removalRequested() {
        if(remove) destroy();
        return remove;
    }
    
    public void remove() { remove = true; }
    
}