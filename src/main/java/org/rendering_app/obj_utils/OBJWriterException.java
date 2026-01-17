package org.rendering_app.obj_utils;

public class OBJWriterException extends RuntimeException {
    public OBJWriterException(String message) {
        super(message);
    }

    public OBJWriterException(String message, Throwable cause) {
        super(message, cause);
    }
}
