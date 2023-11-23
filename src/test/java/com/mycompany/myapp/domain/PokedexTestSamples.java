package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PokedexTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Pokedex getPokedexSample1() {
        return new Pokedex().id(1L);
    }

    public static Pokedex getPokedexSample2() {
        return new Pokedex().id(2L);
    }

    public static Pokedex getPokedexRandomSampleGenerator() {
        return new Pokedex().id(longCount.incrementAndGet());
    }
}
