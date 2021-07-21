package az.ibrahimshirinov.filestorageminio.exception.common;


public class CannotDeleteForeignKeyConstraintException extends InvalidStateException {

    public static final String MESSAGE = "Bu obyekt digər yerlərdə istifadə edildiyindən, silmək olmaz.";
    private static final long serialVersionUID = 6535747579753366436L;

    public CannotDeleteForeignKeyConstraintException(String uid) {
        super(MESSAGE);
    }
}