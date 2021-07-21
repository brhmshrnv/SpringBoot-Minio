package az.ibrahimshirinov.filestorageminio.exception.common;

public class ConstraintsViolationException extends RuntimeException {

    private static final long serialVersionUID = -189329889879747169L;

    public ConstraintsViolationException(String message) {
        super(message);
    }

    public ConstraintsViolationException(String message, Throwable ex) {
        super(message, ex);
    }
}
