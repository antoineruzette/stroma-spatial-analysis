import qupath.lib.gui.dialogs.Dialogs
import qupath.lib.gui.measure.ObservableMeasurementTableData;
import qupath.lib.scripting.QP
import qupath.ext.stardist.StarDist2D
import java.nio.file.Files
import java.nio.file.Paths
import groovy.time.*

def avgNucleusRadius = 2.5
def minNucleusArea = (Math.PI * (avgNucleusRadius)**2) / 2
    
// remove impossible nuclei because they are too small
def toDelete = getDetectionObjects().findAll {measurement(it, 'Nucleus: Area µm^2') <= minNucleusArea}
println("Number of cells deleted: ${toDelete.size()}")
removeObjects(toDelete, true)