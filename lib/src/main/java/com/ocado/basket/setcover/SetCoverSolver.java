package com.ocado.basket.setcover;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Class for solving standard set cover problem <br>
 * Uses exponential algorithm so number of sets must be small
 *
 * @param <UniverseT> Type of the items in the universe
 * @param <SetDescriptorT> Type of the set descriptor ex. set name or id
 */
public class SetCoverSolver<UniverseT, SetDescriptorT> {
    private final Set<UniverseT> universe;
    private final Map<SetDescriptorT, List<UniverseT>> sets;

    /**
     * @param universe elements to cover
     * @param sets sets of elements to use
     */
    public SetCoverSolver(Set<UniverseT> universe, Map<SetDescriptorT, List<UniverseT>> sets) {
        this.universe = universe;
        this.sets = sets;
    }

    /**
     * Comparator for set covers
     * @param <SetDescriptorT> Type of the set descriptor
     */
    public interface SetCoverComparator<SetDescriptorT> {

        /**
         * Function which compares two set descriptors
         * @param candidate candidate to test
         * @param currentBest current best set cover
         * @return {@code true} if {@code candidate} is better than {@code currentBest}, {@code false} otherwise
         */
        boolean isBetterSetCover(Set<SetDescriptorT> candidate, Set<SetDescriptorT> currentBest);
    }

    /**
     * Finds best set cover based of the comparator
     * @param comparator comparator for set covers. See {@link SetCoverComparator} for more info
     * @return Set of set descriptors in best set cover
     */
    public Set<SetDescriptorT> findBestSetCover(SetCoverComparator<SetDescriptorT> comparator) {
        return backtrack(universe, new HashSet<>(), null, comparator);
    }


    /**
     * The main function responsible for finding best set cover <br>
     * Uses backtracking to solve this problem
     *
     * @param currentUniverse universe in the current step
     * @param currentSetCover set cover in the current step
     * @param bestSetCover the best set cover currently found
     * @param comparator set covers comparator
     * @return best set cover
     */
    private Set<SetDescriptorT> backtrack(Set<UniverseT> currentUniverse,
                                          Set<SetDescriptorT> currentSetCover, Set<SetDescriptorT> bestSetCover,
                                          SetCoverComparator<SetDescriptorT> comparator) {
        if (currentUniverse.isEmpty()) {
            if(comparator.isBetterSetCover(currentSetCover, bestSetCover)) {
                bestSetCover = new HashSet<>(currentSetCover);
            }
            return bestSetCover;
        }

        final var itemToCover = currentUniverse.iterator().next();

        /*
         * We don't have to worry about reusing sets.
         * Suppose we decided to reuse set. This means this set covers element itemToCover,
         * but we used this set before and therefore removed all items in the universe from this set.
         */
        for (final var set : sets.entrySet()) {
            final var setDescriptor = set.getKey();
            final var coveredItems = set.getValue();

            if(coveredItems.contains(itemToCover)) {
                final var newUniverse = new HashSet<>(currentUniverse);
                /*
                 * Set.removeAll could be slower.
                 * See this: https://stackoverflow.com/a/28672583
                 */
                coveredItems.forEach(newUniverse::remove);

                final var newSetCover = new HashSet<>(currentSetCover);
                newSetCover.add(setDescriptor);

                bestSetCover = backtrack(newUniverse, newSetCover, bestSetCover, comparator);
            }
        }

        return bestSetCover;
    }
}
