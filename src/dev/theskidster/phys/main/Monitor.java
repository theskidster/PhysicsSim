package dev.theskidster.phys.main;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import org.lwjgl.glfw.GLFWVidMode;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

/**
 * Data structure which contains a peripheral display devices dimensions, refresh rate, and video output information. 
 */
final class Monitor {

    final int width;
    final int height;
    final int refreshRate;
    
    final long handle;
    
    final String info;
    
    private final GLFWVidMode videoMode;
    
    /**
     * Parses information about the primary display device, and provides it as an object.
     */
    Monitor() {        
        handle    = glfwGetPrimaryMonitor();
        videoMode = glfwGetVideoMode(handle);
        
        width       = videoMode.width();
        height      = videoMode.height();
        refreshRate = videoMode.refreshRate();
        
        info = width + "x" + height + " " + refreshRate + "hz";
    }
    
}