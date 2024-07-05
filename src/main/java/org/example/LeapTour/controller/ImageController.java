package org.example.LeapTour.controller;

import cn.dev33.satoken.util.SaResult;
import org.example.LeapTour.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/image/")
@CrossOrigin(origins = "http://localhost:5179", allowCredentials = "true")
public class ImageController {
    @Autowired
    private ImageUtils imageUtils;

    @PostMapping("upload")
    public SaResult uploadImage(@RequestParam(value = "file", required = false) MultipartFile[] multipartFile) {
        if (ObjectUtils.isEmpty(multipartFile)) {
            return SaResult.error("请上传图片");
        } else if (multipartFile.length > 1) {
            return SaResult.error("只能上传一张图片");
        } else {
            imageUtils.uploadImages(multipartFile);
            Map<String, List<String>> uploadImagesUrl = imageUtils.uploadImages(multipartFile);
            return SaResult.data(uploadImagesUrl);
        }
    }
}
