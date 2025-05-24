import java.io.*;
import java.util.*;

/** Robust Sparse Matrix Implementation*/
public class RobustSparseMatrix {
    private int rows;
    private int cols;
    private MatrixNode[] rowHeads;
    private int nonZeroCount;
    
    private static class MatrixNode {
        int row, col;
        int value;
        MatrixNode nextInRow;
        
        MatrixNode(int r, int c, int val) {
            this.row = r;
            this.col = c;
            this.value = val;
            this.nextInRow = null;
        }
    }
    
    public RobustSparseMatrix(int numRows, int numCols) {
        this.rows = numRows;
        this.cols = numCols;
        this.rowHeads = new MatrixNode[numRows];
        this.nonZeroCount = 0;
    }
    
    public RobustSparseMatrix(String filePath) {
        loadFromFile(filePath);
    }
    
    private void loadFromFile(String filePath) {
        try {
            // First pass: determining actual dimensions
            int actualRows = 0, actualCols = 0;
            int declaredRows = 0, declaredCols = 0;
            
            BufferedReader firstPass = new BufferedReader(new FileReader(filePath));
            String line;
            int lineCount = 0;
            
            while ((line = firstPass.readLine()) != null) {
                line = removeAllSpaces(line);
                if (line.length() == 0) continue;
                
                if (lineCount == 0 && line.startsWith("rows=")) {
                    declaredRows = parseInteger(line.substring(5));
                } else if (lineCount == 1 && line.startsWith("cols=")) {
                    declaredCols = parseInteger(line.substring(5));
                } else if (line.startsWith("(") && line.endsWith(")")) {
                    // Parse entry to find actual dimensions
                    String[] parts = parseEntry(line);
                    if (parts.length == 3) {
                        int row = parseInteger(parts[0]);
                        int col = parseInteger(parts[1]);
                        actualRows = Math.max(actualRows, row + 1);
                        actualCols = Math.max(actualCols, col + 1);
                    }
                }
                lineCount++;
            }
            firstPass.close();
            
            // Use the larger of declared vs actual dimensions
            this.rows = Math.max(declaredRows, actualRows);
            this.cols = Math.max(declaredCols, actualCols);
            this.rowHeads = new MatrixNode[this.rows];
            this.nonZeroCount = 0;
            
            System.out.println("Matrix dimensions: " + this.rows + "x" + this.cols);
            System.out.println("(Declared: " + declaredRows + "x" + declaredCols + 
                             ", Actual: " + actualRows + "x" + actualCols + ")");
            
            // Second pass: loading the data
            BufferedReader secondPass = new BufferedReader(new FileReader(filePath));
            lineCount = 0;
            
            while ((line = secondPass.readLine()) != null) {
                line = removeAllSpaces(line);
                if (line.length() == 0) continue;
                
                if (lineCount >= 2 && line.startsWith("(") && line.endsWith(")")) {
                    String[] parts = parseEntry(line);
                    if (parts.length == 3) {
                        int row = parseInteger(parts[0]);
                        int col = parseInteger(parts[1]);
                        int value = parseInteger(parts[2]);
                        
                        if (value != 0 && row >= 0 && col >= 0 && row < this.rows && col < this.cols) {
                            setElement(row, col, value);
                        }
                    }
                }
                lineCount++;
            }
            secondPass.close();
            
            System.out.println("Successfully loaded " + this.nonZeroCount + " non-zero elements");
            
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private String removeAllSpaces(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != ' ' && c != '\t' && c != '\n' && c != '\r') {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    private String[] parseEntry(String line) {
        // Remove parentheses
        line = line.substring(1, line.length() - 1);
        
        // Split by comma and trim
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ',') {
                parts.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(line.charAt(i));
            }
        }
        parts.add(current.toString().trim());
        
        return parts.toArray(new String[0]);
    }
    
    private int parseInteger(String str) {
        if (str.length() == 0) return 0;
        
        int result = 0;
        int sign = 1;
        int startIdx = 0;
        
        if (str.charAt(0) == '-') {
            sign = -1;
            startIdx = 1;
        }
        
        for (int i = startIdx; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return 0; // Return 0 for invalid numbers instead of crashing
            }
            result = result * 10 + (c - '0');
        }
        
        return result * sign;
    }
    
