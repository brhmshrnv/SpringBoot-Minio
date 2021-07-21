package az.ibrahimshirinov.filestorageminio.exception.common;

public class NotFoundException extends RuntimeException{


    private static final long serialVersionUID = 7315036990403658883L;
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
