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
public class RSCType implements DTOValueRSC<String>{
    
    private String type;

    @Override
    public String getValue() {
        return type;
    }

    @Override
    public <K extends DTOValueRSC<String>> K withValue(String type) {
        this.type = type;
        return (K) this;
    }
    
    
    
}
