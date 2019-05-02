package edu.fcu.d0656146.sicxeassembler.model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author D0656146
 */
public class SICXEAssemblyProgram {

    private String filename;
    private ArrayList<SICXEInstruction> codes;
    private HashMap<String, HexInteger> symbolTable;

    SICXEAssemblyProgram(String filename) {
        this.filename = filename;
        codes = new ArrayList<>();
    }

    public void parseCode(HashMap<String, SICXEStandardInstruction> instructionTable, HashMap<String, Integer> registerTable)
            throws FileNotFoundException, IOException, AssembleException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String instruction, addressLable, mnemonicOpcode, operand;
        int lineNumber = 0;
        HexInteger location = new HexInteger();
        //read single line per loop and check the syntax format
        while (reader.ready()) {
            lineNumber++;
            instruction = reader.readLine();
            //ignore line if it's a blank line or comment line
            if (instruction.isBlank() || instruction.trim().charAt(0) == '.') {
                continue;
            }
            //fetch address lable
            if (instruction.indexOf('\t') == -1) {
                instruction += "\t";
            }
            addressLable = instruction.substring(0, instruction.indexOf('\t'));
            if (!isOpcodeLableLegal(addressLable)) {
                throw new AssembleException("Illegal symbol name.", lineNumber);
            }
            instruction = instruction.substring(instruction.indexOf('\t') + 1, instruction.length());
            //fetch mnemonic opcode
            if (instruction.indexOf('\t') == -1) {
                instruction += "\t";
            }
            mnemonicOpcode = instruction.substring(0, instruction.indexOf('\t'));
            if (mnemonicOpcode.isEmpty() || !isOpcodeLableLegal(mnemonicOpcode)) {
                throw new AssembleException("Illegal opcode format.", lineNumber);
            }
            instruction = instruction.substring(instruction.indexOf('\t') + 1, instruction.length());
            //fetch operand
            if (instruction.indexOf('\t') == -1) {
                instruction += "\t";
            }
            operand = instruction.substring(0, instruction.indexOf('\t'));
            //add new instruction into codes' list, not yet completed
            SICXEInstruction tempInstruction = new SICXEInstruction(addressLable, mnemonicOpcode, operand,
                     lineNumber, location, instructionTable, registerTable);
            codes.add(tempInstruction);
            //update symbol table
            if (!addressLable.isEmpty() && !symbolTable.containsKey(addressLable)) {
                symbolTable.put(addressLable, location);
            }
        }
    }

    //determine if a opcode or lable legal
    static boolean isOpcodeLableLegal(String opcode) {
        return !(opcode.length() > 6 || opcode.matches("[a-zA-Z0-9]"));
    }
}
