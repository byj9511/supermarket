package com.byy.product;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
public class ProductTest {
    @Test
    public void OSSDemo1() throws FileNotFoundException {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-shanghai.aliyuncs.com";
// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4GK32VvkyhJ7XN2udqtW";
        String accessKeySecret = "WIQksaITeucbZYrr20Cb24MzfjQkqG";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);


// 创建PutObjectRequest对象。
        InputStream fileInputStream = new FileInputStream("E:\\BaiduNetdiskDownload\\谷粒商城电商项目-2020-尚硅谷-雷丰阳\\分布式基础（全栈开发篇）\\资料源码\\docs\\pics\\2b1837c6c50add30.jpg");
// <yourObjectName>表示上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        PutObjectRequest putObjectRequest = new PutObjectRequest("byy-product", "2b1837c6c50add30.jpg", fileInputStream);
// 上传文件流。


// 关闭OSSClient。太早关闭，还没上传完
//        ossClient.shutdown();
// 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
// ObjectMetadata metadata = new ObjectMetadata();
// metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
// metadata.setObjectAcl(CannedAccessControlList.Private);
// putObjectRequest.setMetadata(metadata);

// 上传字符串。
        ossClient.putObject(putObjectRequest);

// 关闭OSSClient。
        ossClient.shutdown();
    }
    @Test
    void test2(){
        System.out.println(1);
    }
}
