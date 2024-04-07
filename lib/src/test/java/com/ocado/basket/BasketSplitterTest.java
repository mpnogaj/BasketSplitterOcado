package com.ocado.basket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ocado.basket.exceptions.ConfigurationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BasketSplitterTest {

    @Test
    void constructorDoesNotThrowException() {
        assertDoesNotThrow(() -> new BasketSplitter("src/test/resources/config.json"));
    }

    @Test
    void constructorThrowsExceptionWhenFileNotFound() {
        var exception = assertThrows(ConfigurationException.class,
                () -> new BasketSplitter("src/test/resources/notFound.json"));

        var expectedMessage = "Configuration file not found";
        var actualResponse = exception.getMessage();

        assertEquals(actualResponse, expectedMessage);
    }

    @Test
    void constructorThrowsExceptionWhenFileInvalid() {
        var exception = assertThrows(ConfigurationException.class,
                () -> new BasketSplitter("src/test/resources/invalid.json"));

        var expectedMessage = "Configuration file is invalid";
        var actualResponse = exception.getMessage();

        assertEquals(actualResponse, expectedMessage);
    }

    @ParameterizedTest
    @MethodSource
    void splitTest(String basketFile, int groupsNum, int maxGroupCount) {
        assertDoesNotThrow(() -> {
            var configurationFile = "src/test/resources/config.json";

            var basket = loadBasket(basketFile);
            var basketSplitter = new BasketSplitter(configurationFile);

            var result = basketSplitter.split(basket);

            assertEquals(result.size(), groupsNum);
            var maxGroupSize = result.values().stream().map(List::size).max(Comparator.comparingInt(o -> o));
            assertTrue(maxGroupSize.isPresent());
            assertEquals(maxGroupSize.get(), maxGroupCount);
        });
    }

    static Stream<Arguments> splitTest() {
        return Stream.of(Arguments.of("src/test/resources/basket-1.json", 2, 5),
                Arguments.of("src/test/resources/basket-2.json", 3, 13));
    }

    private List<String> loadBasket(String basketFile) throws FileNotFoundException {
        var fileReader = new FileReader(basketFile);
        var reader = new JsonReader(fileReader);
        var gson = new Gson();
        var type = new TypeToken<ArrayList<String>>() {}.getType();

        return gson.fromJson(reader, type);
    }
}