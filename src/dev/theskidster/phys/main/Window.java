package dev.theskidster.phys.main;

import dev.theskidster.phys.scene.Scene;
import dev.theskidster.phys.scene.SceneGravityTest;
import java.nio.IntBuffer;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

/**
 * Represents the applications window.
 */
public final class Window {

    private final int initialPosX;
    private final int initialPosY;
    private int width  = 1280;
    private int height = 720;
    
    private static float mousePosX;
    private static float mousePosY;
    
    final long handle;
    
    private boolean mouseMiddleHeld;
    private boolean mouseRightHeld;
    
    /**
     * Creates a new window object that will display the applications visual output.
     * 
     * @param title   the title that will be used to identify the window
     * @param monitor the monitor that this window will appear on
     */
    Window(String title, Monitor monitor) {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer xStartBuf = stack.mallocInt(1);
            IntBuffer yStartBuf = stack.mallocInt(1);
            
            glfwGetMonitorPos(monitor.handle, xStartBuf, yStartBuf);
            
            initialPosX = Math.round((monitor.width - width) / 2) + xStartBuf.get();
            initialPosY = Math.round((monitor.height - height) / 2) + yStartBuf.get();
        }
        
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        
        handle = glfwCreateWindow(width, height, title, NULL, NULL);
    }
    
    void show(Monitor monitor, HUD hud, Camera camera) {
        glfwSetWindowMonitor(handle, NULL, initialPosX, initialPosY, width, height, monitor.refreshRate);
        glfwSetWindowPos(handle, initialPosX, initialPosY);
        glfwSwapInterval(1);
        glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        glfwShowWindow(handle);
        
        //Set initial viewport demensions for the HUD.
        hud.updateViewport(width, height);
        camera.updateViewport(width, height);
        Scene.updateViewport(width, height);
        
        glfwSetWindowSizeCallback(handle, (window, w, h) -> {
            width  = w;
            height = h;
            
            glViewport(0, 0, width, height);
            hud.updateViewport(width, height);
            camera.updateViewport(width, height);
            Scene.updateViewport(width, height);
        });
        
        glfwSetCursorPosCallback(handle, (window, x, y) -> {
            if(mouseMiddleHeld ^ mouseRightHeld) {
                if(mouseMiddleHeld) camera.setPosition(x, y);
                if(mouseRightHeld)  camera.setDirection(x, y);
            } else {
                camera.prevX = x;
                camera.prevY = y;
            }
            
            mousePosX = (float) x;
            mousePosY = (float) y;
        });
        
        glfwSetMouseButtonCallback(handle, (window, button, action, mods) -> {
            switch(button) {
                case GLFW_MOUSE_BUTTON_MIDDLE -> mouseMiddleHeld = (action == GLFW_PRESS);
                case GLFW_MOUSE_BUTTON_RIGHT  -> mouseRightHeld = (action == GLFW_PRESS);
            }
        });
        
        glfwSetScrollCallback(handle, (window, xOffset, yOffset) -> {
            camera.dolly((float) yOffset);
        });
        
        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            //TODO: add command input mapping
            
            if(key == GLFW_KEY_SPACE && action == GLFW_PRESS) App.setScene(new SceneGravityTest());
        });
    }
    
    /**
     * Provides the current horizontal position of the mouse cursor in the window.
     * 
     * @return the current position of the cursor along the x-axis
     */
    public static float getMouseX() {
        return mousePosX;
    }
    
    /**
     * Provides the current vertical position of the mouse cursor in the window.
     * 
     * @return the current position of the cursor along the y-axis
     */
    public static float getMouseY() {
        return mousePosY;
    }
    
}