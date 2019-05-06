package edu.fcu.d0656146.sicxeassembler.model;

/**
 *
 * @author D0656146
 */
public class SICXERegister {

    public final String name;
    public final int number;
    public final boolean isXEOnly;

    public SICXERegister(String name, int number, boolean isXEOnly) {
        this.name = name;
        this.number = number;
        this.isXEOnly = isXEOnly;
    }
}
