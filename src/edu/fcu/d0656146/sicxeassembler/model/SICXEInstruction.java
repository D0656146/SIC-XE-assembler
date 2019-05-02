package edu.fcu.d0656146.sicxeassembler.model;

import java.util.HashMap;

/**
 *
 * @author D0656146
 */
public class SICXEInstruction extends SICXEStandardInstruction {

    private int lineNumber;
    private HexInteger location;
    private String addressLable;
    private String operand;
    private int instructionLength;

    private boolean isExtended;
    private boolean isIndexAddressing;
    private AddressingMode addressingMode;

    SICXEInstruction(String addressLable, String mnemonicOpcode, String operand, int lineNumber, HexInteger location,
            HashMap<String, SICXEStandardInstruction> instructionTable, HashMap<String, Integer> registerTable)
            throws AssembleException {
        this.lineNumber = lineNumber;
        this.location = new HexInteger(location);
        //match mnemonic opcode
        if (!instructionTable.containsKey(mnemonicOpcode)) {
            throw new AssembleException("Unknown operation \"" + mnemonicOpcode + "\".", lineNumber);
        }
        SICXEStandardInstruction model = instructionTable.get(mnemonicOpcode);
        this.mnemonicOpcode = model.mnemonicOpcode;
        hexOpcode = model.hexOpcode;
        operandType = model.operandType;
        isXEOnly = model.isXEOnly;
        isPsuedo = model.isPsuedo;
        //check operand
        isIndexAddressing = false;
        String[] registers = operand.split(",");;
        switch (model.operandType) {
            case NO_OPERAND:
                if (!operand.isEmpty()) {
                    throw new AssembleException("Unexpected operand follow " + mnemonicOpcode + ".", lineNumber);
                }
                break;
            case ONE_REGISTER:
                if (registers.length != 1) {
                    throw new AssembleException(mnemonicOpcode + " needs 1 register.", lineNumber);
                }
                if (!registerTable.containsKey(registers[0])) {
                    throw new AssembleException("Unknown register " + registers[0] + ".", lineNumber);
                }
                break;
            case TWO_REGISTER:
                if (registers.length != 2) {
                    throw new AssembleException(mnemonicOpcode + " needs 2 registers.", lineNumber);
                }
                break;
            case ADDRESS:
                if (!SICXEAssemblyProgram.isOpcodeLableLegal(operand)) {
                    throw new AssembleException("Illegal symbol name.", lineNumber);
                }
                break;
        }
    }
}
