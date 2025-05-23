import java.io.*;

public class QuickTest {
    public static void main(String[] args) {
        System.out.println("=== TESTING ROBUST SPARSE MATRIX LOADING ===");
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        
        // Try multiple possible paths
        String[] possiblePaths = {
            "easy_sample_01_2.txt",                           // In current directory
            "../sample_inputs/easy_sample_01_2.txt",          // Relative path
            "../../sample_inputs/easy_sample_01_2.txt",       // One level deeper
            "sample_inputs/easy_sample_01_2.txt",             // From parent directory
            "./sample_inputs/easy_sample_01_2.txt"            // Current with sample_inputs
        };
        
        String workingPath = null;
        
        // Find which path works
        for (String path : possiblePaths) {
            File file = new File(path);
            System.out.println("Trying: " + path + " -> " + file.getAbsolutePath());
            if (file.exists()) {
                System.out.println("✓ Found file at: " + path);
                workingPath = path;
                break;
            } else {
                System.out.println("✗ Not found");
            }
        }
        
        if (workingPath == null) {
            System.out.println("\n=== FILE NOT FOUND ===");
            System.out.println("Could not find the sample file in any expected location.");
            System.out.println("Please copy easy_sample_01_2.txt to the src directory, or");
            System.out.println("run the program from the correct directory.");
            return;
        }
        
        try {
            System.out.println("\nLoading matrix from: " + workingPath);
            RobustSparseMatrix matrix = new RobustSparseMatrix(workingPath);
            
            System.out.println("\n=== SUCCESS! ===");
            matrix.display();
            
            // Test element access
            System.out.println("Testing specific elements:");
            System.out.println("Element at (0,436): " + matrix.getElement(0, 436));
            
        } catch (Exception e) {
            System.out.println("\n=== ERROR ===");
            System.out.println("Failed to load matrix: " + e.getMessage());
            e.printStackTrace();
        }
    }
}