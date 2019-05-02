package edu.fcu.d0656146.sicxeassembler.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author D0656146
 */
public class SICXEAssembler {

    private SICXEAssemblyProgram asmProgram;
    private SICXEObjectProgram objProgram;
    private SICXEInstructionTable instructionTable;

    /**
     * Read assembly program from file.
     *
     * @param filename assembly program filename
     */
    public void open(String filename) throws IOException, FileNotFoundException, AssembleException {
        asmProgram = new SICXEAssemblyProgram(filename);
        asmProgram.parseCode(new HashMap<String, SICXEStandardInstruction>(), new HashMap<String, Integer>());
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
