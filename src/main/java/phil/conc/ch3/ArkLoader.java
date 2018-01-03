package phil.conc.ch3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class ArkLoader {

    private ArrayList<AnimalPair> ark;

    public int loadTheArk(Collection<Animal> candidates) {
        HashSet<Animal> animals;
        int numPairs = 0;
        Animal candidate = null;
// animals confined to method, don’t let them escape!
        animals = new HashSet<Animal>();
        animals.addAll(candidates);
        for (Animal a : animals) {
            if (candidate == null || !candidate.isPotentialMate(a))
                candidate = a;
            else {
                ark.add(new AnimalPair(candidate, a));
                ++numPairs;
                candidate = null;
            }
        }
        return numPairs;
    }

    private class Animal {
        public boolean isPotentialMate(Animal a) {
            return true;
        }
    }

    private class AnimalPair {
        public AnimalPair(Animal candidate, Animal a) {
            super();
        }
    }
}