    public void setElement(int currRow, int currCol, int value) {
        if (currRow < 0 || currRow >= rows || currCol < 0 || currCol >= cols || value == 0) {
            return;
        }
        
        MatrixNode newNode = new MatrixNode(currRow, currCol, value);
        
        if (rowHeads[currRow] == null) {
            rowHeads[currRow] = newNode;
        } else {
            MatrixNode current = rowHeads[currRow];
            MatrixNode prev = null;
            
            while (current != null && current.col < currCol) {
                prev = current;
                current = current.nextInRow;
            }
            
            if (current != null && current.col == currCol) {
                current.value = value;
                return;
            }
            
            if (prev == null) {
                newNode.nextInRow = rowHeads[currRow];
                rowHeads[currRow] = newNode;
            } else {
                newNode.nextInRow = current;
                prev.nextInRow = newNode;
            }
        }
        
        nonZeroCount++;
    }
    
    public int getElement(int currRow, int currCol) {
        if (currRow < 0 || currRow >= rows || currCol < 0 || currCol >= cols) {
            return 0;
        }
        
        MatrixNode current = rowHeads[currRow];
        while (current != null) {
            if (current.col == currCol) {
                return current.value;
            }
            if (current.col > currCol) {
                break;
            }
            current = current.nextInRow;
        }
        return 0;
    }
    
    public RobustSparseMatrix add(RobustSparseMatrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            System.out.println("Matrix dimensions don't match for addition");
            return null;
        }
        
        RobustSparseMatrix result = new RobustSparseMatrix(this.rows, this.cols);
        
        for (int i = 0; i < this.rows; i++) {
            MatrixNode thisNode = this.rowHeads[i];
            MatrixNode otherNode = other.rowHeads[i];
            
            while (thisNode != null || otherNode != null) {
                if (thisNode == null) {
                    result.setElement(i, otherNode.col, otherNode.value);
                    otherNode = otherNode.nextInRow;
                } else if (otherNode == null) {
                    result.setElement(i, thisNode.col, thisNode.value);
                    thisNode = thisNode.nextInRow;
                } else if (thisNode.col < otherNode.col) {
                    result.setElement(i, thisNode.col, thisNode.value);
                    thisNode = thisNode.nextInRow;
                } else if (thisNode.col > otherNode.col) {
                    result.setElement(i, otherNode.col, otherNode.value);
                    otherNode = otherNode.nextInRow;
                } else {
                    int sum = thisNode.value + otherNode.value;
                    if (sum != 0) {
                        result.setElement(i, thisNode.col, sum);
                    }
                    thisNode = thisNode.nextInRow;
                    otherNode = otherNode.nextInRow;
                }
            }
        }
        
