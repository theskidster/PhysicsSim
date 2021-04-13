package dev.theskidster.phys.main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

/**
 * Encapsulates an OpenGL shader object- these objects contain the compiled results of a .glsl source code file and provides it as a single stage of a much 
 * larger {@linkplain GLProgram}.
 */
final class Shader {

    final int handle;
    
    /**
     * Parses a .glsl source code file then utilizes the stage specified to produce a new OpenGL shader object.
     * 
     * @param filename the name of the .glsl file to parse
     * @param stage    the stage of the shader process this object will describe. One of; {@link org.lwjgl.opengl.GL30#GL_VERTEX_SHADER GL_VERTEX_SHADER}, 
     *                 {@link org.lwjgl.opengl.GL30#GL_FRAGMENT_SHADER GL_FRAGMENT_SHADER}, 
     *                 {@link org.lwjgl.opengl.GL32#GL_GEOMETRY_SHADER GL_GEOMETRY_SHADER}, 
     *                 {@link org.lwjgl.opengl.GL40#GL_TESS_CONTROL_SHADER GL_TESS_CONTROL_SHADER}, 
     *                 {@link org.lwjgl.opengl.GL40#GL_TESS_EVALUATION_SHADER GL_TESS_EVALUATION_SHADER}, or 
     *                 {@link org.lwjgl.opengl.GL43#GL_COMPUTE_SHADER GL_COMPUTE_SHADER}. 
     */
    Shader(String filename, int stage) {
        String filepath       = "/dev/theskidster/" + App.DOMAIN + "/shaders/" + filename;
        StringBuilder builder = new StringBuilder();
        InputStream file      = Shader.class.getResourceAsStream(filepath);
        
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(file, "UTF-8"))) {
            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch(Exception e) {
            Logger.logSevere("Failed to parse GLSL file: \"" + filename + "\"", e);
        }
        
        CharSequence sourceCode = builder.toString();
        
        handle = glCreateShader(stage);
        glShaderSource(handle, sourceCode);
        glCompileShader(handle);
        
        if(glGetShaderi(handle, GL_COMPILE_STATUS) != GL_TRUE) {
            Logger.logSevere("Failed to compile GLSL file: \"" + filename + "\" " + glGetShaderInfoLog(handle), null);
        }
    }
    
}