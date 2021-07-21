package az.ibrahimshirinov.filestorageminio.controller;

import az.ibrahimshirinov.filestorageminio.domain.File;
import az.ibrahimshirinov.filestorageminio.dto.FileResponseDto;
import az.ibrahimshirinov.filestorageminio.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final FileStorageService service;

    @SneakyThrows
    @Operation(summary = "Upload file ")
    @PostMapping
    public ResponseEntity<File> addFile(@RequestParam("file") MultipartFile multipartFile , @RequestParam("productId") Long productId){
        log.trace("Hello");
        return ResponseEntity.ok().body(service.addFile(productId,multipartFile));
    }

    @SneakyThrows
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFile(@PathVariable Long productId) {
        service.deleteFile(productId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<FileResponseDto> getFile(@PathVariable Long id){
         return ResponseEntity.ok().body(service.getFile(id));
    }

    @SneakyThrows
    @GetMapping("/all/{productId}")
    public ResponseEntity<List<FileResponseDto>> getFiles(@PathVariable Long productId){
       return ResponseEntity.ok().body(service.getFiles(productId));
    }


}
