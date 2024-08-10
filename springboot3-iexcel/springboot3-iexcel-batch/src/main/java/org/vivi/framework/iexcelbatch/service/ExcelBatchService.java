package org.vivi.framework.iexcelbatch.service;

import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelbatch.entity.dto.DataExcelImportDto;
import org.vivi.framework.iexcelbatch.entity.query.UserRequest;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface ExcelBatchService {

    CompletableFuture<Integer> asyncImport1(MultipartFile file) throws IOException;

    DataExcelImportDto asyncImport2(UserRequest userRequest, MultipartFile file);

    DataExcelImportDto asyncImport3(UserRequest userRequest, MultipartFile file) throws IOException;

    void asyncbatchImport(UserRequest userRequest, MultipartFile[] files);
}
