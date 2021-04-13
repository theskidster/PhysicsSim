package dev.theskidster.phys.main;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

/**
 * Provides a type-neutral structure in which uniform variable data may be stored and retrieved as the type needed.
 */
final class UniformVariable {

    final int location;
    private final Buffer buffer;
    
    /**
     * Creates a new data structure to store the data of a uniform variable.
     * 
     * @param location the unique number used to identify the uniform variable supplied by OpenGL
     * @param buffer   a type-neutral buffer used to store the uniform data that will be sent to the currently active shader program.
     */
    UniformVariable(int location, Buffer buffer) {
        this.location = location;
        this.buffer   = buffer;
    }
    
    /**
     * Provides the data buffer associated with this uniform variable as the type described in the method name.
     * 
     * @return the encoded buffer
     */
    IntBuffer asIntBuffer() {
        return (IntBuffer) buffer;
    }
    
    /**
     * Provides the data buffer associated with this uniform variable as the type described in the method name.
     * 
     * @return the encoded buffer
     */
    FloatBuffer asFloatBuffer() {
        return (FloatBuffer) buffer;
    }
    
}