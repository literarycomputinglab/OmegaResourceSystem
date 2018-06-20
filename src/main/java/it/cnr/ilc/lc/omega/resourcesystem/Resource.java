/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.resourcesystem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cnr.ilc.lc.omega.adt.annotation.CatalogItem;
import it.cnr.ilc.lc.omega.annotation.catalog.ResourceSystemAnnotation;
import it.cnr.ilc.lc.omega.annotation.catalog.ResourceSystemAnnotationType;
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.annotation.AnnotationRelationType;
import it.cnr.ilc.lc.omega.core.dto.ADTAnnotationSource;
import it.cnr.ilc.lc.omega.core.dto.ADTAnnotationTarget;
import it.cnr.ilc.lc.omega.core.dto.DTOValueRM;
import it.cnr.ilc.lc.omega.entity.Annotation;
import it.cnr.ilc.lc.omega.entity.AnnotationRelation;
import it.cnr.ilc.lc.omega.entity.Content;
import it.cnr.ilc.lc.omega.exception.VirtualResourceSystemException;
import it.cnr.ilc.lc.omega.resourcesystem.spi.ResourceSystemComponentService;
import java.io.PrintStream;
import java.net.URI;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sirius.kernel.di.std.Part;

/**
 *
 * @author angelo
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

    Resource(Annotation<?, ResourceSystemAnnotation> annotation) {

        super();
        init(annotation);
    }

    private void init(Annotation<?, ResourceSystemAnnotation> annotation) {
        this.annotation = annotation;
        this.setUri(URI.create(annotation.getUri()));

    }

    private void init() {

    }

    public static Resource load(Annotation<?, ResourceSystemAnnotation> annotation) throws ManagerAction.ActionException {

        try {
            if (annotation != null) {
                if (!ResourceSystemAnnotationType.RESOURCE.name().equals(annotation.getData().getType())) {
                    String err = "Incompatible type of loaded annotation: Request " + ResourceSystemAnnotationType.RESOURCE.name()
                            + " Found " + annotation.getData().getType();
                    annotation = null;
                    log.error(err);
                    throw new ManagerAction.ActionException(new Exception(err + annotation.getUri()));
                }
            } else {
                throw new ManagerAction.ActionException(new Exception("Resource System Collection annotation is null with URI " + annotation.getUri()));
            }
        } catch (ManagerAction.ActionException e) {
            throw new ManagerAction.ActionException(new Exception("Error while loading Resource System Collection annotation with URI " + annotation.getUri(), e));
        }

        return new Resource(annotation);

    }

    public static Resource load(URI uri) throws ManagerAction.ActionException {

        Resource resource = null;
        Annotation<?, ResourceSystemAnnotation> annotation = null;

        try {
            annotation = (Annotation<?, ResourceSystemAnnotation>) componentManager.loadAnnotation(uri, Content.class
            );
            if (!ResourceSystemAnnotationType.RESOURCE.name().equals(annotation.getData().getType())) {
                String err = "Incompatible type of loaded annotation: Request " + ResourceSystemAnnotationType.RESOURCE.name()
                        + " Found " + annotation.getData().getType();
                annotation = null;
                log.error(err);
                throw new ManagerAction.ActionException(new Exception(err + uri));
            }
        } catch (ManagerAction.ActionException e) {
            throw new ManagerAction.ActionException(new Exception("Error while loading Resource System Collection annotation with URI " + uri, e));
        }

        if (annotation != null) {
            resource = new Resource(annotation);

        } else {
            throw new ManagerAction.ActionException(new Exception("Resource System Collection annotation is null with URI " + uri));
        }

        return resource;
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
    public void setResourceContent(CatalogItem item) throws VirtualResourceSystemException {

        try {
            ADTAnnotationSource src = DTOValueRM.instantiate(ADTAnnotationSource.class
            ).withValue(this);
            ADTAnnotationTarget trg = DTOValueRM.instantiate(ADTAnnotationTarget.class
            ).withValue(item);
            componentManager.updateAnnotationRelation(src, trg, AnnotationRelationType.HAS_RESOURCE);
        } catch (InstantiationException | IllegalAccessException | ManagerAction.ActionException ex) {
            log.error(ex);
        }
    }

    @Override
    @JsonIgnore
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
        throw new UnsupportedOperationException("You cannot remove a component from a Resource"); 
    }

    @Override
    @JsonIgnore
    public List<ResourceSystemComponent> getChildren() {
        throw new UnsupportedOperationException("There is no child. This is a leaf node"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void print(PrintStream p) {
        int pad = getDepth() * 3 + this.getName().length();
        p.printf("%1$" + pad + "s [%s]\n", this.getName(), this.getType());
    }

    @Override
    public URI getCatalogDescriptionURI() {

        URI uri = null;
        for (AnnotationRelation annRel : annotation.getRelations()) {

            if (annRel.getType().equals(AnnotationRelationType.HAS_RESOURCE.name())) {
                uri = URI.create(annRel.getTargetAnnotation().getUri());
                log.info("Retrieved URI : " + uri);
                return uri;
            }
        }
        log.warn("No description found for resourcesystem idefied by uri: " + this.getUri().toASCIIString());

        return uri;
    }

    @Override
    public boolean isRemovable() {
        log.info("Resource is always removable");
        return true;
    }

    @Override
    public ResourceSystemComponent getChild(URI uri) {
        throw new UnsupportedOperationException("Resource  " + uri.toASCIIString() + " has not children"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResourceSystemComponent getParent(ResourceSystemComponent child) {
        throw new UnsupportedOperationException("You cannot search for parent starting from a Resource (leaf of hierarchy)"); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
