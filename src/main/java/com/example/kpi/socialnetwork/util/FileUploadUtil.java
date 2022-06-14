package com.example.kpi.socialnetwork.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {
    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public static void copyFile(String source, String destination) throws IOException {
        Path sourcePath = Paths.get(source);
        Path destinationPath = Paths.get(destination);

        if (!Files.exists(destinationPath)) {
            Files.createDirectories(destinationPath);
        }
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

    public static String saveTmpFile(Long postId, MultipartFile multipartFile) throws IOException {
        var path = String.format("user-photos/posts/%d/temp/%s",postId, RandomStringGenerator.generate(7));
        var fileName = RandomStringGenerator.generate(7);

        saveFile(path, fileName, multipartFile);
        return Path.of(path, fileName).toString();
    }

    public static boolean removeFile(String path)
    {
        try {
            return Files.deleteIfExists(Path.of(path));
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean removeDirectory(String path){
        return removeDirectory(Paths.get(path).toFile());
    }

    public static boolean removeDirectory(File directoryToBeDeleted)
    {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                removeDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}