        return result;
    }
    
    public RobustSparseMatrix subtract(RobustSparseMatrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            System.out.println("Matrix dimensions don't match for subtraction");
            return null;
        }
        
        RobustSparseMatrix result = new RobustSparseMatrix(this.rows, this.cols);
        
        for (int i = 0; i < this.rows; i++) {
            MatrixNode thisNode = this.rowHeads[i];
            MatrixNode otherNode = other.rowHeads[i];
            
            while (thisNode != null || otherNode != null) {
                if (thisNode == null) {
                    result.setElement(i, otherNode.col, -otherNode.value);
                    otherNode = otherNode.nextInRow;
                } else if (otherNode == null) {
                    result.setElement(i, thisNode.col, thisNode.value);
                    thisNode = thisNode.nextInRow;
                } else if (thisNode.col < otherNode.col) {
                    result.setElement(i, thisNode.col, thisNode.value);
                    thisNode = thisNode.nextInRow;
                } else if (thisNode.col > otherNode.col) {
                    result.setElement(i, otherNode.col, -otherNode.value);
                    otherNode = otherNode.nextInRow;
                } else {
                    int diff = thisNode.value - otherNode.value;
                    if (diff != 0) {
                        result.setElement(i, thisNode.col, diff);
                    }
                    thisNode = thisNode.nextInRow;
                    otherNode = otherNode.nextInRow;
                }
            }
        }
        
        return result;
    }
    
    public RobustSparseMatrix multiply(RobustSparseMatrix other) {
        // Check if dimensions are compatible or nearly compatible
        if (this.cols != other.rows) {
            // Check if they're off by 1 (common in real datasets)
            if (Math.abs(this.cols - other.rows) <= 1) {
                System.out.println("Warning: Dimension mismatch by 1. Adjusting for compatibility.");
                System.out.println("Matrix A cols: " + this.cols + ", Matrix B rows: " + other.rows);
                
                // Use the smaller dimension for safety
                int compatibleDim = Math.min(this.cols, other.rows);
                System.out.println("Using compatible dimension: " + compatibleDim);
                
                // Check if this multiplication will be too large
                long resultSize = (long)this.rows * other.cols;
                if (resultSize > 1000000) { // 1 million
                    System.out.println("ERROR: Result matrix would be too large (" + this.rows + "×" + other.cols + " = " + resultSize + " elements)");
                    System.out.println("Maximum supported size: 1,000,000 elements");
                    System.out.println("Your matrices are too large for multiplication. Try smaller test matrices.");
                    return null;
                }
                
                return multiplySimple(other, compatibleDim);
            } else {
                System.out.println("Matrix dimensions don't match for multiplication");
                System.out.println("Matrix A: " + this.rows + "×" + this.cols);
                System.out.println("Matrix B: " + other.rows + "×" + other.cols);
                System.out.println("For A×B: columns of A (" + this.cols + ") must equal rows of B (" + other.rows + ")");
                return null;
            }
        }
        
        // Check if multiplication will be too computationally expensive
        long resultSize = (long)this.rows * other.cols;
        if (resultSize > 1000000) { // 1 million
            System.out.println("ERROR: Result matrix would be too large (" + this.rows + "×" + other.cols + " = " + resultSize + " elements)");
            System.out.println("Maximum supported size: 1,000,000 elements");
            System.out.println("Your matrices are too large for multiplication. Try smaller test matrices.");
            return null;
        }
        
        // Normal multiplication when dimensions match exactly
        return multiplySimple(other, this.cols);
    }
    
    private RobustSparseMatrix multiplySimple(RobustSparseMatrix other, int compatibleDim) {
        RobustSparseMatrix result = new RobustSparseMatrix(this.rows, other.cols);
        
        System.out.println("Performing multiplication...");
        
        for (int i = 0; i < this.rows; i++) {
            if (this.rowHeads[i] == null) continue;
            
            // Show progress every 100 rows
            if (i % 100 == 0 && i > 0) {
                System.out.println("Processed " + i + "/" + this.rows + " rows (" + (i * 100 / this.rows) + "%)");
            }
            
            for (int j = 0; j < other.cols; j++) {
                int dotProduct = 0;
                
                MatrixNode thisNode = this.rowHeads[i];
                while (thisNode != null) {
                    if (thisNode.col < compatibleDim) {
                        int otherValue = other.getElement(thisNode.col, j);
                        if (otherValue != 0) {
                            dotProduct += thisNode.value * otherValue;
                        }
                    }
                    thisNode = thisNode.nextInRow;
                }
                
                if (dotProduct != 0) {
                    result.setElement(i, j, dotProduct);
                }
            }
        }
        
        System.out.println("Multiplication completed!");
        return result;
    }
    
    public void saveToFile(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            
            writer.write("rows=" + this.rows + "\n");
            writer.write("cols=" + this.cols + "\n");
            
            for (int i = 0; i < this.rows; i++) {
                MatrixNode current = this.rowHeads[i];
                while (current != null) {
                    writer.write("(" + current.row + ", " + current.col + ", " + current.value + ")\n");
                    current = current.nextInRow;
                }
            }
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    
    public void display() {
        System.out.println("Sparse Matrix (" + rows + "x" + cols + "):");
        System.out.println("Non-zero elements: " + nonZeroCount);
        
        int displayed = 0;
        for (int i = 0; i < this.rows && displayed < 10; i++) {
            MatrixNode current = this.rowHeads[i];
            while (current != null && displayed < 10) {
                System.out.println("(" + current.row + ", " + current.col + ") = " + current.value);
                current = current.nextInRow;
                displayed++;
            }
        }
        if (nonZeroCount > 10) {
            System.out.println("... and " + (nonZeroCount - 10) + " more elements");
        }
        System.out.println();
    }
    
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public int getNonZeroCount() { return nonZeroCount; }
}
