import java.io.*;
import java.util.Scanner;

public class MatrixMain {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("==============================================");
        System.out.println("    SPARSE MATRIX OPERATIONS CALCULATOR");
        System.out.println("==============================================");
        System.out.println();
        
        while (true) {
            System.out.println("Available Operations:");
            System.out.println("1. Matrix Addition (A + B)");
            System.out.println("2. Matrix Subtraction (A - B)");
            System.out.println("3. Matrix Multiplication (A × B)");
            System.out.println("4. Exit Program");
            System.out.println();
            
            System.out.print("Enter your choice (1-4): ");
            String input = scanner.nextLine().trim();
            
            if (input.equals("4")) {
                System.out.println("Thank you for using the calculator!");
                break;
            }
            
            if (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
                System.out.println("Invalid choice! Please enter 1, 2, 3, or 4.\n");
                continue;
            }
            
            int operation = Integer.parseInt(input);
            String operationName = getOperationName(operation);
            
            System.out.println("\nYou selected: " + operationName);
            System.out.println("Enter file paths (you can use just filenames if files are in sample_inputs folder):");
            System.out.println();
            
            System.out.print("Enter path for first matrix file: ");
            String file1 = scanner.nextLine().trim();
            
            System.out.print("Enter path for second matrix file: ");
            String file2 = scanner.nextLine().trim();
            
            // Try to find the files
            file1 = findFile(file1);
            file2 = findFile(file2);
            
            // Perform operation
            performOperation(operation, file1, file2);
            
            System.out.println("\n" + "=".repeat(50));
            System.out.print("Do you want to perform another operation? (y/n): ");
            String continueChoice = scanner.nextLine().trim().toLowerCase();
            
            if (!continueChoice.equals("y") && !continueChoice.equals("yes")) {
                System.out.println("Thank you for using the calculator!");
                break;
            }
            System.out.println();
        }
        
        scanner.close();
    }
    
    private static String getOperationName(int operation) {
        switch (operation) {
            case 1: return "Addition";
            case 2: return "Subtraction";
            case 3: return "Multiplication";
            default: return "Unknown";
        }
    }
    
    private static String getOperationSymbol(int operation) {
        switch (operation) {
            case 1: return "add";
            case 2: return "sub";
            case 3: return "mul";
            default: return "unknown";
        }
    }
    
    private static String findFile(String filename) {
        // Try original path first
        File file = new File(filename);
        if (file.exists()) {
            return filename;
        }
        
        // Try the path that worked in QuickTest
        String samplePath = "../../sample_inputs/" + filename;
        File sampleFile = new File(samplePath);
        if (sampleFile.exists()) {
            System.out.println("Found: " + samplePath);
            return samplePath;
        }
        
        // Also try the other possible path
        String altPath = "../sample_inputs/" + filename;
        File altFile = new File(altPath);
        if (altFile.exists()) {
            System.out.println("Found: " + altPath);
            return altPath;
        }
        
        // Return original if nothing found (will show error later)
        System.out.println("Could not find file: " + filename);
        System.out.println("Tried paths:");
        System.out.println("  " + file.getAbsolutePath());
        System.out.println("  " + sampleFile.getAbsolutePath());
        System.out.println("  " + altFile.getAbsolutePath());
        return filename;
    }
    
    private static void performOperation(int operation, String file1, String file2) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("PROCESSING " + getOperationName(operation).toUpperCase());
        System.out.println("=".repeat(50));
        
        try {
            System.out.println("Loading first matrix...");
            RobustSparseMatrix matrix1 = new RobustSparseMatrix(file1);
            
            System.out.println("Loading second matrix...");
            RobustSparseMatrix matrix2 = new RobustSparseMatrix(file2);
            
            System.out.println("Performing " + getOperationName(operation).toLowerCase() + "...");
            RobustSparseMatrix result = null;
            
            switch (operation) {
                case 1:
                    result = matrix1.add(matrix2);
                    break;
                case 2:
                    result = matrix1.subtract(matrix2);
                    break;
                case 3:
                    result = matrix1.multiply(matrix2);
                    break;
            }
            
            if (result != null) {
                String outputFile = "result_" + extractBaseName(file1) + "_" + 
                                  getOperationSymbol(operation) + "_" + 
                                  extractBaseName(file2) + ".txt";
                
                System.out.println("✓ Operation completed successfully!");
                System.out.println("Result: " + result.getRows() + "×" + result.getCols() + 
                                 " (" + result.getNonZeroCount() + " non-zero elements)");
                
                result.saveToFile(outputFile);
                
                System.out.println("\nResult Preview:");
                result.display();
                
            } else {
                System.out.println("✗ Operation failed due to incompatible matrix dimensions");
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    private static String extractBaseName(String filepath) {
        int lastSeparator = Math.max(filepath.lastIndexOf('/'), filepath.lastIndexOf('\\'));
        String filename = filepath.substring(lastSeparator + 1);
        
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0) {
            filename = filename.substring(0, lastDot);
        }
        
        return filename;
    }
}