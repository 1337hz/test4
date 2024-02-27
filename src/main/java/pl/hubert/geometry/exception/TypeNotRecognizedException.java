package pl.hubert.geometry.exception;

public class TypeNotRecognizedException extends RuntimeException{
    public TypeNotRecognizedException(String type) {
        super(String.format("Shape not recognized: %s", type));
    }
}
