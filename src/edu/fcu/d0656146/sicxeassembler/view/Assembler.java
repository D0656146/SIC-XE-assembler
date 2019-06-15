package edu.fcu.d0656146.sicxeassembler.view;

import edu.fcu.d0656146.sicxeassembler.model.AssembleException;
import edu.fcu.d0656146.sicxeassembler.model.SICXEAssembler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author D0656146
 */
public class Assembler {

    private static boolean isXEEnabled;
    private static String filename;

    /**
     * @param args sic/xe program filename, optional -xe argument to enable
     * xe(not yet done)
     */
    public static void main(String args[]) {

        try {
            argsProcess(args);

            SICXEAssembler assembler = new SICXEAssembler();

            if (isXEEnabled) {
                assembler.enableXE();
            }

            assembler.open(filename);
            assembler.assemble();
            assembler.output(filename.substring(0, filename.indexOf('.')) + ".obj");
            //assembler.autoAssemble(args[0]);
        } catch (IOException ex) {
            Logger.getLogger(Assembler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AssembleException ex) {
            Logger.getLogger(Assembler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Processing command line argument
    private static void argsProcess(String[] args) {
        /*if (args.length == 0) {
            System.err.println("Missing filename.");
            System.exit(1);
        }*/
        if (args.length > 2) {
            System.err.println("Unknown arguments.");
            System.exit(1);
        }
        //determine if there's argument "-xe"
        if (args.length == 2) {
            if (!args[1].toLowerCase().equals("-xe")) {
                System.err.println("Unknown arguments.");
                System.exit(1);
            }
            isXEEnabled = true;
        } else {
            isXEEnabled = false;
        }
    }
}
