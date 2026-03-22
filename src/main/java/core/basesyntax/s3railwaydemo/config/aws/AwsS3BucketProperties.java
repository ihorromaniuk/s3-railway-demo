package core.basesyntax.s3railwaydemo.config.aws;

import core.basesyntax.s3railwaydemo.validation.BucketExists;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.reflectoring.aws.s3")
public class AwsS3BucketProperties {

  @BucketExists
  @NotBlank(message = "S3 bucket name must be configured")
  private String bucketName;

  @Valid
  private PresignedUrl presignedUrl = new PresignedUrl();

  @Getter
  @Setter
  @Validated
  public static class PresignedUrl {

    /**
     * The validity period in <b>seconds</b> for the generated presigned URLs. The
     * URLs would not be accessible post expiration.
     */
    @NotNull(message = "S3 presigned URL validity must be specified")
    @Positive(message = "S3 presigned URL validity must be a positive value")
    private Integer validity;

  }

  public Duration getPresignedUrlValidity() {
    var urlValidity = this.presignedUrl.validity;
    return Duration.ofSeconds(urlValidity);
  }
}
