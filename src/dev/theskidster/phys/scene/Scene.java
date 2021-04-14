package dev.theskidster.phys.scene;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

public abstract class Scene {

    //TODO: add documentation.
    
    public final String name;
    
    public Scene(String name) {
        this.name = name;
    }
    
    public abstract void enter();
    
    public abstract void exit();
    
    public void update() {
        
    }
    
    public void render() {
        
    }
    
}