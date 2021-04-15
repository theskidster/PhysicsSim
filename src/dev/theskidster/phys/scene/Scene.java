package dev.theskidster.phys.scene;

import dev.theskidster.phys.entities.Entity;
import dev.theskidster.phys.graphics.FreeTypeFont;
import dev.theskidster.phys.main.GLProgram;
import java.util.HashMap;
import java.util.Map;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

public abstract class Scene {

    //TODO: add documentation.
    
    static {
        //TODO: add skybox and coulds
    }
    
    public final String name;
    
    private final Map<String, Entity> entityMap = new HashMap();
    
    public Scene(String name) {
        this.name = name;
    }
    
    public abstract void enter();
    
    public abstract void renderHUD(GLProgram hudProgram, FreeTypeFont font);
    
    public abstract void exit();
    
    public void update() {
        entityMap.values().forEach(entity -> entity.update());
        entityMap.values().removeIf(entity -> entity.removalRequested());
    }
    
    public void render(GLProgram sceneProgram) {
        entityMap.values().forEach(entity -> entity.render(sceneProgram));
    }
    
    protected void addEntity(String name, Entity entity) {
        entityMap.put(name, entity);
    }
    
}