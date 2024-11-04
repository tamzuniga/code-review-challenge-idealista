package com.idealista.domain;

import com.idealista.domain.model.Ad;

import java.util.List;
import java.util.Optional;

public interface AdRepository {
    List<Ad> findAllAds();

    void save(Ad ad);

    List<Ad> findRelevantAds();

    List<Ad> findIrrelevantAds();
    Optional<Ad> findById(Integer id);
}
