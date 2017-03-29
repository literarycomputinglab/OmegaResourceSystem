/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.resourcesystem.dto;

import it.cnr.ilc.lc.omega.resourcesystem.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author simone
 */
public class RSCParent implements DTOValueRSC<Collection> {

    private Collection collection;
    public final static int ROOT = -1;

    public static RSCParent NOPARENT;

    static {
        try {
            NOPARENT = DTOValueRSC.instantiate(RSCParent.class).withValue(null);
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(RSCParent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Collection getValue() {
        return collection;
    }

    @Override
    public <K extends DTOValueRSC<Collection>> K withValue(Collection collection) {

        this.collection = collection;
        return (K) this;
    }

}
