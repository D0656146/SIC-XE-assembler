package edu.fcu.d0656146.sicxeassembler.model;

/**
 *
 * @author D0656146
 */
public class AssembleException extends Exception {

    /**
     * Creates a new instance of <code>AssembleException</code> without detail
     * message.
     */
    public AssembleException() {
    }

    /**
     * Constructs an instance of <code>AssembleException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AssembleException(String msg, int lineNumber) {
        super("Line " + lineNumber + ": " + msg);
    }
}
