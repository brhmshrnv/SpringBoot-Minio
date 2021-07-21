package az.ibrahimshirinov.filestorageminio.exception;

import az.ibrahimshirinov.filestorageminio.exception.common.NotFoundException;

public class FileNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 2426076563961704059L;
    private static final String MESSAGE = "Fayl tapilmadi %s";
    public FileNotFoundException(Long id) {
        super(String.format(MESSAGE,id));
    }
}
