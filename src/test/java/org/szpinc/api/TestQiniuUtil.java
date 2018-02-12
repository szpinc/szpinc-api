package org.szpinc.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.szpinc.api.image.upload.QiniuUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestQiniuUtil {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${Qiniu.AccessKey}")
    private String ak ;

    @Value("${Qiniu.SecretKey}")
    private String sk ;

    @Value("${Qiniu.bucket}")
    private String bk ;


    @Test
    public void testGetUploadTokean () {



        QiniuUtil qiniuUtil = new QiniuUtil(ak, sk, bk);
        String uploadTokean = qiniuUtil.getUploadTokean();
        logger.info(uploadTokean);
    }

}
