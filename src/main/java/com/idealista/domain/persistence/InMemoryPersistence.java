package com.idealista.domain.persistence;

import com.idealista.application.constants.Constants;
import com.idealista.application.constants.Quality;
import com.idealista.application.constants.Typology;
import com.idealista.domain.*;
import com.idealista.domain.model.Ad;
import com.idealista.domain.model.Picture;
import com.idealista.domain.persistence.vo.AdVO;
import com.idealista.domain.persistence.vo.PictureVO;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryPersistence implements AdRepository {

    private List<AdVO> ads;
    private List<PictureVO> pictures;

    public InMemoryPersistence() {
        ads = new ArrayList<AdVO>();
        ads.add(new AdVO(1, "CHALET", "Este piso es una ganga, compra, compra, COMPRA!!!!!", Collections.<Integer>emptyList(), 300, null, null, null));
        ads.add(new AdVO(2, "FLAT", "Nuevo ático céntrico recién reformado. No deje pasar la oportunidad y adquiera este ático de lujo", Arrays.asList(4), 300, null, null, null));
        ads.add(new AdVO(3, "CHALET", "", Arrays.asList(2), 300, null, null, null));
        ads.add(new AdVO(4, "FLAT", "Ático céntrico muy luminoso y recién reformado, parece nuevo", Arrays.asList(5), 300, null, null, null));
        ads.add(new AdVO(5, "FLAT", "Pisazo,", Arrays.asList(3, 8), 300, null, null, null));
        ads.add(new AdVO(6, "GARAGE", "", Arrays.asList(6), 300, null, null, null));
        ads.add(new AdVO(7, "GARAGE", "Garaje en el centro de Albacete", Collections.<Integer>emptyList(), 300, null, null, null));
        ads.add(new AdVO(8, "CHALET", "Maravilloso chalet situado en lAs afueras de un pequeño pueblo rural. El entorno es espectacular, las vistas magníficas. ¡Cómprelo ahora!", Arrays.asList(1, 7), 300, null, null, null));

        pictures = new ArrayList<PictureVO>();
        pictures.add(new PictureVO(1, "http://www.idealista.com/pictures/1", "SD"));
        pictures.add(new PictureVO(2, "http://www.idealista.com/pictures/2", "HD"));
        pictures.add(new PictureVO(3, "http://www.idealista.com/pictures/3", "SD"));
        pictures.add(new PictureVO(4, "http://www.idealista.com/pictures/4", "HD"));
        pictures.add(new PictureVO(5, "http://www.idealista.com/pictures/5", "SD"));
        pictures.add(new PictureVO(6, "http://www.idealista.com/pictures/6", "SD"));
        pictures.add(new PictureVO(7, "http://www.idealista.com/pictures/7", "SD"));
        pictures.add(new PictureVO(8, "http://www.idealista.com/pictures/8", "HD"));
    }

    @Override
    public List<Ad> findAllAds() {
        return ads
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Ad ad) {
        ads.removeIf(x -> x.getId().equals(ad.getId()));
        ads.add(mapToPersistence(ad));

        ad.getPictures()
            .forEach(this::save);
    }

    private void save(Picture picture) {
        pictures.removeIf(x -> x.getId().equals(picture.getId()));
        pictures.add(mapToPersistence(picture));
    }

    @Override
    public List<Ad> findRelevantAds() {
        return ads
                .stream()
                .filter(x -> x.getScore() >= Constants.FORTY)
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ad> findIrrelevantAds() {
        return ads.parallelStream() // Cambia a un flujo paralelo
                .filter(ad -> ad.getScore() < Constants.FORTY)
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Ad> findById(Integer id) {
        return ads
                .stream()
                .filter(x ->x.getId().equals(id))
                .map(this::mapToDomain).findFirst();
    }

    private Ad mapToDomain(AdVO adVO) {
        return new Ad(adVO.getId(),
                Typology.valueOf(adVO.getTypology()),
                adVO.getDescription(),
                adVO.getPictures().stream().map(this::mapToDomain).collect(Collectors.toList()),
                adVO.getHouseSize(),
                adVO.getGardenSize(),
                adVO.getScore(),
                adVO.getIrrelevantSince());
    }

    private Picture mapToDomain(Integer pictureId) {
        return pictures
                .stream()
                .filter(x -> x.getId().equals(pictureId))
                .findFirst()
                .map(pictureVO -> new Picture(pictureVO.getId(), pictureVO.getUrl(), Quality.valueOf(pictureVO.getQuality())))
                .orElse(null);
    }

    private AdVO mapToPersistence(Ad ad) {
        return new AdVO(ad.getId(),
                ad.getTypology().name(),
                ad.getDescription(),
                ad.getPictures().stream().map(Picture::getId).collect(Collectors.toList()),
                ad.getHouseSize(),
                ad.getGardenSize(),
                ad.getScore(),
                ad.getIrrelevantSince());
    }

    private PictureVO mapToPersistence(Picture picture) {
        return new PictureVO(picture.getId(),
                picture.getUrl(),
                picture.getQuality().name());
    }

}
