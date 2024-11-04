package com.idealista.application;

import com.idealista.application.calculator.ScoreCalculator;
import com.idealista.application.mapper.Mapper;
import com.idealista.domain.*;
import com.idealista.domain.model.Ad;
import com.idealista.domain.model.Picture;
import com.idealista.infrastructure.api.dto.PublicAd;
import com.idealista.infrastructure.api.dto.QualityAd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdsServiceImpl implements AdsService {
    @Autowired
    private ScoreCalculator scoreCalculator;

    @Autowired
    private AdRepository adRepository;
    @Autowired
    private Mapper mapper;

    @Override
    public List<PublicAd> findRelevantAds() {
        List<Ad> ads = adRepository.findRelevantAds();
        return mapper.mapAdd(ads);
    }

    @Override
    public List<QualityAd> findIrrelevantAds() {
        List<Ad> ads = adRepository.findIrrelevantAds();
        if (ads.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No irrelevant ads found");
        }

        List<QualityAd> result = new ArrayList<>();
        for (Ad ad: ads) {
            QualityAd qualityAd = new QualityAd();
            qualityAd.setDescription(ad.getDescription());
            qualityAd.setGardenSize(ad.getGardenSize());
            qualityAd.setHouseSize(ad.getHouseSize());
            qualityAd.setId(ad.getId());
            qualityAd.setPictureUrls(ad.getPictures().stream().map(Picture::getUrl).collect(Collectors.toList()));
            qualityAd.setTypology(ad.getTypology().name());
            qualityAd.setScore(ad.getScore());
            qualityAd.setIrrelevantSince(ad.getIrrelevantSince());

            result.add(qualityAd);
        }

        return result;
    }

    @Override
    public void updateAdAndCalculateScore(Integer id, PublicAd updateRequest) {
        Optional<Ad> optionalAd = adRepository.findById(id);

        if (optionalAd.isPresent()) {
            Ad ad = optionalAd.get();
            if (updateRequest.getDescription() != null) {
                ad.setDescription(updateRequest.getDescription());
            }

            //Y otros parámetros que hagan falta actualizar.
            scoreCalculator.calculateScore(ad);
            adRepository.save(ad);
        }

    }
}
