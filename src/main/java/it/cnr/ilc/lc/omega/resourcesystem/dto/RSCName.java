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
public class RSCName implements DTOValueRSC<String>{

    private String name;
    
    @Override
    public String getValue() {
        return name;
    }

    @Override
    public <K extends DTOValueRSC<String>> K withValue(String name) {
        this.name = name;
        return (K) this;
    }
    
    
}
