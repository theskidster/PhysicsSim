package dev.theskidster.phys.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.system.MemoryUtil;

/**
 * @author J Hoffman
 * Created: Apr 16, 2021
 */

public class Cube {

    public final float width;
    public final float height;
    public final float depth;
    
    public FloatBuffer vertices;
    public IntBuffer indices;
    
    public Cube(float width, float height, float depth) {
        this.width  = width;
        this.height = height;
        this.depth  = depth;
        
        float w = width / 2;
        float h = height / 2;
        float d = depth / 2;
        
        vertices = MemoryUtil.memAllocFloat(144);
        indices  = MemoryUtil.memAllocInt(36);
        
        //Front
        vertices.put(-w) .put(h).put(-d).put(0).put(0).put(-1);   //0
        vertices .put(w) .put(h).put(-d).put(0).put(0).put(-1);   //1
        vertices .put(w).put(-h).put(-d).put(0).put(0).put(-1);   //2
        vertices.put(-w).put(-h).put(-d).put(0).put(0).put(-1);   //3
        
        //Back
        vertices .put(w) .put(h).put(d).put(0).put(0).put(1);     //4
        vertices.put(-w) .put(h).put(d).put(0).put(0).put(1);     //5
        vertices.put(-w).put(-h).put(d).put(0).put(0).put(1);     //6
        vertices .put(w).put(-h).put(d).put(0).put(0).put(1);     //7
        
        //Top
        vertices.put(-w).put(h) .put(d).put(0).put(1).put(0);     //8
        vertices .put(w).put(h) .put(d).put(0).put(1).put(0);     //9
        vertices .put(w).put(h).put(-d).put(0).put(1).put(0);     //10
        vertices.put(-w).put(h).put(-d).put(0).put(1).put(0);     //11
        
        //Bottom
        vertices.put(-w).put(-h).put(-d).put(0).put(-1).put(0);   //12
        vertices .put(w).put(-h).put(-d).put(0).put(-1).put(0);   //13
        vertices .put(w).put(-h) .put(d).put(0).put(-1).put(0);   //14
        vertices.put(-w).put(-h) .put(d).put(0).put(-1).put(0);   //15
        
        //Left
        vertices.put(-w) .put(h) .put(d).put(-1).put(0).put(0);   //16
        vertices.put(-w) .put(h).put(-d).put(-1).put(0).put(0);   //17
        vertices.put(-w).put(-h).put(-d).put(-1).put(0).put(0);   //18
        vertices.put(-w).put(-h) .put(d).put(-1).put(0).put(0);   //19
        
        //Right
        vertices.put(w) .put(h).put(-d).put(1).put(0).put(0);     //20
        vertices.put(w) .put(h) .put(d).put(1).put(0).put(0);     //21
        vertices.put(w).put(-h) .put(d).put(1).put(0).put(0);     //22
        vertices.put(w).put(-h).put(-d).put(1).put(0).put(0);     //23
        
        indices.put(0).put(1).put(2).put(2).put(3).put(0);       //Front
        indices.put(4).put(5).put(6).put(6).put(7).put(4);       //Back
        indices.put(8).put(9).put(10).put(10).put(11).put(8);    //Top
        indices.put(12).put(13).put(14).put(14).put(15).put(12); //Bottom
        indices.put(16).put(17).put(18).put(18).put(19).put(16); //Left
        indices.put(20).put(21).put(22).put(22).put(23).put(20); //Right
        
        vertices.flip();
        indices.flip();
    }
    
}