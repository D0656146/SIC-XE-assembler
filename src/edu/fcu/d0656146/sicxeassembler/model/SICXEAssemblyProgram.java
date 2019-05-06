package edu.fcu.d0656146.sicxeassembler.model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author D0656146
 */
public class SICXEAssemblyProgram {

    private final String filename;
    private final ArrayList<SICXEInstruction> codes;
    private final HashMap<String, Integer> symbolTable;

    SICXEAssemblyProgram(String filename) {
        this.filename = filename;
        codes = new ArrayList<>();
        symbolTable = new HashMap();
    }

    /**
     *
     * @param instructionTable SIC/XE instruction table
     * @param registerTable SIC/XE register table
     * @throws IOException
     * @throws AssembleException
     */
    public void parseCode(HashMap<String, SICXEStandardInstruction> instructionTable,
            HashMap<String, SICXERegister> registerTable)
            throws IOException, AssembleException {

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String instruction, addressLable, mnemonicOpcode, operand;
        int lineNumber = 0;
        int location = 0;
        boolean startExist = false, endExist = false;
        //read single line per loop and check the syntax format
        while (reader.ready()) {
            lineNumber++;
            instruction = reader.readLine();
            //ignore line if it's a blank line or comment line
            if (instruction.isBlank() || instruction.trim().charAt(0) == '.') {
                continue;
            }
            //fetch address lable
            //find tab location to spilt
            if (instruction.indexOf('\t') == -1) {
                instruction += "\t";
            }
            addressLable = instruction.substring(0, instruction.indexOf('\t'));
            //judge syntax and fetch
            if (!isOpcodeLableLegal(addressLable)) {
                throw new AssembleException("Illegal symbol name.", lineNumber);
            }
            instruction = instruction.substring(instruction.indexOf('\t') + 1, instruction.length());
            //fetch mnemonic opcode
            //find tab location to spilt
            if (instruction.indexOf('\t') == -1) {
                instruction += "\t";
            }
            mnemonicOpcode = instruction.substring(0, instruction.indexOf('\t'));
            //judge syntax and fetch
            if (mnemonicOpcode.isEmpty() || !isOpcodeLableLegal(mnemonicOpcode)) {
                throw new AssembleException("Illegal opcode format.", lineNumber);
            }
            instruction = instruction.substring(instruction.indexOf('\t') + 1, instruction.length());
            //fetch operand
            if (instruction.indexOf('\t') == -1) {
                instruction += "\t";
            }
            operand = instruction.substring(0, instruction.indexOf('\t'));
            //check directive START, ignore exceeding memory limit
            if (mnemonicOpcode.equals("START")) {
                if (startExist) {
                    throw new AssembleException("Duplicated START.", lineNumber);
                } else if (addressLable.isEmpty()) {
                    throw new AssembleException("Missing lable of START.", lineNumber);
                }
                try {
                    location = Integer.parseInt(operand, 16);
                } catch (NumberFormatException ex) {
                    throw new AssembleException("Illegal operand of START.", lineNumber);
                }
            } else {
                if (!startExist) {
                    throw new AssembleException("Instruction before START.", lineNumber);
                }
            }
            //check directive END
            if (endExist) {
                throw new AssembleException("Instruction after END.", lineNumber);
            } else if (mnemonicOpcode.equals("END")) {
                endExist = true;
            }

            //add new instruction into codes' list
            SICXEInstruction tempInstruction = new SICXEInstruction(addressLable, mnemonicOpcode, operand,
                    lineNumber, location, instructionTable, registerTable);
            location += tempInstruction.instructionLength;
            codes.add(tempInstruction);
            //update symbol table
            if (symbolTable.containsKey(addressLable)) {
                throw new AssembleException("Duplicated symbol" + addressLable + ".", location);
            }
            if (!addressLable.isEmpty()) {
                symbolTable.put(addressLable, location);
            }
        }
        //check other faults
        if (codes.isEmpty()) {
            throw new AssembleException("Empty program.", lineNumber);
        }
        if (!endExist) {
            throw new AssembleException("Missing END.", lineNumber);
        }
    }

    //determine if a opcode or lable legal
    static boolean isOpcodeLableLegal(String opcode) {
        return !(opcode.length() > 6 || opcode.matches("[a-zA-Z0-9]"));
    }
}
