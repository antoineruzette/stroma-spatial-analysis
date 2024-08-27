/**
 * Authors: Antoine A. Ruzette, Simon F. Nørrelykke
 * Date: 2024-03-03
 *
 * This scripts classifies cells based on intensity thresholds of two markers.
 * The script is designed to be run on detections.
 * 
 * Released under the MIT License (see LICENSE file)
 */

import qupath.lib.objects.PathObject
import static qupath.lib.gui.scripting.QPEx.*
import java.nio.file.Files
import java.nio.file.Paths

// Iterate over threshold combinations
def ker_thresholds = (0..2000000).step(100000).collect()
def marker_thresholds = (0..500000).step(25000).collect()

// def ker_thresholds = [200000]
// def marker_thresholds = [150000]

// Get the objects (here, we use detections - change if required)
def pathObjects = getDetectionObjects()

// Loop over each combination of thresholds
ker_thresholds.each { ker_threshold ->
    marker_thresholds.each { marker_threshold ->
        println('Starting cell sub-classification...')
        println('Resetting detections class...')
        resetDetectionClassifications()
        println('Parameters: ')
        print("Keratin threshold: ${ker_threshold}; Marker threshold: ${marker_threshold}")

        // Define cell classifier JSON string with updated thresholds
        def cellClassifierString = """
        {
          "object_classifier_type": "CompositeClassifier",
          "classifiers": [
            {
              "object_classifier_type": "SimpleClassifier",
              "function": {
                "classifier_fun": "ClassifyByMeasurementFunction",
                "measurement": "KER_488: Cytoplasm: MedianTotalIntensity",
                "pathClassEquals": "KER_488",
                "pathClassAbove": "KER_488",
                "threshold": ${ker_threshold}
              },
              "pathClasses": [
                "KER_488"
              ],
              "filter": "CELLS",
              "timestamp": 1694426186386
            },
            {
              "object_classifier_type": "SimpleClassifier",
              "function": {
                "classifier_fun": "ClassifyByMeasurementFunction",
                "measurement": "pNDRG1_647: Cell: MedianTotalIntensity",
                "pathClassEquals": "pNDRG1_647",
                "pathClassAbove": "pNDRG1_647",
                "threshold": ${marker_threshold}
              },
              "pathClasses": [
                "pNDRG1_647"
              ],
              "filter": "CELLS",
              "timestamp": 1694426219269
            }
          ]
        }
        """

        // Define the file path to save the JSON, including the value of thresholds
        def cellClassifierFilePath = "/Users/antoine/Harvard/IAC/NinaKozlova/20240111_AsPC_pNDRG1/Results/models/cell_classification/cell_classifier_ker-thresh${ker_threshold}_marker-thresh${marker_threshold}.json"

        // Save the JSON string to a JSON file
        def cellClassifierFile = new File(cellClassifierFilePath)
        cellClassifierFile.text = cellClassifierString

        // Execute your classifier with the current thresholds
        runObjectClassifier(cellClassifierFilePath)

        /**
        // Remove outliers for KER_488: pNDRG1_647 class
        def kerTotalIntensities = pathObjects.collect { measurement(it, "KER_488: Cytoplasm: MedianTotalIntensity") }
        // Sort the total intensities
        Collections.sort(kerTotalIntensities)
        // Calculate the 5th and 95th percentiles
        def kerIndex5th = Math.round(kerTotalIntensities.size() * 0.01).toInteger()
        def kerIndex95th = Math.round(kerTotalIntensities.size() * 0.99).toInteger()
        def kerPercentile5th = kerTotalIntensities[kerIndex5th]
        def kerPercentile95th = kerTotalIntensities[kerIndex95th]

        // Set new class to mark for outliers
        def kerNewPathClass = getPathClass("KER_488Outliers")
        pathObjects.forEach {
            def intensity = measurement(it, "KER_488: Cytoplasm: MedianTotalIntensity")
            if (intensity < kerPercentile5th) {
                // Below 5th percentile, update pathClass to "Below_5th_Percentile"
                it.setPathClass(kerNewPathClass)
            } else if (intensity > kerPercentile95th) {
                // Above 95th percentile, update pathClass to "Above_95th_Percentile"
                it.setPathClass(kerNewPathClass)
            }
        }

        // Remove outliers for pNDRG1_647 Class
        def pNDRG1TotalIntensities = pathObjects.collect { measurement(it, "pNDRG1_647: Cell: MedianTotalIntensity") }
        // Sort the total intensities
        Collections.sort(pNDRG1TotalIntensities)
        // Calculate the 5th and 95th percentiles
        def pNDRG1Index5th = Math.round(pNDRG1TotalIntensities.size() * 0.01).toInteger()
        def pNDRG1Index95th = Math.round(pNDRG1TotalIntensities.size() * 0.99).toInteger()
        def pNDRG1Percentile5th = pNDRG1TotalIntensities[pNDRG1Index5th]
        def pNDRG1Percentile95th = pNDRG1TotalIntensities[pNDRG1Index95th]

        // Set new class to mark for outliers
        def pNDRG1NewPathClass = getPathClass("pNDRG1_647Outliers")
        pathObjects.forEach {
            def intensity = measurement(it, "pNDRG1_647: Cell: MedianTotalIntensity")
            if (intensity < pNDRG1Percentile5th) {
                // Below 5th percentile, update pathClass to "Below_5th_Percentile"
                it.setPathClass(pNDRG1NewPathClass)
            } else if (intensity > pNDRG1Percentile95th) {
                // Above 95th percentile, update pathClass to "Above_95th_Percentile"
                it.setPathClass(pNDRG1NewPathClass)
            }
        }
        **/

        // Update hierarchy
        fireHierarchyUpdate()
        
        try {
            // Calculate Class Ratio
            selectObjectsByClassification("KER_488: pNDRG1_647")
            pNDRG1KerObjects = getSelectedObjects()
            pNDRG1KerObjectsNumber = pNDRG1KerObjects.size()
            println pNDRG1KerObjectsNumber

            selectObjectsByClassification("KER_488")
            kerObjects = getSelectedObjects()
            kerObjectsNumber = kerObjects.size()
            println kerObjectsNumber

            classRatio = pNDRG1KerObjectsNumber / kerObjectsNumber
        } catch (Exception e) {
            println "An error occurred while calculating class ratio: ${e.message}"
            classRatio = 0
        }
        
        println "Class Ratio (ker+pNDRG1+/ker+pNDRG1-): ${classRatio}"

        // Export Class Ratio and corresponding parameters
        def folder_path = "/Users/antoine/Harvard/IAC/NinaKozlova/20240111_AsPC_pNDRG1/Results/class_ratio_data"
        def imageName = getProjectEntry().getImageName()

        def path = buildFilePath(folder_path, "${imageName}" + ".csv")
        def measurements = ["ClassRatio", "KER_488", "pNDRG1_647"]

        // Check if the file exists, if not, create a new file with a header
        if (!Files.exists(Paths.get(path))) {
            try (def writer = new PrintWriter(path)) {
                def sb = new StringBuilder()
                sb.append("ClassRatio, KER_488, pNDRG1_647")
                writer.println(sb.toString())
            }
        }

        // Append data to the CSV file
        try (def writer = new PrintWriter(new FileOutputStream(new File(path), true))) {
            def sb = new StringBuilder()
            sb.append(classRatio)
            sb.append(',')
            sb.append(ker_threshold)
            sb.append(',')
            sb.append(marker_threshold)
            writer.println(sb.toString())
        } catch (Exception e) {
            println "An error occurred while writing to the CSV file: ${e.message}"
        }
        println('ClassRatio and corresponding parameters exported to csv!')
        println('All done!')
    }
}
