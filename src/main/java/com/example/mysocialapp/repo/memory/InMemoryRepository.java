package com.example.mysocialapp.repo.memory;



import com.example.mysocialapp.domain.Entity;
import com.example.mysocialapp.domain.validators.Validator;
import com.example.mysocialapp.repo.Repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    private final Validator<E> validator;
    protected Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public E getOne(ID id) {
        if(id == null){
//            System.out.println("vezi ca id e null");
            throw new IllegalArgumentException("ID is null!");
        }
//        System.out.println("id nu e null");
        return entities.get(id);
    }

    @Override
    public Iterable<E> getAll() {
        return entities.values();
    }

    @Override
    public void verifyEntity(E entity){
        if(entity == null){
            throw new IllegalArgumentException("Entity is null!");
        }
        validator.validate(entity);
        if(entities.get(entity.getId()) != null){
            throw new IllegalArgumentException("ID must be unique!");
        }
    }

    @Override
    public E save(E entity){
        verifyEntity(entity);
        entities.put(entity.getId(), entity);
        System.out.println(entity.getId());
        return null;
    }

    @Override
    public int size() {
        return entities.size();
    }

    @Override
    public E delete(ID id) {
        E e_id = this.entities.get(id);
        if(e_id == null){
            throw new IllegalArgumentException("Entity does not exist!");
        }
        else{
            this.entities.remove(id);
            return e_id;
        }
    }

    @Override
    public E update(E entity){
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);
        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return entity;
        }
        return null;
    }
}
