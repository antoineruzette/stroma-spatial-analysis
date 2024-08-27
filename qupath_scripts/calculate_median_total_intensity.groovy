/**
 * Authors: Antoine A. Ruzette, Simon F. Nørrelykke
 * Date: 2024-03-21
 *
 * This script calculates the total intensity of a marker by 
 * multiplying the area of the object by the median intensity of the marker.
 * The script is designed to be run on detections.
 * 
 * Released under the MIT License (see LICENSE file)
 */

import qupath.lib.objects.PathObject
import static qupath.lib.gui.scripting.QPEx.*
import java.util.Collections

def kerTotalIntensity = new KerCalculator()
def pNDRG1TotalIntensity = new MarkerCalculator()
def fibronectinTotalIntensity = new FibronectinCalculator()
def dapiTotalIntensity = new DAPICalculator()

// Get the objects (here, we use detections)
def pathObjects = getDetectionObjects()

// Add ker measurements
pathObjects.each {
    try (def ml = it.getMeasurementList()) {
        ml.putMeasurement(kerTotalIntensity.getName(), kerTotalIntensity.calculate(it))
    }
}

// Add pNDRG1 measurements
pathObjects.each {
    try (def ml = it.getMeasurementList()) {
        ml.putMeasurement(pNDRG1TotalIntensity.getName(), pNDRG1TotalIntensity.calculate(it))
    }
}

// Add fibronectin measurements
pathObjects.each {
    try (def ml = it.getMeasurementList()) {
        ml.putMeasurement(fibronectinTotalIntensity.getName(), fibronectinTotalIntensity.calculate(it))
    }
}

// Add DAPI measurements
pathObjects.each {
    try (def ml = it.getMeasurementList()) {
        ml.putMeasurement(dapiTotalIntensity.getName(), dapiTotalIntensity.calculate(it))
    }
}

fireHierarchyUpdate()

/**
 * Class to define your new measurement
 */
class KerCalculator {

    /**
     * Returns the name of the new measurement
     * @return The measurement name
     */
    String getName() {
        return "KER_488: Cytoplasm: MedianTotalIntensity"
    }

    /**
     * Calculates the total intensity by multiplying area by mean intensity
     * @param pathObject The PathObject for which to calculate the measurement
     * @return The total intensity value for the specified object
     */
    double calculate(PathObject pathObject) {
        // Get the required measurements
        double cellArea = measurement(pathObject, "Cell: Area µm^2")
        double nucleusArea = measurement(pathObject, "Nucleus: Area µm^2")
        double cytoplasmArea = cellArea - nucleusArea
        double medianCytoIntensity = measurement(pathObject, "KER_488: Cytoplasm: Median")

        // Calculate the total intensity
        double totalCytoIntensity = cytoplasmArea * medianCytoIntensity
        return totalCytoIntensity
    }
}


/**
 * Class to define your new measurement
 */
class MarkerCalculator {

    /**
     * Returns the name of the new measurement
     * @return The measurement name
     */
    String getName() {
        return "pNDRG1_647: Cell: MedianTotalIntensity"
    }

    /**
     * Calculates the total intensity by multiplying area by mean intensity
     * @param pathObject The PathObject for which to calculate the measurement
     * @return The total intensity value for the specified object
     */
    double calculate(PathObject pathObject) {
        // Get the required measurements
        double cellArea = measurement(pathObject, "Cell: Area µm^2")
        double medianCellIntensity = measurement(pathObject, "pNDRG1_647: Cell: Median")

        // Calculate the total intensity
        double totalCellIntensity = cellArea * medianCellIntensity
        return totalCellIntensity
    }
}

/**
 * Class to define your new measurement
 */
class FibronectinCalculator {

    /**
     * Returns the name of the new measurement
     * @return The measurement name
     */
    String getName() {
        return "FN_568: Cell: MedianTotalIntensity"
    }

    /**
     * Calculates the total intensity by multiplying area by mean intensity
     * @param pathObject The PathObject for which to calculate the measurement
     * @return The total intensity value for the specified object
     */
    double calculate(PathObject pathObject) {
        // Get the required measurements
        double cellArea = measurement(pathObject, "Cell: Area µm^2")
        double medianCellIntensity = measurement(pathObject, "FN_568: Cell: Median")

        // Calculate the total intensity
        double totalCellIntensity = cellArea * medianCellIntensity
        return totalCellIntensity
    }
}

/**
 * Class to define your new measurement
 */
class DAPICalculator {

    /**
     * Returns the name of the new measurement
     * @return The measurement name
     */
    String getName() {
        return "DAPI: Nucleus: MedianTotalIntensity"
    }

    /**
     * Calculates the total intensity by multiplying area by mean intensity
     * @param pathObject The PathObject for which to calculate the measurement
     * @return The total intensity value for the specified object
     */
    double calculate(PathObject pathObject) {
        // Get the required measurements
        double cellArea = measurement(pathObject, "Cell: Area µm^2")
        double medianCellIntensity = measurement(pathObject, "DAPI: Nucleus: Median")

        // Calculate the total intensity
        double totalCellIntensity = cellArea * medianCellIntensity
        return totalCellIntensity
    }
}

