package edu.fcu.d0656146.sicxeassembler.model;

/**
 *
 * @author D0656146
 */
public class SICXEStandardInstruction {

    protected boolean isXEOnly;
    protected boolean isPsuedo;

    protected String mnemonicOpcode;
    protected HexInteger hexOpcode;
    protected OperandType operandType;

    SICXEStandardInstruction(boolean isXEOnly, boolean isPsuedo,
            String mnemonicOpcode, HexInteger hexOpcode,
            OperandType operandType, int instructionLength) {
        this.isXEOnly = isXEOnly;
        this.isPsuedo = isPsuedo;

        this.mnemonicOpcode = mnemonicOpcode;
        this.hexOpcode = hexOpcode;
        this.operandType = operandType;
    }
}
