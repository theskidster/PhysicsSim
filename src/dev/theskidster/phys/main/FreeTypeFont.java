package dev.theskidster.phys.main;

import com.mlomb.freetypejni.Face;
import static com.mlomb.freetypejni.FreeTypeConstants.FT_LOAD_RENDER;
import com.mlomb.freetypejni.Library;
import dev.theskidster.jlogger.JLogger;
import dev.theskidster.shadercore.GLProgram;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryStack;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

/**
 * The FreeTypeFont class represents fonts which can be used to render text. Objects of this type will parse the file provided to their constructor and 
 * generate a series of {@linkplain Glyph glyphs} that are later assembled into words and displayed on the screen.
 */
public final class FreeTypeFont {

    /**
     * Data structure used to store information about individual letters.
     */
    private final class Glyph {
        int texHandle;
        int advance;
        int width;
        int height;
        int bearingX;
        int bearingY;
    }
    
    private final int FLOATS_PER_GLYPH = 24;
    
    private final int vao = glGenVertexArrays();
    private final int vbo = glGenBuffers();
    
    private static final Map<Character, Glyph> glyphs = new HashMap<>();
    
    /**
     * Generates a new renderable font using the data from the file provided.
     * 
     * @param freeType a reference to the freetype library that helps parse font information
     * @param filename the name of the font file to parse. Extension is expected.
     * @param size     the desired size of the font in pixels
     */
    FreeTypeFont(Library freeType, String filename, int size) {
        String filepath = "/dev/theskidster/" + App.DOMAIN + "/assets/" + filename;
        
        try(InputStream file = FreeTypeFont.class.getResourceAsStream(filepath)) {
            loadFont(freeType, file, size);
            
            glBindVertexArray(vao);
            
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, FLOATS_PER_GLYPH * Float.BYTES, GL_DYNAMIC_DRAW);
            
            glVertexAttribPointer(0, 2, GL_FLOAT, false, (4 * Float.BYTES), 0);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, (4 * Float.BYTES), (2 * Float.BYTES));
            
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            
        } catch(Exception e) {
            JLogger.logSevere("Failed to load font: \"" + filename + "\"", e);
        }
    }
    
    /**
     * Utilizes data supplied by the freetype library to generate new {@linkplain Glyph glyphs} that will be used by the font during text rendering.
     * 
     * @param freeType a reference to the freetype library that helps parse font information
     * @param file     the raw font file data
     * @param size     the desired size of the font in pixels
     */
    private void loadFont(Library freeType, InputStream file, int size) {
        try {
            Face face = freeType.newFace(file.readAllBytes(), 0);
            face.setPixelSizes(0, size);
            
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            
            for(char c = 0; c < 128; c++) {
                face.loadChar(c, FT_LOAD_RENDER);
                
                Glyph g     = new Glyph();
                g.texHandle = glGenTextures();
                g.advance   = face.getGlyphSlot().getAdvance().getX();
                g.width     = face.getGlyphSlot().getBitmap().getWidth();
                g.height    = face.getGlyphSlot().getBitmap().getRows();
                g.bearingX  = face.getGlyphSlot().getBitmapLeft();
                g.bearingY  = face.getGlyphSlot().getBitmapTop();
                
                glBindTexture(GL_TEXTURE_2D, g.texHandle);
                glTexImage2D(GL_TEXTURE_2D, 
                             0, 
                             GL_RED, 
                             g.width, 
                             g.height, 
                             0, 
                             GL_RED, 
                             GL_UNSIGNED_BYTE, 
                             face.getGlyphSlot().getBitmap().getBuffer());
                
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                
                glyphs.put(c, g);
            }
            
            face.delete();
            
        } catch(IOException e) {
            JLogger.logSevere("Failed to parse font data from ttf file", e);
        }
    }
    
    /**
     * Draws text to the screen in the font from which this method is called.
     * 
     * @param text       the message to display to the user
     * @param xPos       the horizontal position of the rendered text
     * @param yPos       the vertical position of the rendered text
     * @param color      the color the provided text will appear as
     * @param hudProgram the GLProgram to use
     */
    public void drawString(String text, float xPos, float yPos, Color color, GLProgram hudProgram) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        glBindVertexArray(vao);
        glActiveTexture(GL_TEXTURE0);
        
        hudProgram.setUniform("uType", 0);
        hudProgram.setUniform("uColor", color.asVec3());
        
        for(char c : text.toCharArray()) {
            Glyph g = glyphs.get(c);
            
            float x = xPos + g.bearingX;
            float y = yPos + (g.bearingY - g.height);
            float w = g.width;
            float h = g.height;
            
            glBindTexture(GL_TEXTURE_2D, g.texHandle);
            
            try(MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer vertexBuf = stack.mallocFloat(FLOATS_PER_GLYPH);
                
                //(vec2 position), (vec2 texCoords)
                vertexBuf.put(x)    .put(y + h).put(0).put(0);
                vertexBuf.put(x)    .put(y)    .put(0).put(1);
                vertexBuf.put(x + w).put(y)    .put(1).put(1);
                vertexBuf.put(x)    .put(y + h).put(0).put(0);
                vertexBuf.put(x + w).put(y)    .put(1).put(1);
                vertexBuf.put(x + w).put(y + h).put(1).put(0);
                
                vertexBuf.flip();
                
                glBindBuffer(GL_ARRAY_BUFFER, vbo);
                glBufferSubData(GL_ARRAY_BUFFER, 0, vertexBuf);
            }
            
            glDrawArrays(GL_TRIANGLES, 0, 6);
            xPos += (g.advance >> 6);
        }
        
        glDisable(GL_BLEND);
        App.checkGLError();
    }
    
}