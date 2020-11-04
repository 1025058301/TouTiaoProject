package pers.lcy.toutiao.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pers.lcy.toutiao.util.CommonUtil;

import java.io.IOException;
import java.util.UUID;

@Service
public class QiniuCloudService {
    public static Logger logger= LoggerFactory.getLogger(QiniuCloudService.class);

    Configuration cfg=new Configuration(Region.region2());
    UploadManager uploadManager=new UploadManager(cfg);
    String AccessKey="ZiJXZMhWzY_1YyHzaQSg7ONH5pgdku-lage9V4hl";
    String SecretKey="gvwzt4GhVtJYckhtyZ5pWS_i6KFWCJ_wF4VnOOke";
    //存储空间名
    String bucketName="cccy";
    Auth auth=Auth.create(AccessKey,SecretKey);

    private String getupLoadToken(){
        return auth.uploadToken(bucketName);
    }

    public String saveImage(MultipartFile file) throws IOException {
        try{
            int dotIndex = file.getOriginalFilename().lastIndexOf(".");
            if (dotIndex < 0) {
                return null;
            }
            String fileExt=file.getOriginalFilename().substring(dotIndex+1).toLowerCase();
            if(!CommonUtil.isFileAllowed(fileExt)){
                return null;
            }
            String fileName= UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
            Response response=uploadManager.put(file.getBytes(),fileName,getupLoadToken());
            if(!(response.isOK()&&response.isJson())){
                logger.error("七牛异常"+response.bodyString());
                return null;
            }
            return CommonUtil.QiniuDomain+ JSONObject.parseObject(response.bodyString()).get("key");
        }catch (QiniuException e){
            logger.error(e.getMessage());
            return null;
        }
    }
}
