package link.sendwish.backend.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import link.sendwish.backend.service.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class AmazonS3Controller {
    private final AmazonS3Service amazonS3Service;

    /**
     * Amazon S3에 파일을 업로드
     * @return 성공시 200 Success와 함께 업로드 된 파일의 파일명 리스트 반환
     */
    @ApiOperation(value= "Amazon S3에 파일을 업로드", notes = "Amazon S3에 파일을 업로드")
    @PostMapping("/file")
    public ResponseEntity<List<String>> uploadFile(@ApiParam(value="파일들(여러 파일 업로드 가능)", required = true)
                                                   @RequestPart List<MultipartFile> multipartFiles){
        return ResponseEntity.ok(amazonS3Service.uploadFile(multipartFiles));
    }

    /**
     * Amazon S3에 업로드 된 파일을 삭제
     * @return 성공시 200 Success
     */
    @ApiOperation(value="Amazon S3에 업로드된 파일을 삭제", notes = "Amazon S3에 업로드된 파일 삭제")
    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(@ApiParam(value="파일 하나 삭제", required = true)
                                           @RequestPart String fileName){
        amazonS3Service.deleteFile(fileName);
        return ResponseEntity.ok(null);
    }

}
