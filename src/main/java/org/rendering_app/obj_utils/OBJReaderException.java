package org.rendering_app.obj_utils;

public class OBJReaderException extends RuntimeException {
    public OBJReaderException(String errorMessage, int lineInd) {
        super("Error parsing OBJ file on line: " + lineInd + ". " + errorMessage);
    }

    public OBJReaderException(String errorMessage, int lineInd, Throwable cause) {
        super("Error parsing OBJ file on line: " + lineInd + ". " + errorMessage, cause);
    }
}
