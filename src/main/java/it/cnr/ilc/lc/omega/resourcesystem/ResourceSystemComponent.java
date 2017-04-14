/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.resourcesystem;

import it.cnr.ilc.lc.omega.adt.annotation.CatalogItem;
import it.cnr.ilc.lc.omega.annotation.catalog.ResourceSystemAnnotation;
import it.cnr.ilc.lc.omega.annotation.catalog.ResourceSystemAnnotationBuilder;
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.ResourceManager;
import it.cnr.ilc.lc.omega.core.datatype.ADTAbstractAnnotation;
import it.cnr.ilc.lc.omega.entity.Annotation;
import it.cnr.ilc.lc.omega.exception.VirtualResourceSystemException;
import it.cnr.ilc.lc.omega.resourcesystem.dto.RSCDescription;
import it.cnr.ilc.lc.omega.resourcesystem.dto.RSCName;
import it.cnr.ilc.lc.omega.resourcesystem.dto.RSCParent;
import it.cnr.ilc.lc.omega.resourcesystem.dto.RSCType;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sirius.kernel.di.std.Part;

/**
 *
 * @author angelo
 * @author simone
 */
public abstract class ResourceSystemComponent extends ADTAbstractAnnotation {

    private static final Logger log = LogManager.getLogger(ResourceSystemComponent.class);

    @Part
    protected static ResourceManager componentManager;

    private URI uri;

    protected Annotation<?, ResourceSystemAnnotation> annotation; // metterlo qui oppure metterlo per ciascun oggetto concreto?

    protected URI getUri() {
        return uri;
    }

    protected void setUri(URI uri) {
        this.uri = uri;
    }

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
    } // gestire anche il recupero di un componente passando un URI

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

    public void setResourceContent(CatalogItem item) throws VirtualResourceSystemException {

        throw new VirtualResourceSystemException("Attempting to set a resource content by a not implemented method");
    }

    public Integer getDepth() {

        return this.annotation.getData().getDepth();
    }

    @Override
    protected Annotation<?, ResourceSystemAnnotation> getAnnotation() {
        return annotation;
    }

    public static <T extends ResourceSystemComponent> T of(Class<T> clazz, URI uri) {

        T t = null;
        try {
            t = clazz.newInstance();
            t.setUri(uri);
        } catch (InstantiationException | IllegalAccessException ex) {
            log.error(ex);
        }

        return t;
    }

    public static <T extends ResourceSystemComponent> T load(Class<T> clazz, URI uri) {

        log.info("loading resource identified by URI: " + uri.toASCIIString());
        T t = null;
        try {
            Method load = clazz.getDeclaredMethod("load", URI.class);
            t = (T) load.invoke(null, uri);
            log.info("loaded resource identified by URI: " + t.getName());

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            log.error(ex);
        }
        
        t.print(System.out);
        return t;
    }

    public <T extends ResourceSystemComponent> T withParams(RSCName name,
            RSCDescription description, RSCType type, RSCParent parent) throws ManagerAction.ActionException {

        if (null == annotation) {
            ResourceSystemAnnotationBuilder rsab = new ResourceSystemAnnotationBuilder()
                    .URI(getUri())
                    .name(name.getValue())
                    .description(description.getValue())
                    .depth(((parent.getValue() == null) ? RSCParent.ROOT : parent.getValue().getDepth()) + 1)
                    .type(type.getValue());

            log.info("rsab=(" + rsab.toString() + ")");
            annotation = componentManager.createAnnotation(ResourceSystemAnnotation.class, rsab);

        }

        return (T) this;
    }

    public void save() throws ManagerAction.ActionException {
        // controllare che annotation non sia null
        componentManager.saveAnnotation(annotation);
    }

    public abstract VirtualResourceSystemAttribute getAttribute();

    public abstract ResourceSystemComponent add(ResourceSystemComponent component);

    public abstract ResourceSystemComponent add(List<ResourceSystemComponent> components) throws VirtualResourceSystemException;

    public abstract ResourceSystemComponent remove(ResourceSystemComponent component);

    public abstract List<ResourceSystemComponent> getChildren();

    public abstract void print(PrintStream p);
}
