package com.example.kpi.socialnetwork.restControllers;

import com.example.kpi.socialnetwork.util.FileUploadUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/files")
public class FilesApiController {

    @PutMapping("/temp/save/{postId}")
    public String saveTempFile(@PathVariable("postId") Long postId, @RequestParam("tweet-image")MultipartFile file) throws IOException {
        return FileUploadUtil.saveTmpFile(postId, file);
    }

    @DeleteMapping("/temp/{fileName}")
    public boolean deleteFile(@PathVariable("fileName")String fileName){
        return FileUploadUtil.removeFile(fileName);
    }
}
