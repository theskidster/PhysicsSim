package dev.theskidster.phys.main;

/**
 * @author J Hoffman
 * Created: Apr 13, 2021
 */

/**
 * Represents a GLSL data type that will be used to create a buffer of corresponding size.
 */
enum BufferType {
    INT,
    FLOAT,
    VEC2, VEC3,
    MAT3, MAT4;
}