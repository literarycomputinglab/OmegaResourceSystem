/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.resourcesystem.dto;

import it.cnr.ilc.lc.omega.annotation.catalog.ResourceSystemAnnotationType;

/**
 *
 * @author simone
 */
public class RSCType implements DTOValueRSC<ResourceSystemAnnotationType>{
    
    private ResourceSystemAnnotationType type;

    @Override
    public ResourceSystemAnnotationType getValue() {
        return type;
    }

    @Override
    public <K extends DTOValueRSC<ResourceSystemAnnotationType>> K withValue(ResourceSystemAnnotationType type) {
        this.type = type;
        return (K) this;
    }
    
    
    
}
