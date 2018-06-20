/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.resourcesystem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cnr.ilc.lc.omega.annotation.catalog.ResourceSystemAnnotation;
import it.cnr.ilc.lc.omega.annotation.catalog.ResourceSystemAnnotationType;
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.annotation.AnnotationRelationType;
import it.cnr.ilc.lc.omega.entity.Annotation;
import it.cnr.ilc.lc.omega.entity.AnnotationRelation;
import it.cnr.ilc.lc.omega.entity.Content;
import it.cnr.ilc.lc.omega.exception.VirtualResourceSystemException;
import it.cnr.ilc.lc.omega.resourcesystem.spi.ResourceSystemComponentService;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sirius.kernel.di.std.Part;

/**
 *
 * @author angelo
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

    Collection(Annotation<?, ResourceSystemAnnotation> annotation) throws VirtualResourceSystemException {
        super();
        init(annotation);
        initComponents();
    }

    private void init(Annotation<?, ResourceSystemAnnotation> annotation) throws VirtualResourceSystemException {
        this.annotation = annotation;
        this.setUri(URI.create(annotation.getUri()));
    }

    private void init() {

    }

    public static Collection load(URI uri) throws ManagerAction.ActionException, VirtualResourceSystemException {

        Collection collection = null;
        Annotation<?, ResourceSystemAnnotation> annotation = null;
        try {
            annotation = (Annotation<?, ResourceSystemAnnotation>) componentManager.loadAnnotation(uri, Content.class);
            if (annotation != null) {
                if (!ResourceSystemAnnotationType.COLLECTION.name().equals(annotation.getData().getType())) {
                    String err = "Incompatible type of loaded annotation: Request " + ResourceSystemAnnotationType.COLLECTION.name()
                            + " Found " + annotation.getData().getType();
                    annotation = null;
                    log.error(err);

                    throw new VirtualResourceSystemException(new Exception(err + uri));

                } else {
                    collection = new Collection(annotation);
                }
            } else {
                throw new ManagerAction.ActionException(new Exception("Resource System Collection annotation is null with URI " + uri));

            }
        } catch (ManagerAction.ActionException e) {
            throw new ManagerAction.ActionException(new Exception("Error while loading Resource System Collection annotation with URI " + uri, e));
        }

        return collection;
    }

    public static Collection load(Annotation<?, ResourceSystemAnnotation> annotation) throws ManagerAction.ActionException, VirtualResourceSystemException {
        Collection collection = null;
        try {
            if (annotation != null) {
                if (!ResourceSystemAnnotationType.COLLECTION.name().equals(annotation.getData().getType())) {
                    String err = "Incompatible type of loaded annotation: Request " + ResourceSystemAnnotationType.COLLECTION.name()
                            + " Found " + annotation.getData().getType();
                    annotation = null;
                    log.error(err);

                    throw new VirtualResourceSystemException(new Exception(err + annotation.getUri()));

                } else {
                    collection = new Collection(annotation);
                }
            } else {
                throw new ManagerAction.ActionException(new Exception("Resource System Collection annotation is null with URI " + annotation.getUri()));

            }
        } catch (ManagerAction.ActionException e) {
            throw new ManagerAction.ActionException(new Exception("Error while loading Resource System Collection annotation with URI " + annotation.getUri(), e));
        }

        return collection;
    }

    private void initComponents() throws VirtualResourceSystemException {

        for (AnnotationRelation annRel : annotation.getRelations()) {

            if (annRel.getType().equals(AnnotationRelationType.HAS_CHILD.name())) {
                Annotation<? extends Content, ResourceSystemAnnotation> target = (Annotation<?, ResourceSystemAnnotation>) annRel.getTargetAnnotation();
                if (ResourceSystemAnnotationType.COLLECTION.name().equals(target.getData().getType())) { //FIXME contruire un ENUM per il type (item, folder)
                    components.add(ResourceSystemComponent.load(Collection.class, target));
                } else if (ResourceSystemAnnotationType.RESOURCE.name().equals(target.getData().getType())) { //FIXME contruire un ENUM per il type (item, folder)
                    components.add(ResourceSystemComponent.load(Resource.class, target));
                } else {
                    throw new VirtualResourceSystemException("Error while initialize the Resource System Component");
                }
            }

        }
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
    @JsonIgnore
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
        ResourceSystemComponent toBeRemoved = null;
        try {
            toBeRemoved = this.getChild(component.getUri());
        } catch (IllegalArgumentException e) {
            log.warn(e);
        }
        if (null != toBeRemoved) {
            log.info("Found component uri=("+ toBeRemoved.getUri() +") to be removed ");
            if (toBeRemoved.equals(component)) {
                try {
                    if (null == ResourceSystemComponent.delete(toBeRemoved)) {
                        if (this.getParent(toBeRemoved).getChildren().remove(toBeRemoved)) {
                            log.info("Component uri=(" + toBeRemoved.getUri() + ") removed");
                            component = null;
                            toBeRemoved = null;
                            return null;
                        } else {
                            throw new IllegalArgumentException("component not found in collection");
                        }
                    } else {
                        log.info("Component uri=(" + toBeRemoved.getUri() + ") cannot be removed");
                    }
                } catch (ManagerAction.ActionException ex) {
                    log.error("On remove component", ex);
                }
                return toBeRemoved;
            } else {
                component = toBeRemoved;
                for (ResourceSystemComponent comp : this.components) {
                    try {
                        return comp.remove(component);
                    } catch (UnsupportedOperationException | IllegalArgumentException e) {
                        log.info(e.getLocalizedMessage());
                    }
                }
            }

        } else {
            log.warn("Component uri=(" + component.getUri() + ") not found or you are trying to the delete the root of the hierarchy");
        }

        throw new IllegalArgumentException(
                "Component uri=(" + component.getUri() + ") not found or you are trying to the delete the root of the hierarchy");
    }

    @Override
    public List<ResourceSystemComponent> getChildren() {
        return this.components;
    }

    @Override
    public void print(PrintStream p
    ) {
        int pad = getDepth() * 3 + this.getName().length();
        p.printf("%1$" + pad + "s [%s]\n", this.getName(), this.getType());
        for (ResourceSystemComponent component : components) {
            component.print(p);
        }
    }

    @Override
    public URI getCatalogDescriptionURI() {

        return URI.create("");
    }

    @Override
    public boolean isRemovable() {
        log.info("Collection uri=(" + getUri() + ") is removable? " + this.getAnnotation().isEmptyRelation());
        return this.getAnnotation().isEmptyRelation();
    }

    @Override
    public ResourceSystemComponent getChild(URI uri) {
        if (null != uri) {
            for (ResourceSystemComponent component : this.components) {
                if (uri.equals(URI.create(component.getAnnotation().getUri()))) {
                    return component;
                } else {
                    try {
                        return component.getChild(uri);
                    } catch (UnsupportedOperationException | IllegalArgumentException e) {
                        log.warn("" + e.getLocalizedMessage());
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Uri cannot be null!");
        }

        throw new IllegalArgumentException(
                "Component with uri=(" + uri + ") not found");
    }

    @Override
    public ResourceSystemComponent getParent(ResourceSystemComponent child) {
        if (null != child) {
            if (this.components.contains(child)) {
                return this;
            } else {
                try {
                    for (ResourceSystemComponent component : components) {
                        return component.getParent(child);
                    }

                } catch (UnsupportedOperationException | IllegalArgumentException e) {
                    log.warn(e.getLocalizedMessage());
                }
            }
        } else {
            throw new IllegalArgumentException("Uri cannot be null!");
        }

        throw new IllegalArgumentException(
                "Component with uri=(" + child.getUri() + ") not found");
    }

}
