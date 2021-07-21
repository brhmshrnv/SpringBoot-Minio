package az.ibrahimshirinov.filestorageminio.service;

import az.ibrahimshirinov.filestorageminio.domain.File;
import az.ibrahimshirinov.filestorageminio.exception.FileNotFoundException;
import az.ibrahimshirinov.filestorageminio.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository repository;

    public File save(Long productId,MultipartFile multipartFile, String minioFileName) {
      log.trace("DB : Save file props to database. Product ID : {}",productId);
       File file =  File.builder()
                .originalName(multipartFile.getOriginalFilename())
                .size(multipartFile.getSize())
                .format(multipartFile.getContentType())
                .minioName(minioFileName)
                .productId(productId)
                .build();

      return  repository.save(file);
    }

    public File getById(Long id) {
        log.trace("DB : Get file props from database.  ID : {}",id);
        return repository.findById(id).orElseThrow(() -> new FileNotFoundException(id));
    }

    public List<File> getAll(Long productId){
        log.trace("DB : Get all files props from database. Product ID : {}",productId);
        List<File> allByProductId = repository.findAllByProductId(productId);

        allByProductId.forEach(System.out::println);
        return allByProductId;
    }

    public void deleteFile(Long productId) {
        log.trace("DB : Get all files props from database. Product ID : {}",productId);
        File file = getById(productId);
        repository.deleteById(file.getId());
    }


}
