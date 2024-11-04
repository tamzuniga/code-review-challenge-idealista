package com.idealista.application.mapper;

import com.idealista.domain.model.Ad;
import com.idealista.domain.model.Picture;
import com.idealista.infrastructure.api.dto.PublicAd;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper implements IMapper{
    @Override
    public List<PublicAd> mapAdd(List<Ad> adList) {
        adList.sort(Comparator.comparing(Ad::getScore));
        List<PublicAd> result = new ArrayList<>();
        for (Ad ad: adList) {
            PublicAd publicAd = new PublicAd();
            publicAd.setDescription(ad.getDescription());
            publicAd.setGardenSize(ad.getGardenSize());
            publicAd.setHouseSize(ad.getHouseSize());
            publicAd.setId(ad.getId());
            publicAd.setPictureUrls(ad.getPictures().stream().map(Picture::getUrl).collect(Collectors.toList()));
            publicAd.setTypology(ad.getTypology().name());

            result.add(publicAd);
        }
        return result;
    }
}
