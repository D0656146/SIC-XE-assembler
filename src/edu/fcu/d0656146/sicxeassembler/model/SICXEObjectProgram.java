package edu.fcu.d0656146.sicxeassembler.model;

import java.util.*;

/**
 *
 * @author D0656146
 */
public class SICXEObjectProgram {

    public final ArrayList<String> objectCodes;
    public final ArrayList<Integer> location;
    public final ArrayList<Integer> breakPoint;
    public final ArrayList<Integer> breakPointPrev;
    public final String startAddress;
    public final String programLength;
    private int baseAddress;
    private boolean isBaseEnabled = false;
    private final boolean isXEEnabled;

    public SICXEObjectProgram(SICXEAssemblyProgram asmProgram,
            HashMap<String, SICXERegister> registerTable) throws AssembleException {
        this.objectCodes = new ArrayList<>();
        this.location = new ArrayList<>();
        this.breakPoint = new ArrayList<>();
        this.breakPointPrev = new ArrayList<>();
        this.isXEEnabled = asmProgram.getIsXEEnabled();
        if (isAddressExceedLimit(asmProgram.startAddress)) {
            throw new AssembleException("Out of addressing range.", asmProgram.codes.get(0).getLinenumber());
        }
        String temp = Integer.toString(asmProgram.startAddress, 16);
        startAddress = patchZeroBefore(temp, 6);
        temp = Integer.toHexString(asmProgram.programLength);
        programLength = patchZeroBefore(temp, 6);
        breakPoint.add(asmProgram.startAddress);
        breakPointPrev.add(asmProgram.startAddress);

        for (SICXEInstruction instruction : asmProgram.codes) {
            String objectCode = "";
            //check directive BASE
            if (instruction.getMnemonicOpcode().equals("BASE")) {
                String base = instruction.getOperand();
                isBaseEnabled = true;
                try {
                    baseAddress = Integer.parseInt(base, 16);
                } catch (NumberFormatException ex) {
                    if (asmProgram.symbolTable.get(instruction.getOperand()) == null) {
                        throw new AssembleException("Unknown symbol " + instruction.getOperand() + ".", instruction.getLinenumber());
                    }
                    baseAddress = asmProgram.symbolTable.get(instruction.getOperand());
                }
            }
            //check directive NOBASE
            if (instruction.getMnemonicOpcode().equals("NOBASE")) {
                isBaseEnabled = false;
            }
            //check if instruction has object code
            if (instruction.getInstructionLength() == 0) {
                continue;
            }
            //parse to object code
            switch (instruction.operandType) {
                case NO_OPE:
                    int opcode = instruction.hexOpcode;
                    if (isXEEnabled && !instruction.isXEOnly) {
                        opcode += 0b11;
                    }
                    objectCode += Integer.toHexString(opcode);
                    objectCode = patchZero(objectCode, instruction.instructionLength * 2);
                    break;
                case ONE_REG:
                    objectCode += Integer.toHexString(instruction.hexOpcode);
                    objectCode += Integer.toString(registerTable.get(instruction.getOperand()).number);
                    objectCode = patchZero(objectCode, instruction.instructionLength * 2);
                    break;
                case TWO_REG:
                    objectCode += Integer.toHexString(instruction.hexOpcode);
                    String[] registers = instruction.getOperand().split(",");
                    objectCode += Integer.toString(registerTable.get(registers[0]).number);
                    objectCode += Integer.toString(registerTable.get(registers[1]).number);
                    objectCode = patchZero(objectCode, instruction.instructionLength * 2);
                    break;
                case ADDRESS:
                    int realOpcode = instruction.hexOpcode;
                    switch (instruction.getAddressingMode()) {
                        case IMMEDIATE:
                            realOpcode += 0b01;
                            break;
                        case INDIRECT:
                            realOpcode += 0b10;
                            break;
                        case XE:
                            realOpcode += 0b11;
                            break;
                    }
                    objectCode += Integer.toHexString(realOpcode);
                    objectCode = patchZeroBefore(objectCode, 2);
                    int realAddress;
                    try {
                        realAddress = Integer.parseInt(instruction.getOperand());
                        objectCode += patchZeroBefore(Integer.toHexString(realAddress), instruction.getInstructionLength() * 2 - 2);
                        break;
                    } catch (NumberFormatException ex) {
                        if (asmProgram.symbolTable.get(instruction.getOperand()) == null) {
                            throw new AssembleException("Unknown symbol " + instruction.getOperand() + ".", instruction.getLinenumber());
                        }
                        realAddress = asmProgram.symbolTable.get(instruction.getOperand());
                    }
                    if (isAddressExceedLimit(realAddress)) {
                        throw new AssembleException("Out of addressing range.", asmProgram.codes.get(0).getLinenumber());
                    }
                    if (instruction.getAddressingMode() == AddressingMode.SIC) {
                        if (instruction.getIsIndexAddressing()) {
                            realAddress += 0x1000;
                        }
                        String addressString = Integer.toHexString(realAddress);
                        while (addressString.length() != 4) {
                            addressString = "0" + addressString;
                        }
                        objectCode += addressString;
                        break;
                    } else if (instruction.getIsExtended()) {
                        if (instruction.getIsIndexAddressing()) {
                            objectCode += "9";
                        } else {
                            objectCode += "1";
                        }
                        String addressString = Integer.toHexString(realAddress);
                        while (addressString.length() != 5) {
                            addressString = "0" + addressString;
                        }
                        objectCode += addressString;
                        break;
                    } else {
                        String addressString;
                        int deltaAddress = realAddress - instruction.getLocation() - instruction.getInstructionLength();
                        if (deltaAddress <= 0x7FF && deltaAddress * -1 <= 0x800) {
                            if (deltaAddress < 0) {
                                deltaAddress += 0x1000;
                            }
                            if (instruction.getIsIndexAddressing()) {
                                objectCode += "A";
                            } else {
                                objectCode += "2";
                            }
                            addressString = Integer.toHexString(deltaAddress);
                        } else if (isBaseEnabled && realAddress - baseAddress >= 0 && realAddress - baseAddress <= 0xFFF) {
                            if (instruction.getIsIndexAddressing()) {
                                objectCode += "C";
                            } else {
                                objectCode += "4";
                            }
                            addressString = Integer.toHexString(realAddress - baseAddress);
                        } else {
                            throw new AssembleException("Failed to addressing.", instruction.getLinenumber());
                        }
                        while (addressString.length() != 3) {
                            addressString = "0" + addressString;
                        }
                        objectCode += addressString;
                    }
                    break;

                case CONST:
                    breakPoint.add(instruction.getLocation());
                    objectCode = "RES" + instruction.getOperand();
                    break;
                case DECIMAL:
                    breakPoint.add(instruction.getLocation());
                    objectCode = "RES";
                    break;
            }
            this.objectCodes.add(objectCode.toUpperCase());
            this.location.add(instruction.getLocation());
        }
        breakPoint.add(asmProgram.programLength);
        location.add(asmProgram.programLength);
    }

    public static String patchZero(String target, int length) {
        while (target.length() < length) {
            target += "0";
        }
        return target;
    }

    public static String patchZeroBefore(String target, int length) {
        while (target.length() < length) {
            target = "0" + target;
        }
        return target;
    }

    private boolean isAddressExceedLimit(int address) {
        if (isXEEnabled) {
            return address > SICXEAssembler.XE_MEMORY_LIMIT;
        } else {
            return address > SICXEAssembler.SIC_MEMORY_LIMIT;
        }
    }
}
