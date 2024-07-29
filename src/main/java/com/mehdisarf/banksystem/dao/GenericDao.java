package com.mehdisarf.banksystem.dao;

public interface GenericDao <T, ID> {

    ID create(T t);

    T update(T t);

    T get(ID id);

    void delete(ID id);
}
