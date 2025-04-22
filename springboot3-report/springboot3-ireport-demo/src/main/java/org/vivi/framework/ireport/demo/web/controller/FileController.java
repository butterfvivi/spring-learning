package org.vivi.framework.ireport.demo.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.ireport.demo.common.utils.FileUtils;
import org.vivi.framework.ireport.demo.report.dto.AnalysisZipFileDataDto;
import org.vivi.framework.ireport.demo.report.dto.DownloadFileDto;
import org.vivi.framework.ireport.demo.report.dto.DownloadFileZipDto;

import java.util.Map;

@RequestMapping("api/file")
@RestController
public class FileController {

    @PostMapping("/downloadFile")
    public void downloadFile(HttpServletResponse response, @RequestBody DownloadFileDto dto) throws Exception {
        FileUtils.downloadFile(response, dto);
    }

    @PostMapping("/downloadFileZip")
    public void downloadFileZip(HttpServletResponse response, @RequestBody DownloadFileZipDto dto) throws Exception {
        FileUtils.downloadFileZip(response, dto);
    }

    @PostMapping("/analysisZipFileData")
    public Map<String,Object> analysisZipFileData(@RequestBody AnalysisZipFileDataDto dto) throws Exception {
        return FileUtils.analysisZipFileData( dto);
    }
}
