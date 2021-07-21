package az.ibrahimshirinov.filestorageminio.service;

import az.ibrahimshirinov.filestorageminio.config.MinioConfiguration;
import az.ibrahimshirinov.filestorageminio.domain.File;
import az.ibrahimshirinov.filestorageminio.dto.FileResponseDto;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileStorageService {

    @Value("${minio.bucket}")
    private  String bucket;

    private final MinioConfiguration minioClient;
    private final FileService service;

    @SneakyThrows
    public File addFile(Long productId,MultipartFile multipartFile) {
        log.trace("Minio : save file to Minio server . Product ID : {}",productId);

        String filename = UUID.randomUUID() +"."+FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        minioClient
                .minioClient()
                .putObject(PutObjectArgs.builder().bucket(bucket).object(filename).stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                        .contentType(multipartFile.getContentType())
                        .build()
                );

        return service.save(productId, multipartFile,filename);
    }

    @SneakyThrows
    public void deleteFile(Long productId) {
        log.trace("Minio : delete file from Minio server . Product ID : {}",productId);
        minioClient
                .minioClient()
                .removeObject(RemoveObjectArgs.builder().bucket(bucket).object(get(productId).getMinioName())
                        .build());
        service.deleteFile(get(productId).getId());
    }

    @SneakyThrows
    public FileResponseDto getFile(Long id) {
        log.trace("Minio : retrieve file from Minio server. ID : {}",id);
        String objectUrl = minioClient
                .minioClient()
                .getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(get(id).getMinioName())
                        .expiry(1, TimeUnit.HOURS)
                        .build());
        return new FileResponseDto(id,get(id).getProductId(),objectUrl);
    }


    public List<FileResponseDto> getFiles(Long productId) {
        log.trace("Minio : retrieve all files from Minio server . Product ID : {}",productId);
        List<File> files = service.getAll(productId);
      return   files.stream().map(file -> getFile(file.getId())).collect(Collectors.toList());
    }

    private File get(Long id){
        return service.getById(id);
    }

}
