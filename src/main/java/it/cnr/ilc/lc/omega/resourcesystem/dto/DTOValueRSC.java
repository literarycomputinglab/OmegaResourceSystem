/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.resourcesystem.dto;

/**
 *
 * @author simone
 * @param <T>
 * @param <K>
 */
public interface DTOValueRSC<T> {

    static public <T,K extends DTOValueRSC<T>> K instantiate(Class<K> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }

    abstract T getValue();

    abstract <K extends DTOValueRSC<T>> K withValue(T t);

}
