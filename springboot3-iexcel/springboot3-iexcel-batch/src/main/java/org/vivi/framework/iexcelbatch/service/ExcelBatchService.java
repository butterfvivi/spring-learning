package org.vivi.framework.iexcelbatch.service;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelbatch.common.response.R;
import org.vivi.framework.iexcelbatch.entity.dto.DataExcelImportDto;
import org.vivi.framework.iexcelbatch.entity.query.UserRequest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public interface ExcelBatchService {

    void asyncImport2(Class<T> head, MultipartFile file, Function<T, R> function, Function<List<R>,Integer> dbFunction) throws IOException;

    void batchUpload2(Class<T> head, MultipartFile[] files, Function<T,R> function, Function<List<R>,Integer> dbFunction) throws IOException;

    CompletableFuture<Integer> batchUpload3(MultipartFile file) throws IOException;

    <T,R> DataExcelImportDto readExcelAndSaveAsync(Class<T> head, MultipartFile file, Function<T,R> function, Function<List<R>,Integer> dbFunction) throws IOException, ExecutionException, InterruptedException;

    DataExcelImportDto asyncImport(UserRequest userRequest, MultipartFile file) throws IOException, ExecutionException, InterruptedException;

}
