package org.vivi.framework.report.simple.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.report.simple.common.response.ResponseBean;
import org.vivi.framework.report.simple.service.FileService;


/**
 * (GaeaFile)实体类
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseBean upload(@RequestParam("file") MultipartFile file) {
        return ResponseBean.builder().message("success").data((fileService.upload(file))).build();
    }

//    @GetMapping(value = "/download/{fileId}")
//    public ResponseEntity<byte[]> download(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileId") String fileId) {
//        return fileService.download(request, response, fileId);
//    }

}
