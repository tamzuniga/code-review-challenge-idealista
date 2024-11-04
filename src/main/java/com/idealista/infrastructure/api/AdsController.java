package com.idealista.infrastructure.api;

import java.util.List;

import com.idealista.application.AdsService;
import com.idealista.infrastructure.api.dto.PublicAd;
import com.idealista.infrastructure.api.dto.QualityAd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdsController {

    @Autowired
    private AdsService adsService;

    @GetMapping("/ads/quality")
    public ResponseEntity<List<QualityAd>> qualityListing() {
        return ResponseEntity.ok(adsService.findIrrelevantAds());
    }

    @GetMapping("/ads/public")
    public ResponseEntity<List<PublicAd>> publicListing() {
        return ResponseEntity.ok(adsService.findRelevantAds());
    }

    @PutMapping("ads/{id}/score")
    public ResponseEntity<Void> updateScore(@PathVariable Integer id, @RequestBody PublicAd updateRequest) {
        adsService.updateAdAndCalculateScore(id, updateRequest);
        return ResponseEntity.ok().build();

    }
}
