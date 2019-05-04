package edu.fcu.d0656146.sicxeassembler.model;

/**
 *
 * @author D0656146
 */
public class SICXEStandardInstruction {

    protected boolean isXEOnly;
    protected boolean isPsuedo;

    protected String mnemonicOpcode;
    protected int hexOpcode;
    protected OperandType operandType;

    protected int instructionLength;

    SICXEStandardInstruction(boolean isXEOnly, boolean isPsuedo,
            String mnemonicOpcode, int hexOpcode,
            OperandType operandType, int instructionLength) {
        this.isXEOnly = isXEOnly;
        this.isPsuedo = isPsuedo;

        this.mnemonicOpcode = mnemonicOpcode;
        this.hexOpcode = hexOpcode;
        this.operandType = operandType;
        this.instructionLength = instructionLength;
    }
}
