package edu.fcu.d0656146.sicxeassembler.model;

import java.util.*;

/**
 *
 * @author D0656146
 */
public class SICXEObjectProgram {

    private final ArrayList<String> objectCodes;

    public SICXEObjectProgram(SICXEAssemblyProgram asmProgram,
            HashMap<String, SICXERegister> registerTable) throws AssembleException {
        this.objectCodes = new ArrayList<>();
        for (SICXEInstruction instruction : asmProgram.codes) {
            String objectCode = "";
            switch (instruction.operandType) {
                case NO_OPE:
                    objectCode += Integer.toString(instruction.hexOpcode, 16);
                    patchZero(objectCode, instruction.instructionLength * 2);
                    break;
                case ONE_REG:
                    objectCode += Integer.toString(instruction.hexOpcode, 16);
                    objectCode += "0" + Integer.toString(registerTable.get(instruction.getOperand()).number);
                    patchZero(objectCode, instruction.instructionLength * 2);
                    break;
                case TWO_REG:
                    objectCode += Integer.toString(instruction.hexOpcode, 16);
                    String[] registers = instruction.getOperand().split(",");
                    objectCode += "0" + Integer.toString(registerTable.get(registers[0]).number);
                    objectCode += "0" + Integer.toString(registerTable.get(registers[1]).number);
                    patchZero(objectCode, instruction.instructionLength * 2);
                    break;
                case ADDRESS:
                    objectCode += Integer.toString(instruction.hexOpcode, 16);
                    int address;
                    try {
                        address = Integer.parseInt(instruction.getOperand(), 16);
                    } catch (NumberFormatException ex) {
                        if (asmProgram.symbolTable.get(instruction.getOperand()) == null) {
                            throw new AssembleException("Unknown symbol " + instruction.getOperand() + ".", instruction.getLinenumber());
                        }
                        address = asmProgram.symbolTable.get(instruction.getOperand());
                    }
                    objectCode += Integer.toHexString(address);
                    patchZero(objectCode, instruction.instructionLength * 2);
                    break;
                case CONST:
                    objectCode = instruction.getOperand();
                    break;
                case DECIMAL:
                    objectCode = "RES";
                    objectCode += instruction.instructionLength;
                    break;
            }
            this.objectCodes.add(objectCode.toUpperCase());
        }
    }

    private String patchZero(String target, int length) {
        while (target.length() < length) {
            target += "0";
        }
        return target;
    }
}
