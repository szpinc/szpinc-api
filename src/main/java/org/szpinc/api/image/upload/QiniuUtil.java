package org.szpinc.api.image.upload;

import com.qiniu.util.Auth;

public class QiniuUtil {

    private String ak ;

    private String sk ;

    private String bk ;

    public QiniuUtil(String ak, String sk, String bk) {
        this.ak = ak;
        this.sk = sk;
        this.bk = bk;
    }

    public String getUploadTokean () {
        return Auth.create(ak,sk).uploadToken(bk);
    }
}
