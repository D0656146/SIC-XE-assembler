package edu.fcu.d0656146.sicxeassembler.view;

import edu.fcu.d0656146.sicxeassembler.model.SICXEAssembler;
import java.io.IOException;

/**
 *
 * @author D0656146
 */
public class Assembler {

    /**
     * @param args sic/xe program filename, optional -xe argument to enable xe
     */
    public static void main(String[] args) throws IOException {

        boolean xeAvailable = argsProcess(args);

        SICXEAssembler assembler = new SICXEAssembler();

        if (xeAvailable) {
            assembler.enableXE();
        }

        assembler.open(args[0]);
        assembler.assemble();
        assembler.output();
        //assembler.autoAssemble(args[0]);
    }

    //Processing command line argument
    private static boolean argsProcess(String[] args) {
        if (args.length == 0) {
            System.err.println("Missing filename.");
            System.exit(1);
        }
        if (args.length > 2) {
            System.err.println("Unknown arguments.");
            System.exit(1);
        }
        //determine if there's argument "-xe"
        if (args.length == 2) {
            if (args[1].toLowerCase().equals("-xe")) {
                return true;
            } else {
                System.err.println("Unknown arguments.");
                System.exit(1);
            }
        }
        return false;
    }
}
