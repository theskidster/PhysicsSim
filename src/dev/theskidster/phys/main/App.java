package dev.theskidster.phys.main;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

/**
 * Static class which contains all state directly concerned with the applications execution.
 */
public final class App {
    
    private static boolean vSync = true;
    
    public static final String VERSION = "0.0.0";
    
    private final Monitor monitor;
    private final Window window;
    
    /**
     * Initializes utilities such as the window and shader program used by the application.
     */
    App() {
        if(!glfwInit()) {
            Logger.logSevere("Failed to initialize GLFW.", null);
        }
        
        monitor = new Monitor();
        window  = new Window("ode4j testbed v" + VERSION, monitor);
        
        glfwMakeContextCurrent(window.handle);
        GL.createCapabilities();
        
        //Establish the applications shader program that will render scenes.
        {
            
        }
        
        
    }
    
    /**
     * Exposes window and starts the applications main logic loop.
     */
    void start() {
        window.show(monitor);
        
        Logger.logSystemInfo();
        
        //Variables for timestep
        int tickCount = 0;
        final double TARGET_DELTA = 1 / 60.0;
        double prevTime = glfwGetTime();
        double currTime;
        double delta = 0;
        boolean ticked;
        
        while(!glfwWindowShouldClose(window.handle)) {
            currTime = glfwGetTime();
            
            delta += currTime - prevTime;
            if(delta < TARGET_DELTA && vSync) delta = TARGET_DELTA;
            
            prevTime = currTime;
            ticked   = false;
            
            while(delta >= TARGET_DELTA) {
                delta -= TARGET_DELTA;
                ticked = true;
                tickCount = (tickCount == Integer.MAX_VALUE) ? 0 : tickCount + 1;
                
                glfwPollEvents();
                
                //TODO: update scene
            }
            
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            //TODO: render scene;
            
            glfwSwapBuffers(window.handle);
            
            if(!ticked) {
                try {
                    Thread.sleep(1);
                } catch(InterruptedException e) {}
            }
        }
        
        GL.destroy();
        glfwTerminate();
    }
    
    /**
     * Gracefully terminates application execution.
     */
    void exit() {
        glfwSetWindowShouldClose(window.handle, true);
    }
    
}