package com.idealista.application;

import com.idealista.infrastructure.api.dto.PublicAd;
import com.idealista.infrastructure.api.dto.QualityAd;

import java.util.List;

public interface AdsService {

    List<PublicAd> findRelevantAds();
    List<QualityAd> findIrrelevantAds();
    void updateAdAndCalculateScore(Integer id, PublicAd publicAd);
}
