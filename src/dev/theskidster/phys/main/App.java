package dev.theskidster.phys.main;

import dev.theskidster.phys.scene.Scene;
import dev.theskidster.phys.scene.SceneGravityTest;
import dev.theskidster.shadercore.BufferType;
import dev.theskidster.shadercore.GLProgram;
import dev.theskidster.shadercore.Shader;
import dev.theskidster.shadercore.ShaderCore;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL20.*;
import org.ode4j.ode.OdeHelper;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

/**
 * Static class which contains all state directly concerned with the applications execution.
 */
public final class App {
    
    private static boolean vSync = true;
    
    public static final String DOMAIN  = "phys";
    public static final String VERSION = "0.0.0";
    
    private final Monitor monitor;
    private final Window window;
    private final GLProgram hudProgram;
    private final GLProgram sceneProgram;
    private final HUD hud;
    private final Camera camera;
    private static Scene scene;
    
    /**
     * Initializes utilities such as the window and shader program used by the application.
     */
    App() {
        if(!glfwInit()) {
            Logger.logSevere("Failed to initialize GLFW.", null);
        }
        
        if(!System.getProperty("os.name").toLowerCase().contains("win")) {
            Logger.logSevere("Unsupported operating system. Use a 64 bit Windows system.", null);
        } else {
            if(!System.getProperty("os.arch").contains("64")) {
                Logger.logSevere("Unsupported architecture. Windows system must be 64 bit.", null);
            }
        }
        
        monitor = new Monitor();
        window  = new Window("ode4j testbed v" + VERSION, monitor);
        
        glfwMakeContextCurrent(window.handle);
        GL.createCapabilities();
        
        ShaderCore.setFilepath("/dev/theskidster/phys/shaders/");
        
        //Establish the shader for the applications heads up display (hud).
        {
            var shaderSourceFiles = new LinkedList<Shader>() {{
                add(new Shader("hudVertex.glsl", GL_VERTEX_SHADER));
                add(new Shader("hudFragment.glsl", GL_FRAGMENT_SHADER));
            }};
            
            hudProgram = new GLProgram(shaderSourceFiles, "hud");
            hudProgram.use();
            
            hudProgram.addUniform(BufferType.INT,  "uType");
            hudProgram.addUniform(BufferType.VEC3, "uColor");
            hudProgram.addUniform(BufferType.MAT4, "uProjection");
        }
        
        //Establish the shader that will be used to draw the 3D scene.
        {
            var shaderSourceFiles = new LinkedList<Shader>() {{
                add(new Shader("sceneVertex.glsl", GL_VERTEX_SHADER));
                add(new Shader("sceneFragment.glsl", GL_FRAGMENT_SHADER));
            }};
            
            sceneProgram = new GLProgram(shaderSourceFiles, "scene");
            sceneProgram.use();
            
            sceneProgram.addUniform(BufferType.INT,  "uType");
            sceneProgram.addUniform(BufferType.VEC3, "uColor");
            sceneProgram.addUniform(BufferType.MAT3, "uNormal");
            sceneProgram.addUniform(BufferType.MAT4, "uModel");
            sceneProgram.addUniform(BufferType.MAT4, "uView");
            sceneProgram.addUniform(BufferType.MAT4, "uProjection");
        }
        
        String cwd = Path.of("").toAbsolutePath().toString() + "\\freetype-jni-64.dll";
        
        /*
        I prefer to localize my dependencies so things are easier to find- here
        I copy the native freetype .dll to the current working directory of the 
        application so we dont need to install it in the users JAVA_HOME.
        */
        try {
            InputStream source = App.class.getResourceAsStream("/dev/theskidster/" + DOMAIN + "/assets/freetype-jni-64.dll");
            Files.copy(source, Paths.get(cwd), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Logger.logSevere("failed to copy dll file", e);
        }
        
        hud    = new HUD(cwd);
        camera = new Camera();
        Scene.setCameraReference(camera);
        OdeHelper.initODE2(0);
    }
    
    /**
     * Exposes window and starts the applications main logic loop.
     */
    void start() {
        Logger.logSystemInfo();
        setScene(new SceneGravityTest());
        window.show(monitor, hud, camera);
        
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
                
                scene.update();
            }
            
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            sceneProgram.use();
            camera.render(sceneProgram);
            scene.render(sceneProgram);
            
            hudProgram.use();
            hud.setProjectionMatrix(hudProgram);
            scene.renderHUD(hudProgram, hud.font);
            
            glfwSwapBuffers(window.handle);
            
            if(!ticked) {
                try {
                    Thread.sleep(1);
                } catch(InterruptedException e) {}
            }
        }
        
        GL.destroy();
        OdeHelper.closeODE();
        glfwTerminate();
    }
    
    /**
     * Gracefully terminates application execution.
     */
    void exit() {
        glfwSetWindowShouldClose(window.handle, true);
    }
    
    /**
     * Checks for possible errors in the current OpenGL state.
     */
    public static void checkGLError() {
        int glError = glGetError();
        
        if(glError != GL_NO_ERROR) {
            String desc = "";
            
            switch(glError) {
                case GL_INVALID_ENUM      -> desc = "invalid enum";
                case GL_INVALID_VALUE     -> desc = "invalid value";
                case GL_INVALID_OPERATION -> desc = "invalid operation";
                case GL_STACK_OVERFLOW    -> desc = "stack overflow";
                case GL_STACK_UNDERFLOW   -> desc = "stack underflow";
                case GL_OUT_OF_MEMORY     -> desc = "out of memory";
            }
            
            Logger.logSevere("OpenGL Error: (" + glError + ") " + desc, null);
        }
    }
    
    /**
     * Changes the current scene.
     * 
     * @param value the new scene to enter
     */
    public static void setScene(Scene value) {
        if(scene != null) scene.exit();
        scene = value;
        
        Logger.logInfo("Entered scene: \"" + scene.name + "\"");
    }
    
}