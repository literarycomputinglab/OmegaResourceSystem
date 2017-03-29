/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.resourcesystem;

import it.cnr.ilc.lc.omega.resourcesystem.spi.ResourceSystemComponentService;
import java.io.PrintStream;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sirius.kernel.di.std.Part;

/**
 *
 * @author simone
 */
public class Resource extends ResourceSystemComponent {

    private static final Logger log = LogManager.getLogger(ResourceSystemComponent.class);

    @Part
    private static ResourceSystemComponentService resourceService;

    Resource() {

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
        throw new UnsupportedOperationException("You cannot add component to a Resource");
    }

    @Override
    public ResourceSystemComponent add(List<ResourceSystemComponent> components) {
        throw new UnsupportedOperationException("You cannot add components to a Resource");
    }

    @Override
    public ResourceSystemComponent remove(ResourceSystemComponent component) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ResourceSystemComponent> getChildren() {
        throw new UnsupportedOperationException("There is no child. This is a leaf node"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void print(PrintStream p) {
        int pad = getDepth()* 3 + this.getName().length();
        p.printf("%1$" + pad + "s [%s]\n", this.getName(), this.getType());
    }

}
