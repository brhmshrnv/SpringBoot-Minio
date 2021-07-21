package az.ibrahimshirinov.filestorageminio.exception.common;

public class ServiceUnavailableException extends RuntimeException {

    private static final long serialVersionUID = 1416594412164612L;

    public ServiceUnavailableException() {
        super("Request can not be processed at the moment, please try again later.");
    }
}