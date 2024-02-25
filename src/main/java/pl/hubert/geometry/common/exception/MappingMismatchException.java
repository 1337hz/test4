package pl.hubert.geometry.common.exception;

public class MappingMismatchException  extends RuntimeException{
    public MappingMismatchException() {
        super("Mismatched type between mapper and entity");
    }

    public MappingMismatchException(String message) {
        super(message);
    }
}
