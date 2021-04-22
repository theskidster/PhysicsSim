package dev.theskidster.phys.main;

import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

/**
 * Immutable class used to represent colors in standard RGB format.
 */
public class Color {

    public static final Color WHITE = new Color(1);
    public static final Color RED = new Color(255, 0, 0);
    
    public final float r;
    public final float g;
    public final float b;
    
    private final Vector3f conversion;
    
    /**
     * Creates a new shade using the provided scalar value.
     * 
     * @param scalar the value each component of this color will be set to. Between 0 and 1.
     */
    private Color(float scalar) {
        r = g = b = scalar;
        conversion = new Vector3f(scalar);
    }
    
    /**
     * Creates a new color using the three RGB components supplied. Supplied values are expected to be between 0 and 255.
     * 
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     */
    private Color(int r, int g, int b) {
        this.r = (r / 255f);
        this.g = (g / 255f);
        this.b = (b / 255f);
        
        conversion = new Vector3f(this.r, this.g, this.b);
    }
    
    /**
     * Creates a new color using RGB components. Supplied values are expected to be between 0 and 255.
     * 
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @return the color generated using the values supplied to the RGB components
     */
    public static Color create(int r, int g, int b) {
        return new Color(r, g, b);
    }
    
    /**
     * Convenience method that provides the value of this color as a {@linkplain org.joml.Vector3f Vector3f} object.
     * 
     * @return the converted color value
     */
    public Vector3f asVec3() {
        return conversion;
    }
    
}