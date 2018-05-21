/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.resourcesystem.dto;

/**
 *
 * @author simone
 */
public class RSCDescription implements DTOValueRSC<String>{

    private String description; 
    
    @Override
    public String getValue() {
       return description;
    }

    @Override
    public <K extends DTOValueRSC<String>> K withValue(String description) {
        this.description = description;
        return (K) this;
    }

    RSCDescription() {
    }

    RSCDescription(String description) {
        this.description = description;
    }
    
    
    
}
