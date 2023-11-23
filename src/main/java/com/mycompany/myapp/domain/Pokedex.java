package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Pokedex.
 */
@Entity
@Table(name = "pokedex")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pokedex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pokemon")
    @JsonIgnoreProperties(value = { "pokemon" }, allowSetters = true)
    private Set<Pokemon> pokemon = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pokedex id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Pokemon> getPokemon() {
        return this.pokemon;
    }

    public void setPokemon(Set<Pokemon> pokemon) {
        if (this.pokemon != null) {
            this.pokemon.forEach(i -> i.setPokemon(null));
        }
        if (pokemon != null) {
            pokemon.forEach(i -> i.setPokemon(this));
        }
        this.pokemon = pokemon;
    }

    public Pokedex pokemon(Set<Pokemon> pokemon) {
        this.setPokemon(pokemon);
        return this;
    }

    public Pokedex addPokemon(Pokemon pokemon) {
        this.pokemon.add(pokemon);
        pokemon.setPokemon(this);
        return this;
    }

    public Pokedex removePokemon(Pokemon pokemon) {
        this.pokemon.remove(pokemon);
        pokemon.setPokemon(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pokedex)) {
            return false;
        }
        return getId() != null && getId().equals(((Pokedex) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pokedex{" +
            "id=" + getId() +
            "}";
    }
}
