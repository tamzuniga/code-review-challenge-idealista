package com.idealista.application;

import com.idealista.application.calculator.ScoreCalculator;
import com.idealista.application.constants.Quality;
import com.idealista.application.constants.Typology;
import com.idealista.domain.*;
import com.idealista.domain.model.Ad;
import com.idealista.domain.model.Picture;
import com.idealista.infrastructure.api.dto.PublicAd;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdsServiceImplTest {

    @Mock
    private AdRepository adRepository;

    @InjectMocks
    private AdsServiceImpl scoreService;
    @Spy
    private ScoreCalculator scoreCalculator;

    @Test
    public void calculateScoresRelevantAddTest() {
        when(adRepository.findById(any())).thenReturn(Optional.of(relevantAd()));
        scoreService.updateAdAndCalculateScore(irrelevantAd().getId(), publicAd());
        verify(adRepository).findById(any());
        verify(adRepository, times(1)).save(any());
    }
    @Test
    public void calculateScoresIrrelevantAddTest() {
        when(adRepository.findById(any())).thenReturn(Optional.of(irrelevantAd()));
        scoreService.updateAdAndCalculateScore(irrelevantAd().getId(), publicAd());
        verify(adRepository).findById(any());
        verify(adRepository, times(1)).save(any());
    }

    private Ad relevantAd() {
        return new Ad(1,
                Typology.FLAT,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras dictum felis elit, vitae cursus erat blandit vitae. Maecenas eget efficitur massa. Maecenas ut dolor eget enim consequat iaculis vitae nec elit. Maecenas eu urna nec massa feugiat pharetra. Sed eu quam imperdiet orci lobortis fermentum. Sed odio justo, congue eget iaculis.",
                Arrays.asList(new Picture(1, "http://urldeprueba.com/1", Quality.HD), new Picture(2, "http://urldeprueba.com/2", Quality.HD)),
                50,
                null);
    }

    private Ad irrelevantAd() {
        return new Ad(1,
                Typology.FLAT,
                "",
                Collections.emptyList(),
                100,
                null);
    }

    private PublicAd publicAd(){
        PublicAd ad = new PublicAd();

        ad.setId(1);  // Asigna un ID al anuncio
        ad.setTypology("Flat");  // Asigna la tipología, por ejemplo, "House" o "Flat"
        ad.setDescription("Ático céntrico muy luminoso y recién reformado, parece nuevo.");

        // Lista de URLs de imágenes
        List<String> pictureUrls = Arrays.asList(
                "https://example.com/picture1.jpg",
                "https://example.com/picture2.jpg"
        );
        ad.setPictureUrls(pictureUrls);

        ad.setHouseSize(120);
        ad.setGardenSize(50);

        return ad;
    }

}