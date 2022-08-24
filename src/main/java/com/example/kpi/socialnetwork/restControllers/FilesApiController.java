package com.example.kpi.socialnetwork.restControllers;

import com.example.kpi.socialnetwork.util.FileUploadUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/files")
public class FilesApiController {

    @PutMapping("/temp/save/{postId}")
    public String saveTempFile(@PathVariable("postId") Long postId,
                               @RequestParam("tweet-image")MultipartFile file) throws IOException {
        return FileUploadUtil.saveTmpFile(postId, file);
    }

    @DeleteMapping("/temp")
    public ResponseEntity<String> deleteFile(@RequestParam("fileName")String fileName){
        return new ResponseEntity<>(Boolean.toString(FileUploadUtil.removeDirectory(fileName)), HttpStatus.OK);
    }
}
