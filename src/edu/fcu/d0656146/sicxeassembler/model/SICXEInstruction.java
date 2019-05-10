package edu.fcu.d0656146.sicxeassembler.model;

import java.util.HashMap;

/**
 *
 * @author D0656146
 */
public class SICXEInstruction extends SICXEStandardInstruction {

    private int lineNumber;
    private int location;
    private String addressLable;
    private String operand;

    private boolean isExtended;
    private boolean isIndexAddressing;
    private AddressingMode addressingMode;

    SICXEInstruction(String addressLable, String mnemonicOpcode, String operand, int lineNumber, int location,
            HashMap<String, SICXEStandardInstruction> instructionTable,
            HashMap<String, SICXERegister> registerTable)
            throws AssembleException {
        //temporarily filled up to avoid compile error, no actual effect
        super(true, true, "TEMP", 0x00, OperandType.NO_OPE, 0);
        //simple assignments
        this.lineNumber = lineNumber;
        this.location = location;
        this.addressLable = addressLable;
        this.mnemonicOpcode = mnemonicOpcode;
        this.operand = operand;
        //match mnemonic opcode
        if (!instructionTable.containsKey(mnemonicOpcode)) {
            throw new AssembleException("Unknown operation " + mnemonicOpcode + ".", lineNumber);
        }
        SICXEStandardInstruction model = instructionTable.get(mnemonicOpcode);
        hexOpcode = model.hexOpcode;
        operandType = model.operandType;
        isXEOnly = model.isXEOnly;
        isPsuedo = model.isPsuedo;
        instructionLength = model.instructionLength;
        //check operand
        addressingMode = AddressingMode.SIC;
        isIndexAddressing = false;
        String[] registers = operand.split(",");
        switch (model.operandType) {
            case NO_OPE:
                if (!operand.isEmpty()) {
                    throw new AssembleException("Unexpected operand follow " + mnemonicOpcode + ".", lineNumber);
                }
                break;
            case ONE_REG:
                if (registers.length != 1) {
                    throw new AssembleException(mnemonicOpcode + " needs 1 register.", lineNumber);
                }
                if (!registerTable.containsKey(registers[0])) {
                    throw new AssembleException("Unknown register " + registers[0] + ".", lineNumber);
                }
                break;
            case TWO_REG:
                if (registers.length != 2) {
                    throw new AssembleException(mnemonicOpcode + " needs 2 registers.", lineNumber);
                }
                if (!registerTable.containsKey(registers[0]) || !registerTable.containsKey(registers[1])) {
                    throw new AssembleException("Unknown register " + registers[0] + ".", lineNumber);
                }
                break;

            case ADDRESS:
                if (registers.length > 1) {
                    if (registers.length == 2 || registers[1].equals("X")) {
                        isIndexAddressing = true;
                    } else {
                        throw new AssembleException("Two much arguments for " + mnemonicOpcode + ".", lineNumber);
                    }

                }
                if (!SICXEAssemblyProgram.isOpcodeLableLegal(operand)) {
                    throw new AssembleException("Illegal symbol name.", lineNumber);
                }
                break;
            case CONST:
                this.operand = parseConstant(operand);
                instructionLength = operand.length() / 2;
                break;
            case DECIMAL:
                //RESW, RESB, WORD
                try {
                    instructionLength = Integer.parseInt(operand);
                } catch (NumberFormatException ex) {
                    throw new AssembleException("operand follow " + mnemonicOpcode + " should be a integer", lineNumber);
                }
                if (mnemonicOpcode.equals("RESW")) {
                    instructionLength *= 3;
                }
                if (mnemonicOpcode.equals("WORD")) {
                    instructionLength = 3;
                    try {
                        int value = Integer.parseInt(operand);
                        //2's complement
                        if (value < 0) {
                            value += 0x1000000;
                        }
                        this.operand = Integer.toHexString(value);
                        if (operand.length() > 6) {
                            throw new NumberFormatException();
                        }

                    } catch (NumberFormatException ex) {
                        throw new AssembleException("Illegal number", lineNumber);
                    }
                    while (operand.length() < 6) {
                        this.operand = "0" + this.operand;
                    }
                }
        }
    }

    private String parseConstant(String operand) throws AssembleException {
        char mode = operand.charAt(0);
        if (mode != 'C' || mode != 'X' || operand.charAt(1) != '\'' || operand.charAt(operand.length() - 1) != '\'') {
            throw new AssembleException("Illegal operand of " + this.mnemonicOpcode + ".", this.lineNumber);
        }
        operand = operand.substring(2, operand.length());
        if (mode == 'C') {
            String convertedOperand = "";
            for (char c : operand.toCharArray()) {
                convertedOperand += Integer.toHexString((int) c);
            }
            return convertedOperand;
        } else {
            try {
                Integer.parseInt(operand, 16);
                if (operand.length() % 2 == 1) {
                    throw new AssembleException("Odd length hex operand after " + this.mnemonicOpcode + ".", lineNumber);
                }
            } catch (NumberFormatException ex) {
                throw new AssembleException("Illegal operand of " + this.mnemonicOpcode + ".", lineNumber);
            }
            return operand;
        }
    }

    public String getAddressLable() {
        return addressLable;
    }

    public String getOperand() {
        return operand;
    }

    public int getLinenumber() {
        return lineNumber;
    }

    public int getLocation() {
        return location;
    }
}
