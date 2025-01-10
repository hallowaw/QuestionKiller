package site.yanbin.allGlobal.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class CosUtils {

    @Value("${tencent.cos.secretId}")
    private String secretId;

    @Value("${tencent.cos.secretKey}")
    private String secretKey;

    @Value("${tencent.cos.bucketName}")
    private String bucketName;

    @Value("${tencent.cos.region}")
    private String region;

    public String upload(MultipartFile image) throws Exception {
        // 获取文件名和后缀
        String originalFilename = image.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;

        // 创建腾讯云 COS 客户端
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region regionObj = new Region(region);
        ClientConfig clientConfig = new ClientConfig(regionObj);
        COSClient cosClient = new COSClient(cred, clientConfig);

        try {
            // 将 MultipartFile 转换为临时文件
            File tempFile = convertMultipartFileToFile(image);

            // 使用临时文件上传
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, tempFile);
            PutObjectResult result = cosClient.putObject(putObjectRequest);

            // 上传成功后，返回文件的 URL 地址
            String imageUrl = "https://" + bucketName + ".cos." + region + ".myqcloud.com/" + fileName;
            return imageUrl;

        } catch (CosClientException e) {
            // 错误处理
            e.printStackTrace();
            return null;
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }

    // 辅助方法：将 MultipartFile 转换为 File
    private File convertMultipartFileToFile(MultipartFile image) throws IOException {
        // 创建临时文件
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
        image.transferTo(tempFile);  // 将文件内容写入到临时文件
        return tempFile;
    }
}
