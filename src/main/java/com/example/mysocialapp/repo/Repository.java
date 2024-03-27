package com.example.mysocialapp.repo;



// Java interface that defines the basic operations for a generic repository.
// these operations include functions to: get entities, get all entities, check entities,
// save entities, get repository size, delete entities, and update entities.

import com.example.mysocialapp.domain.Entity;

public interface Repository<ID, E extends Entity<ID>> {

    /**
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    E getOne(ID id);

    /**
     * @return all entities
     */
    Iterable<E> getAll();

    /**
     * @param entity - the entity that mus be validated
     * @throws IllegalArgumentException and ValidationException if the entity isn't a good one
     */
    void verifyEntity(E entity);

    /**
     * @param entity entity must be not null
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     // @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.     *
     */
    E save(E entity);

    /**
     * @return the number of existing entities in this repo
     */
    int size();

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException if the given id is null.
     */
    E delete(ID id);

    /**
     * @param entity entity must not be null
     * @return null - if the entity is updated,
     * otherwise  returns the entity  - (ex. id does not exist).
     * @throws IllegalArgumentException if the given entity is null.
     // @throws ValidationException      if the entity is not valid.
     */
    E update(E entity);

}

