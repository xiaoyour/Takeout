package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


@Api("通用控制")
@RestController
@Slf4j
public class CommonController {

    @Autowired
    AliOssUtil aliOssUtil;


    /**
     * 文件上传
     * @param file
     * @return
     */
    @ApiOperation("文件上传")
    @PostMapping("/admin/common/upload")
    public Result<String> FileUpdate(MultipartFile file){
        log.info("开始上传文件：{}",file);
//        获得原始文件名
        String originalFilename = file.getOriginalFilename();
//        截取文件格式
        String fileFormat = originalFilename.substring(originalFilename.lastIndexOf("."));
//        拼接UUID和文件格式形成上传文件名
        String uploadFileName = UUID.randomUUID()+ fileFormat;
        try {
//            字符串upload为图片在云上的url
            String upload = aliOssUtil.upload(file.getBytes(), uploadFileName);
            return Result.success(upload);
        } catch (IOException e) {
            log.error(MessageConstant.UPLOAD_FAILED);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }

    }
}
