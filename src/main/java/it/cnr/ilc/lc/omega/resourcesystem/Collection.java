/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.resourcesystem;

import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.annotation.AnnotationRelationType;
import it.cnr.ilc.lc.omega.exception.VirtualResourceSystemException;
import it.cnr.ilc.lc.omega.resourcesystem.spi.ResourceSystemComponentService;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sirius.kernel.di.std.Part;

/**
 *
 * @author simone
 */
public class Collection extends ResourceSystemComponent {

    private static final Logger log = LogManager.getLogger(Collection.class);

    @Part
    private static ResourceSystemComponentService resourceService;

    List<ResourceSystemComponent> components = new ArrayList<>();

    Collection() {
        super();
        init();
    }

    private void init() {

    }

    @Override
    public String getName() {
        return this.annotation.getData().getName();
    }

    @Override
    public String getDescription() {
        return this.annotation.getData().getDescription();
    }

    @Override
    public String getType() {
        return this.annotation.getData().getType();
    }

    @Override
    public VirtualResourceSystemAttribute getAttribute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResourceSystemComponent add(ResourceSystemComponent component) {

        if (component != null) {
            try {
                componentManager.updateAnnotationRelation(this, component, AnnotationRelationType.HAS_CHILD);
                components.add(component);
            } catch (ManagerAction.ActionException ex) {
                log.error(ex);
            }

        }
        return this;
    }

    @Override
    public ResourceSystemComponent add(List<ResourceSystemComponent> components) throws VirtualResourceSystemException {

        int initialLenght = this.components.size();
        if (components != null) {
            for (ResourceSystemComponent c : components) {
                this.add(c);
            }
        }
        int finalLenght = this.components.size();
        if ((finalLenght - initialLenght) != components.size()) {
            throw new VirtualResourceSystemException("Problem storing a new resource system component: expected to add  "
                    + components.size() + ", but only " + (finalLenght - initialLenght) + " were added");
        }
        return this;
    }

    @Override
    public ResourceSystemComponent remove(ResourceSystemComponent component) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ResourceSystemComponent> getChildren() {
        return this.components;
    }

    @Override
    public void print(PrintStream p) {
        int pad = getDepth() * 3 + this.getName().length();
        p.printf("%1$" + pad + "s [%s]\n", this.getName(), this.getType());
        for (ResourceSystemComponent component : components) {
            component.print(p);
        }
    }

}
