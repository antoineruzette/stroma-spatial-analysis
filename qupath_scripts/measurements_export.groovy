import qupath.lib.objects.PathObject
import static qupath.lib.gui.scripting.QPEx.*
import java.nio.file.Files
import java.nio.file.Paths
import qupath.lib.gui.measure.ObservableMeasurementTableData;

// ---- EXPORT MEASUREMENTS

println('Starting measurements export...')

def imageName = getProjectEntry().getImageName()
def folder_path = "/Users/antoine/Harvard/IAC/NinaKozlova/20240111_AsPC_pNDRG1/Results/20240218_spatial_data/20240301_data"
// Check if the folder exists, if not, create it
def folder = new File(folder_path)
if (!folder.exists()) {
    folder.mkdirs()
}

def path = buildFilePath(folder_path, "${imageName}" + '.csv')

//Get cells and define measurements to export
def cells = getCellObjects()

def ob = new ObservableMeasurementTableData();
def measurements = ob.getMeasurementNames()

ob.setImageData(getCurrentImageData(), cells);

// Check if the file exists, if not, create a new file with a header
if (!Files.exists(Paths.get(path))) {
    try (def writer = new PrintWriter(path)) {
        def sb = new StringBuilder()
        sb.append('Image,Class')
        for (def measurementName in measurements) {
            sb.append(',')
            sb.append(measurementName)
        }
        writer.println(sb.toString())
    }
}

try (def writer = new FileWriter(path, true)) {

    // Append measurements to the existing file, including the image name
    for (def cell in cells) {
        def sb = new StringBuilder()
        sb.append(imageName) // Add the image name
        sb.append(',')
        sb.append(cell.getPathClass())
        for (def measurementName in measurements) {
            sb.append(',')
            if (ob.isNumericMeasurement(measurementName)) {
                sb.append(ob.getNumericValue(cell, measurementName))
            }
            if (ob.isStringMeasurement(measurementName)) {
                sb.append(ob.getStringValue(cell, measurementName))
            }
        }
        writer.append(sb.toString() + "\n")
    }
} catch (IOException e) {
    // Handle IOException (e.g., log the error)
    e.printStackTrace()
}

println "Done!"
println "Appended to $path"
