package pl.hubert.geometry.common.exception;

public class TypeNotRecognizedException extends RuntimeException{
    public TypeNotRecognizedException(String type) {
        super(String.format("Shape not recognized: %s", type));
    }
}
