package com.example.hundredplaces.fakedata

import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceTypeEnum
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages

object FakePlaceDataSource {
    val PlacesList = listOf(
        PlaceWithCityAndImages(
            Place(
                1,
                "Национален исторически музей",
                42.65542986025372,
                23.271044935725428,
                4.7,
                "",
                false,
                PlaceTypeEnum.MUSEUM,
                1
            ),
            "гр. София",
            listOf("img1.src")
        ),
        PlaceWithCityAndImages(
            Place(2,
                "Амфитеатър",
                42.14748477241597,
                24.751403134086157,
                4.6,
                "",
                true,
                PlaceTypeEnum.OTHER,
                2
            ),
            "гр. Пловдив",
            listOf("img2.src")
        ),
        PlaceWithCityAndImages(
            Place(3,
                "вр. Вихрен",
                41.76912487394589,
                23.399031256884356,
                4.9,
                "",
                true,
                PlaceTypeEnum.PEAK,
                3
            ),
            "Пирин",
            listOf("img3.src")
        ),
        PlaceWithCityAndImages(
            Place(4,
                "Царевец",
                43.085785389796406,
                25.651566436279538,
                4.7,
                "",
                true,
                PlaceTypeEnum.FORTRESS,
                4
            ),
            "гр. Велико Търново",
            listOf("img4.src")
        ),
    )
}