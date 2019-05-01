package edu.fcu.d0656146.sicxeassembler.model;

/**
 *
 * @author D0656146
 */
public class SICXEInstruction {

    private boolean isXEOnly;
    private boolean isPsuedo;

    private int location;
    private String addressLable;
    private String mnemonicOpcode;
    private String operand;

    private boolean isIndexAddressing;
    private AddressingMode addressingMode;
}

 enum AddressingMode {
    SIC, XE, IMMEDIATE, INDIRECT
}
