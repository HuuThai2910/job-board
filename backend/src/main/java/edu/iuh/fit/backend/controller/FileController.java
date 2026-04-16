/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.controller;

import edu.iuh.fit.backend.dto.response.UploadFileResponse;
import edu.iuh.fit.backend.service.FileService;
import edu.iuh.fit.backend.util.annotaion.ApiMessage;
import edu.iuh.fit.backend.util.error.StorageException;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@RestController
@RequestMapping("/api/v1")
public class FileController {
    private final FileService fileService;
    @Value("${thai.upload-file.base-uri}")
    private String baseURI;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload single file successfully")
    public ResponseEntity<UploadFileResponse> upload(@RequestParam(name = "file", required = false) MultipartFile file,
                                                     @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {
//        Validate
        if(file == null || file.isEmpty()){
            throw new StorageException("File is empty. Please upload a file");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> {
            assert fileName != null;
            return fileName.toLowerCase().endsWith(item);
        });
        if(!isValid)
            throw new StorageException("Invalid file extension. Only allows: " + allowedExtensions.toString());
//        Tao mot thu muc chua file neu no chua ton tai
        this.fileService.createUploadFolder(baseURI + folder);
//        Store file
        String uploadFile = this.fileService.store(file, folder);
        UploadFileResponse uploadFileResponse = new UploadFileResponse(uploadFile, Instant.now());
        return ResponseEntity.ok(uploadFileResponse);
    }

    @GetMapping("files")
    @ApiMessage("Download a file successfully")
    public ResponseEntity<Resource> download(@RequestParam(name = "fileName", required = false) String fileName,
                                             @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException{
        if (fileName == null || folder == null) {
            throw new StorageException ("Missing required params: (fileName or foder)");
        }
// check file exist (and not a directory)
        long fileLength = this.fileService.getFileLength (fileName, folder);
        if (fileLength == 0) {
            throw new StorageException("File with name = + fileName + not found.");
        }
// download a file
        InputStreamResource resource = this.fileService.getResource (fileName, folder);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);

    }
}
