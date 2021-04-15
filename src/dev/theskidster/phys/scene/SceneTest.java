package dev.theskidster.phys.scene;

import dev.theskidster.phys.entities.TestEntity;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

public class SceneTest extends Scene {

    public SceneTest() {
        super("Test Scene");
    }

    @Override
    public void enter() {
        addEntity("test entity", new TestEntity(new Vector3f(0, 0, -10)));
    }

    @Override
    public void exit() {
    }

}