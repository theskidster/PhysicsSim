package dev.theskidster.phys.commands;

/**
 * Aug 24, 2021
 */

/**
 * @author J Hoffman
 * @since  
 */
final class Key {
    
    private final char c;
    private final char C;

    Key(char c, char C) {
        this.c = c;
        this.C = C;
    }

    char getChar(boolean shiftHeld) {
        return (!shiftHeld) ? c : C;
    }
    
}