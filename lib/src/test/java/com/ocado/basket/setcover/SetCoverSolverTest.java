package com.ocado.basket.setcover;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SetCoverSolverTest {
    @ParameterizedTest
    @MethodSource
    void findBestSetCoverInt(Set<Integer> universe, Map<String, List<Integer>> sets, int expectedSize) {
        var solver = new SetCoverSolver<Integer, String>(universe, sets);

        var setCover = solver.findBestSetCover((candidate, currentBest) -> {
            if (currentBest == null) return true;
            return candidate.size() < currentBest.size();
        });

        assertNotNull(setCover);
        assertEquals(setCover.size(), expectedSize);
    }

    static Stream<Arguments> findBestSetCoverInt() {
        var universe = new HashSet<Integer>() {{
            for (int i = 1; i <= 5; i++) {
                add(i);
            }
        }};

        var sets1 = new HashMap<String, List<Integer>>() {{
            put("Gr 1", new ArrayList<>() {{
                add(1);
                add(2);
                add(3);
            }});
            put("Gr 2", new ArrayList<>() {{
                add(2);
                add(4);
            }});
            put("Gr 3", new ArrayList<>() {{
                add(3);
                add(4);
            }});
            put("Gr 4", new ArrayList<>() {{
                add(4);
                add(5);
            }});
        }};

        var sets2 = new HashMap<String, List<Integer>>(){{
            put("Gr 1", new ArrayList<>() {{
                add(1);
                add(2);
                add(3);
            }});
            put("Gr 2", new ArrayList<>() {{
                add(1);
                add(2);
                add(3);
                add(5);
            }});
            put("Gr 3", new ArrayList<>() {{
                add(1);
                add(2);
                add(3);
                add(4);
            }});
            put("Gr 4", new ArrayList<>() {{
                add(3);
                add(4);
                add(5);
            }});
            put("Gr 5", new ArrayList<>() {{
                add(2);
                add(4);
                add(5);
            }});
        }};

        return Stream.of(Arguments.of(universe, sets1, 2),
                Arguments.of(universe, sets2, 2));
    }
}