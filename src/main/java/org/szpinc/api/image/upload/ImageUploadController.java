package org.szpinc.api.image.upload;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.szpinc.api.utils.MD5Util;
import org.szpinc.api.utils.MD5Util2;

import java.io.IOException;
import java.util.Date;

/**
 *
 */
@RestController
public class ImageUploadController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${Qiniu.AccessKey}")
    private String ak;

    @Value("${Qiniu.SecretKey}")
    private String sk;

    @Value("${Qiniu.bucket}")
    private String bk;

    @PostMapping("/api/upload/v1/file")
    public ResultMessage uploadFile(MultipartFile file) {
        byte[] bytes = null;
        String fileName = null;

        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (bytes == null) {
            logger.info("上传文件没有数据");
            return ResultMessage.build(500, "没有数据");
        }

        try {
            String hashCode = MD5Util.md5HashCode(file.getInputStream());
            logger.info("文件的HashCode："+hashCode);
            fileName = file.getOriginalFilename();
            String extName = fileName.substring(fileName.lastIndexOf('.'));
            fileName = MD5Util2.getStringMD5(hashCode + new Date()+"")+extName;
            logger.info(fileName);

        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }

        //创建七牛云配置类
        Configuration configuration = new Configuration(Zone.zone0());
        //创建文件上传管理器
        UploadManager uploadManager = new UploadManager(configuration);
        //
        QiniuUtil qiniuUtil = new QiniuUtil(ak, sk, bk);
        //获取验证器
        String uploadTokean = qiniuUtil.getUploadTokean();

        if (uploadTokean == null) {
            return ResultMessage.build(500, "权限错误");
        }
        try {
            Response response = uploadManager.put(bytes, fileName, uploadTokean);
            DefaultPutRet defaultPutRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            logger.info("文件名："+defaultPutRet.key);
            logger.info("文件Hash值："+defaultPutRet.hash);
            return ResultMessage.ok("https://img.szpinc.org/" + defaultPutRet.key);
        } catch (QiniuException e) {
            logger.error(e.getMessage(), e);
        }
        return ResultMessage.build(500, "ERROR");
    }
}
