package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PokemonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Pokemon getPokemonSample1() {
        return new Pokemon().id(1L).nombre("nombre1").tipo("tipo1").elemento("elemento1").region("region1");
    }

    public static Pokemon getPokemonSample2() {
        return new Pokemon().id(2L).nombre("nombre2").tipo("tipo2").elemento("elemento2").region("region2");
    }

    public static Pokemon getPokemonRandomSampleGenerator() {
        return new Pokemon()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .tipo(UUID.randomUUID().toString())
            .elemento(UUID.randomUUID().toString())
            .region(UUID.randomUUID().toString());
    }
}
