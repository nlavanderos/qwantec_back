package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Pokemon.
 */
@Entity
@Table(name = "pokemon")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pokemon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 25)
    @Column(name = "nombre", length = 25, nullable = false)
    private String nombre;

    @NotNull
    @Size(max = 15)
    @Column(name = "tipo", length = 15, nullable = false)
    private String tipo;

    @NotNull
    @Size(max = 15)
    @Column(name = "elemento", length = 15, nullable = false)
    private String elemento;

    @NotNull
    @Size(max = 25)
    @Column(name = "region", length = 25, nullable = false)
    private String region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "pokemon" }, allowSetters = true)
    private Pokedex pokemon;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pokemon id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Pokemon nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Pokemon tipo(String tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getElemento() {
        return this.elemento;
    }

    public Pokemon elemento(String elemento) {
        this.setElemento(elemento);
        return this;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    public String getRegion() {
        return this.region;
    }

    public Pokemon region(String region) {
        this.setRegion(region);
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Pokedex getPokemon() {
        return this.pokemon;
    }

    public void setPokemon(Pokedex pokedex) {
        this.pokemon = pokedex;
    }

    public Pokemon pokemon(Pokedex pokedex) {
        this.setPokemon(pokedex);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pokemon)) {
            return false;
        }
        return getId() != null && getId().equals(((Pokemon) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pokemon{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", elemento='" + getElemento() + "'" +
            ", region='" + getRegion() + "'" +
            "}";
    }
}
