package link.sendwish.backend.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Upload {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    // UUID로 생성한 파일명으로 s3에 업로드
    public String upload(MultipartFile multipartFile) throws IOException {
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        // ObjectMetadata를 통해 ContentLength를 S3로 알려줌
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getInputStream().available());

        // 파일 Stream을 열어서 S3에 파일을 업로드
        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objectMetadata);

        // getUrl 메소드를 통해서 s3에 업로드된 사진 URL을 가져오는 방식
        return amazonS3.getUrl(bucket, s3FileName).toString();
    }


}
