/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.resourcesystem;

import sirius.kernel.di.std.Register;
import it.cnr.ilc.lc.omega.resourcesystem.spi.VirtualResourceSystemManagerSPI;


/**
 *
 * @author simone
 */
@Register(classes = {VirtualResourceSystemManagerSPI.class})
public final class VirtualResourceSystemManager implements VirtualResourceSystemManagerSPI{
    
}
