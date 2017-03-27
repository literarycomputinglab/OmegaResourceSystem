/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.resourcesystem.spi;

import it.cnr.ilc.lc.omega.resourcesystem.VirtualResourceSystemManager;
import sirius.kernel.di.std.Register;

/**
 *
 * @author simone
 */

@Register(classes = {VirtualResourceSystemManager.class})
public class VirtualResourceSystemManagerSPI implements VirtualResourceSystemManager {
    
}
