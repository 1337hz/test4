package pl.hubert.geometry.exception;

public class MappingMismatchException  extends RuntimeException{
    public MappingMismatchException() {
        super("Mismatched type between mapper and entity");
    }
}
