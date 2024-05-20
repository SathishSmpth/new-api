package com.kamatchibotique.application.service.services.common;

import com.kamatchibotique.application.exception.ServiceException;

import java.io.Serializable;
import java.util.List;

public interface CommonService<K extends Serializable & Comparable<K>,E> {
    void save(E entity) throws ServiceException;

    void saveAll(Iterable<E> entities) throws ServiceException;

    void update(E entity) throws ServiceException;

    void create(E entity) throws ServiceException;

    void delete(E entity) throws ServiceException;

    E getById(K id) throws ServiceException;

    List<E> list();

    Long count();

    void flush();
}
