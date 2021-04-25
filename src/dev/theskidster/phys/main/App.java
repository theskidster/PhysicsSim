package dev.theskidster.phys.main;

import dev.theskidster.jlogger.JLogger;
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
    
    private static int tickCount = 0;
    
    private static boolean vSync = true;
    private static boolean showMetrics = true;
    
    public static final String DOMAIN  = "phys";
    public static final String VERSION = "0.0.0";
    
    private final Monitor monitor;
    private final Window window;
    private final GLProgram hudProgram;
    private final GLProgram sceneProgram;
    private final GLProgram depthProgram;
    private final HUD hud;
    private final Camera camera;
    private final ShadowMap shadowMap;
    private static Scene scene;
    
    /**
     * Initializes utilities such as the window and shader program used by the application.
     */
    App() {
        if(!glfwInit()) {
            JLogger.logSevere("Failed to initialize GLFW.", null);
        }
        
        if(!System.getProperty("os.name").toLowerCase().contains("win")) {
            JLogger.logSevere("Unsupported operating system. Use a 64 bit Windows system.", null);
        } else {
            if(!System.getProperty("os.arch").contains("64")) {
                JLogger.logSevere("Unsupported architecture. Windows system must be 64 bit.", null);
            }
        }
        
        monitor = new Monitor();
        window  = new Window("ode4j testbed v" + VERSION, monitor);
        
        glfwMakeContextCurrent(window.handle);
        GL.createCapabilities();
        
        JLogger.newHorizontalLine();
        JLogger.logInfo("OS NAME:\t\t" + System.getProperty("os.name"));
        JLogger.logInfo("JAVA VERSION:\t" + System.getProperty("java.version"));
        JLogger.logInfo("GLFW VERSION:\t" + glfwGetVersionString());
        JLogger.logInfo("OPENGL VERSION:\t" + glGetString(GL_VERSION));
        JLogger.logInfo("APP VERSION:\t" + VERSION);
        JLogger.newHorizontalLine();
        JLogger.newLine();
        
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
            sceneProgram.addUniform(BufferType.VEC3, "uLightPos");
            sceneProgram.addUniform(BufferType.MAT3, "uNormal");
            sceneProgram.addUniform(BufferType.MAT4, "uModel");
            sceneProgram.addUniform(BufferType.MAT4, "uView");
            sceneProgram.addUniform(BufferType.MAT4, "uProjection");
            sceneProgram.addUniform(BufferType.MAT4, "uLightSpace");
        }
        
        //Establish the shader for the shadow map.
        {
            var shaderSourceFiles = new LinkedList<Shader>() {{
                add(new Shader("depthVertex.glsl", GL_VERTEX_SHADER));
                add(new Shader("depthFragment.glsl", GL_FRAGMENT_SHADER));
            }};
            
            depthProgram = new GLProgram(shaderSourceFiles, "depth");
            depthProgram.use();
            
            depthProgram.addUniform(BufferType.MAT4, "uModel");
            depthProgram.addUniform(BufferType.MAT4, "uLightSpace");
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
            JLogger.logSevere("failed to copy dll file", e);
        }
        
        hud       = new HUD(cwd);
        camera    = new Camera();
        shadowMap = new ShadowMap();
        Scene.setCameraReference(camera);
        OdeHelper.initODE2(0);
    }
    
    /**
     * Exposes window and starts the applications main logic loop.
     */
    void start() {
        setScene(new SceneGravityTest());
        window.show(monitor, hud, camera);
        
        //Variables for timestep
        int cycles = 0;
        int fps = 0;
        final double TARGET_DELTA = 1 / 60.0;
        double prevTime = glfwGetTime();
        double currTime;
        double delta = 0;
        double deltaMetric = 0;
        boolean ticked;
        
        while(!glfwWindowShouldClose(window.handle)) {
            currTime = glfwGetTime();
            
            delta += currTime - prevTime;
            if(delta < TARGET_DELTA && vSync) delta = TARGET_DELTA;
            
            prevTime = currTime;
            ticked   = false;
            
            while(delta >= TARGET_DELTA) {
                deltaMetric = delta;
                
                delta -= TARGET_DELTA;
                ticked = true;
                tickCount = (tickCount == Integer.MAX_VALUE) ? 0 : tickCount + 1;
                
                glfwPollEvents();
                
                scene.update();
                
                if(tick(60)) {
                    fps    = cycles;
                    cycles = 0;
                }
            }
            
            shadowMap.createMap(camera.up, depthProgram, scene);
            
            glViewport(0, 0, window.getWidth(), window.getHeight());
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            sceneProgram.use();
            camera.render(sceneProgram);
            sceneProgram.setUniform("uLightPos", shadowMap.lightPos);
            sceneProgram.setUniform("uLightSpace", false, shadowMap.lightSpace);
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, shadowMap.depthTexHandle);
            scene.render(sceneProgram);
            
            hudProgram.use();
            hud.setProjectionMatrix(hudProgram);
            scene.renderHUD(hudProgram, hud.font);
            
            if(showMetrics) {
                hud.font.drawString("FPS: " + fps, 12, window.getHeight() - 20, Color.WHITE, hudProgram);
                hud.font.drawString("DELTA: " + (float) deltaMetric, 12, window.getHeight() - 40, Color.WHITE, hudProgram);
                hud.font.drawString("TICKED: " + ticked, 12, window.getHeight() - 60, Color.WHITE, hudProgram);
                hud.font.drawString("VSYNC: " + vSync, 12, window.getHeight() - 80, Color.YELLOW, hudProgram);
                hud.font.drawString("MONITOR: " + monitor.info, 12, window.getHeight() - 100, Color.YELLOW, hudProgram);
                hud.font.drawString("MEM FREE: " + Runtime.getRuntime().freeMemory(), 12, window.getHeight() - 120, Color.CYAN, hudProgram);
            }
            
            glfwSwapBuffers(window.handle);
            
            if(!ticked) {
                try {
                    Thread.sleep(1);
                } catch(InterruptedException e) {}
            } else {
                cycles++;
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
            
            JLogger.logSevere("OpenGL Error: (" + glError + ") " + desc, null);
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
        
        JLogger.logInfo("Entered scene: \"" + scene.name + "\"");
    }
    
    /**
     * Ticks (returns true) whenever the number of cycles has been reached. Intended to be used in if statements for systems that don't require the decoupled 
     * precision of the {@link dev.theskidster.xjge.util.Timer Timer} class.
     * 
     * @param cycles the number of cycles until a tick occurs
     * @return true every time the number of cycles is reached
     */
    public static boolean tick(int cycles) {
        return tickCount % cycles == 0;
    }
    
    public static void toggleMetrics() {
        showMetrics = !showMetrics;
    }
    
}