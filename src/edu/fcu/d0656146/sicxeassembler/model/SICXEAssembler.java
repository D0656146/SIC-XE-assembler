package edu.fcu.d0656146.sicxeassembler.model;

import java.io.*;
import java.util.HashMap;

/**
 *
 * @author D0656146
 */
public class SICXEAssembler {

    private static final int SIC_MEMORY_LIMIT = 0x7FFF;

    private SICXEAssemblyProgram asmProgram;
    private SICXEObjectProgram objProgram;
    private HashMap<String, SICXEStandardInstruction> instructionTable;
    private HashMap<String, SICXERegister> registerTable;

    public SICXEAssembler() throws IOException {
        initInstructionTable("instructions");
        initRegisterTable("registers");
    }

    /**
     * Read assembly program from file.
     *
     * @param filename assembly program filename
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws edu.fcu.d0656146.sicxeassembler.model.AssembleException
     */
    public void open(String filename) throws AssembleException, IOException {
        asmProgram = new SICXEAssemblyProgram(filename);
        asmProgram.parseCode(instructionTable, registerTable);
    }

    public void assemble() throws AssembleException {
        objProgram = new SICXEObjectProgram(asmProgram, registerTable);
    }

    public void output() {
    }

    public void enableXE() {
    }

    private void initInstructionTable(String filename) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/" + filename)));
        while (input.ready()) {
            String line = input.readLine();
            if (line.isBlank() || line.trim().charAt(0) == '#') {
                continue;
            }

            String[] props = line.split(" +");
            boolean isPsuedo = Boolean.parseBoolean(props[4]);
            boolean isXEOnly = Boolean.parseBoolean(props[5]);
            String mnemonicOpcode = props[0];
            int hexOpcode = Integer.parseInt(props[1], 16);
            OperandType operandType = OperandType.valueOf(props[2]);
            int instructionLength = Integer.parseInt(props[3]);
            instructionTable.put(mnemonicOpcode,
                    new SICXEStandardInstruction(isPsuedo, isXEOnly, mnemonicOpcode, hexOpcode, operandType, instructionLength));
        }
    }

    private void initRegisterTable(String filename) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/" + filename)));
        while (input.ready()) {
            String line = input.readLine();
            if (line.isBlank() || line.trim().charAt(0) == '#') {
                continue;
            }

            String[] props = line.split(" +");
            String name = props[0];
            int number = Integer.parseInt(props[1]);
            boolean isXEOnly = Boolean.parseBoolean(props[2]);
            registerTable.put(name, new SICXERegister(name, number, isXEOnly));
        }
    }

}
