package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pokedex;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pokedex entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PokedexRepository extends JpaRepository<Pokedex, Long> {}
