package org.vivi.framework.iasyncexcel.starter.context.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.vivi.framework.iasyncexcel.core.service.IStorageService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

@Slf4j
public class ServerLocalStorageService implements IStorageService, InitializingBean {
    private final static String DEFAULT_TEMP_PATH = "excel";
    private String tempPath;

    @Override
    public void afterPropertiesSet() throws Exception {
        // Initialize temp path in user directory if not set
        if (tempPath == null) {
            tempPath = System.getProperty("user.dir") + File.separator + DEFAULT_TEMP_PATH;
            File dir = new File(tempPath);
            if (!dir.exists() && !dir.mkdirs()) {
                log.warn("Failed to create directory: {}", tempPath);
            }
        }
        log.info("Excel storage directory: {}", tempPath);
    }

    @Override
    public String write(String name, Consumer<OutputStream> osConsumer) throws Exception {
        Path filePath = Paths.get(tempPath, name);
        Files.createDirectories(filePath.getParent());

        try (OutputStream os = Files.newOutputStream(filePath)) {
            osConsumer.accept(os);
        }

        return filePath.toString();
    }
    
    @Override
    public String write(String name, InputStream data) throws Exception {
        String filePath = tempPath + File.separator + name;
        File file = new File(filePath);

        // Create parent directories if needed
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            throw new IOException("Failed to create directory: " + parentFile);
        }

        // Create the file if it doesn't exist
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Failed to create file: " + filePath);
        }

        // Write the data
        try (InputStream is = data) {
            FileUtils.copyInputStreamToFile(is, file);
        }

        return filePath;
    }
    
    @Override
    public InputStream read(String path) throws Exception {
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("File not found: " + path);
        }
        return Files.newInputStream(file.toPath());
    }
    
    @Override
    public boolean delete(String path) throws Exception {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
