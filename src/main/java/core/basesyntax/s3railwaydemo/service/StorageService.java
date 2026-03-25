package core.basesyntax.s3railwaydemo.service;

import core.basesyntax.s3railwaydemo.config.aws.AwsS3BucketProperties;
import core.basesyntax.s3railwaydemo.dto.ItemAddedResponse;
import core.basesyntax.s3railwaydemo.dto.ItemPutUrl;
import core.basesyntax.s3railwaydemo.dto.ItemUrl;
import io.awspring.cloud.s3.S3Template;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(AwsS3BucketProperties.class)
public class StorageService {

  private final S3Template s3Template;
  private final AwsS3BucketProperties awsS3BucketProperties;

  public ItemAddedResponse save(MultipartFile file) throws IOException {
    var objectKey = file.getOriginalFilename();
    var bucketName = awsS3BucketProperties.getBucketName();
    if (objectKey == null) {
      throw new RuntimeException("smth went wrong");
    }
    s3Template.upload(bucketName, objectKey, file.getInputStream());
    return new ItemAddedResponse(objectKey);
  }

  public ItemUrl generateViewablePresignedUrl(@NonNull final String objectKey) {
    final var bucketName = awsS3BucketProperties.getBucketName();
    final var urlValidity = awsS3BucketProperties.getPresignedUrlValidity();

    return new ItemUrl(s3Template.createSignedGetURL(bucketName, objectKey, urlValidity));
  }

  public void delete(String objectKey) {
    var bucketName = awsS3BucketProperties.getBucketName();
    s3Template.deleteObject(bucketName, objectKey);
  }

  public ItemPutUrl generateUploadablePresignedUrl(@NonNull final String objectKey) {
    final var bucketName = awsS3BucketProperties.getBucketName();
    final var urlValidity = awsS3BucketProperties.getPresignedUrlValidity();

    return new ItemPutUrl(s3Template.createSignedPutURL(bucketName, objectKey, urlValidity));
  }
}
