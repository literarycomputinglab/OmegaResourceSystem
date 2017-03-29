/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.resourcesystem;

import it.cnr.ilc.lc.omega.resourcesystem.spi.ResourceSystemComponentService;
import sirius.kernel.di.std.Register;

/**
 *
 * @author simone
 */
@Register(classes = {ResourceSystemComponentService.class})
public class ResourceSystemService implements ResourceSystemComponentService{

    @Override
    public String service(String context) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
