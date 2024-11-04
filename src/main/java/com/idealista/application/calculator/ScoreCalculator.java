package com.idealista.application.calculator;

import com.idealista.application.constants.Constants;
import com.idealista.application.constants.Quality;
import com.idealista.application.constants.Typology;
import com.idealista.domain.model.Ad;
import com.idealista.domain.model.Picture;

import java.util.*;

public class ScoreCalculator implements  IScoreCalculator {
    @Override
    public void calculateScore(Ad ad) {
        int score = Constants.ZERO;

        score += calculatePictureScore(ad);
        score += calculateDescriptionScore(ad);
        score += calculateCompletenessScore(ad);

        ad.setScore(clampScore(score));
        setIrrelevantSince(ad);
    }

    private int calculatePictureScore(Ad ad) {
        int score = 0;
        if (ad.getPictures().isEmpty()) {
            score -= Constants.TEN;
        } else {
            for (Picture picture : ad.getPictures()) {
                score += Quality.HD.equals(picture.getQuality()) ? Constants.TWENTY : Constants.TEN;
            }
        }
        return score;
    }

    private int calculateDescriptionScore(Ad ad) {
        int score = 0;
        Optional<String> optDesc = Optional.ofNullable(ad.getDescription());

        if (optDesc.isPresent()) {
            String description = optDesc.get();
            if (!description.isEmpty()) {
                score += Constants.FIVE;
            }

            List<String> words = Arrays.asList(description.split(" "));
            score += calculateTypologyScore(ad, words);
            score += calculateKeywordScore(words);
        }
        return score;
    }

    private int calculateTypologyScore(Ad ad, List<String> words) {
        int score = 0;
        if (Typology.FLAT.equals(ad.getTypology())) {
            if (words.size() >= Constants.TWENTY && words.size() <= Constants.FORTY_NINE) {
                score += Constants.TEN;
            } else if (words.size() >= Constants.FIFTY) {
                score += Constants.THIRTY;
            }
        } else if (Typology.CHALET.equals(ad.getTypology()) && words.size() >= Constants.FIFTY) {
            score += Constants.TWENTY;
        }
        return score;
    }

    public static int calculateKeywordScore(List<String> words) {
        int score = 0;

        // Creamos un set para evitar duplicados y convertir las palabras a minus
        Set<String> normalizedWords = new HashSet<>();
        for (String word : words) {
            String cleanedWord = word.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚ]", "").toLowerCase();
            if (!cleanedWord.isEmpty()) {
                normalizedWords.add(cleanedWord);
            }
        }

        // Verificar las palabras clave y sumar los puntos
        if (normalizedWords.contains("luminoso")) score += Constants.FIVE;
        if (normalizedWords.contains("nuevo")) score += Constants.FIVE;
        if (normalizedWords.contains("céntrico")) score += Constants.FIVE;
        if (normalizedWords.contains("reformado")) score += Constants.FIVE;
        if (normalizedWords.contains("ático")) score += Constants.FIVE;

        return score;
    }



    private int calculateCompletenessScore(Ad ad) {
        return ad.isComplete() ? Constants.FORTY : 0;
    }

    private int clampScore(int score) {
        if (score < Constants.ZERO) {
            return Constants.ZERO;
        } else if (score > Constants.ONE_HUNDRED) {
            return Constants.ONE_HUNDRED;
        }
        return score;
    }

    private void setIrrelevantSince(Ad ad) {
        if (ad.getScore() < Constants.FORTY) {
            ad.setIrrelevantSince(new Date());
        }
    }
}

