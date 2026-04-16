/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.service;/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

public interface FileService {
    void createUploadFolder(String folder) throws URISyntaxException;

    String store(MultipartFile file, String folder) throws URISyntaxException, IOException;




    long getFileLength(String fileName, String folder) throws URISyntaxException;


    InputStreamResource getResource(String fileName, String folder)
            throws URISyntaxException, FileNotFoundException;
}
