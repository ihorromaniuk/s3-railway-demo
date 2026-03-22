package core.basesyntax.s3railwaydemo.controller;

import core.basesyntax.s3railwaydemo.dto.ItemAddedResponse;
import core.basesyntax.s3railwaydemo.dto.ItemUrl;
import core.basesyntax.s3railwaydemo.service.StorageService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller {

    private final StorageService storageService;

    @PostMapping
    public ResponseEntity<ItemAddedResponse> save(@RequestBody MultipartFile file) throws IOException {
        return ResponseEntity.ok(storageService.save(file));
    }

    @GetMapping
    public ResponseEntity<ItemUrl> getUrl(@RequestParam String objectKey) {
        return ResponseEntity.ok(storageService.generateViewablePresignedUrl(objectKey));
    }

    @DeleteMapping
    public void deleteResource(@RequestParam String objectKey) {
        storageService.delete(objectKey);
    }
}
