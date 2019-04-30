package edu.fcu.d0656146.sicxeassembler.view;

import edu.fcu.d0656146.sicxeassembler.model.SICXEAssembler;

/**
 *
 * @author D0656146
 */
public class Assembler {

    /**
     * @param args sic/xe program filename
     */
    public static void main(String[] args) {

        SICXEAssembler assembler = new SICXEAssembler();

        argsProcess(args, assembler);

        assembler.open(args[0]);
        assembler.assemble();
        assembler.output();
        //assembler.autoAssemble();
    }

    //Processing command line argument
    private static void argsProcess(String[] args, SICXEAssembler assembler) {
        if (args.length == 0) {
            System.err.println("Missing filename.");
            System.exit(1);
        }
        if (args.length > 2) {
            System.err.println("Unknown arguments");
            System.exit(1);
        }
        if (args.length == 2 && args[1].toLowerCase().equals("-xe")) {
            assembler.allowXE();
        }
    }

}
