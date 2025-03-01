package com.sib.sayitbettr.Controller;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@RestController
@RequestMapping("/voice")
public class VoiceComparisonController {

    private final String FLASK_API_URL = "http://127.0.0.1:5000/compare";

    @PostMapping("/compare")
    public ResponseEntity<String> compareVoices(@RequestParam("file1") MultipartFile file1,
                                                @RequestParam("file2") MultipartFile file2) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Multipart Body için request hazırlıyoruz
            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file1", file1.getResource());
            body.add("file2", file2.getResource());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Flask API'yi çağır
            ResponseEntity<String> response = restTemplate.exchange(FLASK_API_URL, HttpMethod.POST, requestEntity, String.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hata: " + e.getMessage());
        }
    }
}
