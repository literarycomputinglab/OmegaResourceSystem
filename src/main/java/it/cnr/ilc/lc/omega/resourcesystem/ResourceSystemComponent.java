/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.resourcesystem;

import it.cnr.ilc.lc.omega.exception.VirtualResourceSystemException;
import java.util.Iterator;
import java.util.List;
import sirius.kernel.di.std.Part;

/**
 *
 * @author simone
 */
public abstract class ResourceSystemComponent {

    @Part
    private static ResourceSystemComponentManager componentManager;

    public Boolean isResource() {

        return Resource.class.isInstance(this);
    }

    public Boolean isCollection() {

        return Collection.class.isInstance(this);
    }

    public Iterator<ResourceSystemComponent> getIterator() {

        return getChildren().iterator(); //le Recource (foglie) hanno una lista vuota che memorizza i figli (che non ha)
    }

    public ResourceSystemComponent getComponent(int index) throws VirtualResourceSystemException {
        if (isResource()) {
            throw new VirtualResourceSystemException("The component you trying to access is a Resource (leaf)");
        } else {
            return getChildren().get(index);
        }
    }

    public <T extends ResourceSystemComponent> T getCurrentComponent(Class<T> clazz) throws VirtualResourceSystemException {
        if (this.getClass().equals(clazz)) {
            return (T) this;
        } else {
            throw new VirtualResourceSystemException("You cannot cast " + clazz.getTypeName() + " to " + this.getClass().getTypeName());
        }
    }

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getType();

    public static ResourceSystemComponent createResource() {
        return null;
    }

    public static ResourceSystemComponent createCollection() {
        return null;
    }

    public abstract VirtualResourceSystemAttribute getAttribute();

    public abstract ResourceSystemComponent add(ResourceSystemComponent component);

    public abstract ResourceSystemComponent remove(ResourceSystemComponent component);

    public abstract List<ResourceSystemComponent> getChildren();

}
