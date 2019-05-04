package edu.fcu.d0656146.sicxeassembler.model;

import java.io.IOException;
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
    private HashMap<String, Integer> registerTable;

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

    public void assemble() {
    }

    public void output() {
    }

    public void enableXE() {
    }

}

class SICXEInstructionTable {

    public SICXEInstructionTable() {
    }
}
