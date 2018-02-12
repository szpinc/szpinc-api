package org.szpinc.api;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.szpinc.api.image.upload.QiniuUtil;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestFileUpload {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${Qiniu.AccessKey}")
    private String ak ;

    @Value("${Qiniu.SecretKey}")
    private String sk ;

    @Value("${Qiniu.bucket}")
    private String bk ;

    @Test
    public void upload () {

        String filePath = "c:\\material.png";

        Configuration configuration = new Configuration(Zone.zone0());

        UploadManager uploadManager = new UploadManager(configuration);

        QiniuUtil qiniuUtil = new QiniuUtil(ak, sk, bk);

        String uploadTokean = qiniuUtil.getUploadTokean();

        try {
            Response response = uploadManager.put(filePath, null, uploadTokean);
            DefaultPutRet defaultPutRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            logger.info(defaultPutRet.key);
            logger.info(defaultPutRet.hash);
        } catch (QiniuException e) {
            logger.error(e.getMessage(),e);
        }

    }

}
