package io.github.kimmking.javacourse.dfs.controller;

import io.github.kimmking.javacourse.dfs.FileConfig;
import io.github.kimmking.javacourse.dfs.JavaConfig;
import io.github.kimmking.javacourse.dfs.model.User;
import io.github.kimmking.javacourse.dfs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    UserService userService;

    @Autowired
    FastFileStorageClient storageClient;

    @GetMapping("/test")
    public User createUser() {
        return new User(1L,"KK01", "null-"+System.currentTimeMillis());
    }

    @PostMapping("/")
    public User upload(@RequestParam("file") CommonsMultipartFile file, @RequestParam("name") String name) throws IOException {
        long startTime=System.currentTimeMillis();
        System.out.println("文件名称：" + file.getOriginalFilename());
        String fileName= startTime + "-" + file.getOriginalFilename();
        File newFile=new File(FileConfig.PIC_PATH + "/" +fileName);
        System.out.println("文件路径：" + newFile.getAbsolutePath());
        file.transferTo(newFile);
        long  endTime=System.currentTimeMillis();
        System.out.println("文件处理时间："+(endTime-startTime)+"ms");
        User user = new User(startTime, name, "http://localhost:8011/pic/"+fileName);
        return userService.create(user);
    }

    @PostMapping("/uploadDfs")
    public User uploadDfs(@RequestParam("file") CommonsMultipartFile file, @RequestParam("name") String name) throws IOException {
        long startTime=System.currentTimeMillis();
        System.out.println("文件名称：" + file.getOriginalFilename());
        String fileName= startTime + "-" + file.getOriginalFilename();
        File newFile=new File(FileConfig.PIC_PATH + "/" +fileName);
        System.out.println("文件路径：" + newFile.getAbsolutePath());
        file.transferTo(newFile);

        FileInputStream is = new FileInputStream(newFile);
        StorePath storePath = storageClient.uploadFile(is, newFile.length(), org.apache.commons.io.FilenameUtils.getExtension(newFile.getName()), null);
        is.close();
        String fullPath = storePath.getFullPath();
        System.out.println("FastDFS Path = " + fullPath);

        long  endTime=System.currentTimeMillis();
        System.out.println("文件处理时间："+(endTime-startTime)+"ms");
        User user = new User(startTime, name, fullPath);
        return userService.create(user);
    }

    @RequestMapping("/findById")
    public User findById(@RequestParam("id") Long id){
        return userService.findById(id);
    }

    // get pic



}
