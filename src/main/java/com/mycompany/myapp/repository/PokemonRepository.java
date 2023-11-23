package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pokemon;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pokemon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {}
