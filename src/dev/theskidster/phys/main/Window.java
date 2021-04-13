package dev.theskidster.phys.main;

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
final class Window {

    private final int initialPosX;
    private final int initialPosY;
    private int width  = 1280;
    private int height = 720;
    
    final long handle;
    
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
    
    void show(Monitor monitor) {
        glfwSetWindowMonitor(handle, NULL, initialPosX, initialPosY, width, height, monitor.refreshRate);
        glfwSetWindowPos(handle, initialPosX, initialPosY);
        glfwSwapInterval(1);
        glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        glfwShowWindow(handle);
        
        glfwSetWindowSizeCallback(handle, (window, w, h) -> {
            width  = w;
            height = h;
            glViewport(0, 0, width, height);
        });
        
    }
    
}