package edu.fcu.d0656146.sicxeassembler.model;

import java.util.*;

/**
 *
 * @author D0656146
 */
public class SICXEObjectProgram {

    public final ArrayList<String> objectCodes;
    public final ArrayList<Integer> breakPoint;
    public final String startAddress;
    public final String programLength;

    public SICXEObjectProgram(SICXEAssemblyProgram asmProgram,
            HashMap<String, SICXERegister> registerTable) throws AssembleException {
        this.objectCodes = new ArrayList<>();
        this.breakPoint = new ArrayList<>();
        String temp = Integer.toString(asmProgram.startAddress, 16);
        patchZero(temp, 6);
        startAddress = temp;
        temp = Integer.toHexString(asmProgram.programLength);
        patchZero(temp, 6);
        programLength = temp;
        breakPoint.add(asmProgram.startAddress);

        for (SICXEInstruction instruction : asmProgram.codes) {
            String objectCode = "";
            switch (instruction.operandType) {
                case NO_OPE:
                    objectCode += Integer.toHexString(instruction.hexOpcode);
                    patchZero(objectCode, instruction.instructionLength * 2);
                    break;
                case ONE_REG:
                    objectCode += Integer.toHexString(instruction.hexOpcode);
                    objectCode += "0" + Integer.toString(registerTable.get(instruction.getOperand()).number);
                    patchZero(objectCode, instruction.instructionLength * 2);
                    break;
                case TWO_REG:
                    objectCode += Integer.toHexString(instruction.hexOpcode);
                    String[] registers = instruction.getOperand().split(",");
                    objectCode += "0" + Integer.toString(registerTable.get(registers[0]).number);
                    objectCode += "0" + Integer.toString(registerTable.get(registers[1]).number);
                    patchZero(objectCode, instruction.instructionLength * 2);
                    break;
                case ADDRESS:
                    objectCode += Integer.toHexString(instruction.hexOpcode);
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
                    breakPoint.add(instruction.getLocation());
                    objectCode = "RES";
                    break;
            }
            this.objectCodes.add(objectCode.toUpperCase());
        }
        breakPoint.add(asmProgram.programLength);
    }

    private String patchZero(String target, int length) {
        while (target.length() < length) {
            target += "0";
        }
        return target;
    }
}
