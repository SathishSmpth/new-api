package com.kamatchibotique.application.service.impl.common;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class CommonServiceImpl<K extends Serializable & Comparable<K>,E> implements CommonService<K, E> {

    private final Class<E> objectClass;


    private final JpaRepository<E, K> repository;

    @SuppressWarnings("unchecked")
    public CommonServiceImpl(@Lazy JpaRepository<E, K> repository) {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.objectClass = (Class<E>) genericSuperclass.getActualTypeArguments()[1];
        this.repository = repository;
    }

    protected final Class<E> getObjectClass() {
        return objectClass;
    }


    public E getById(K id) throws ServiceException {
        return repository.findById(id).orElseThrow(() -> new ServiceException("Entity not found with id: " + id));
    }


    public void save(E entity) throws ServiceException {
        repository.saveAndFlush(entity);
    }

    public void saveAll(Iterable<E> entities) throws ServiceException {
        repository.saveAll(entities);
    }


    public void create(E entity) throws ServiceException {
        save(entity);
    }



    public void update(E entity) throws ServiceException {
        save(entity);
    }


    public void delete(E entity) throws ServiceException {
        repository.delete(entity);
    }


    public void flush() {
        repository.flush();
    }



    public List<E> list() {
        return repository.findAll();
    }


    public Long count() {
        return repository.count();
    }

    protected E saveAndFlush(E entity) {
        return repository.saveAndFlush(entity);
    }
}


