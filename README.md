# Sparse Matrix Operations Calculator

A high-performance Java implementation for sparse matrix operations designed to handle large real-world datasets efficiently.

## 📋 Table of Contents
- [Overview](#overview)
- [Project Structure](#project-structure)
- [Features](#features)
- [Mathematical Background](#mathematical-background)
- [Implementation Details](#implementation-details)
- [Installation & Usage](#installation--usage)
- [Operations Explained](#operations-explained)
- [Performance Considerations](#performance-considerations)
- [Troubleshooting](#troubleshooting)
- [Example Usage](#example-usage)

## 🎯 Overview

This project implements a **sparse matrix data structure** optimized for memory efficiency and computational performance. Sparse matrices are matrices where most elements are zero, making traditional dense matrix representations wasteful.

### Key Advantages:
- **Memory Efficient**: Only stores non-zero elements
- **Performance Optimized**: Custom algorithms for sparse matrix operations
- **Real-World Ready**: Handles dimension mismatches and large datasets
- **User Friendly**: Interactive command-line interface

## 📁 Project Structure

```
/dsa/sparse_matrix/
├── code/
│   └── src/
│       ├── RobustSparseMatrix.java    # Core sparse matrix implementation
│       ├── MatrixMain.java            # Main program with user interface
│       └── QuickTest.java             # Testing utilities
├── sample_inputs/
│   ├── easy_sample_01_2.txt           # Sample sparse matrix files
│   ├── easy_sample_01_3.txt
│   ├── easy_sample_02_1.txt
│   └── easy_sample_02_2.txt
└── output/                            # Generated automatically
    ├── result_matrix1_add_matrix2.txt # Operation results
    ├── result_matrix1_sub_matrix2.txt
    └── result_matrix1_mul_matrix2.txt
```

### ✨ Features

### Core Functionality
- ✅ **Matrix Addition**: Element-wise addition of two sparse matrices
- ✅ **Matrix Subtraction**: Element-wise subtraction of two sparse matrices
- ✅ **Matrix Multiplication**: Standard mathematical matrix multiplication
- ✅ **File I/O**: Read from and write to custom sparse matrix format
- ✅ **Organized Output**: Automatically creates `output/` folder for results
- ✅ **Dimension Validation**: Automatic dimension checking and adjustment
- ✅ **Error Handling**: Robust error handling for real-world data

### Advanced Features
- 🔧 **Auto-Dimension Adjustment**: Handles off-by-one dimension mismatches
- 🚀 **Performance Optimization**: Efficient algorithms for large matrices
- 📊 **Progress Tracking**: Shows progress for long-running operations
- 🛡️ **Memory Protection**: Prevents operations that would exhaust memory
- 🔍 **Debug Mode**: Detailed logging for troubleshooting
- 📁 **Clean Organization**: Separates input, code, and output files

## 📚 Mathematical Background

### What is a Sparse Matrix?

A **sparse matrix** is a matrix where most elements are zero. For example:
```
Dense Matrix (4×4):        Sparse Representation:
[1  0  0  3]              (0,0): 1
[0  0  2  0]      →       (0,3): 3
[0  5  0  0]              (1,2): 2
[4  0  0  0]              (2,1): 5
                          (3,0): 4
```

### Why Use Sparse Matrices?

1. **Memory Efficiency**: Store only non-zero elements
2. **Computational Efficiency**: Skip operations involving zeros
3. **Real-World Relevance**: Many practical applications have sparse data

### Mathematical Operations

#### Addition/Subtraction: A ± B
- **Requirement**: A and B must have the same dimensions
- **Process**: Add/subtract corresponding elements
- **Complexity**: O(nnz(A) + nnz(B)) where nnz = number of non-zeros

#### Multiplication: A × B
- **Requirement**: Columns of A must equal rows of B
- **Process**: Dot product of rows and columns
- **Result Dimensions**: (rows of A) × (columns of B)
- **Complexity**: O(nnz(A) × cols(B)) in worst case

## 🔧 Implementation Details

### Data Structure Design

Our implementation uses a **row-based linked list** approach:

```java
class MatrixNode {
    int row, col, value;
    MatrixNode nextInRow;  // Points to next element in same row
}

class SparseMatrix {
    MatrixNode[] rowHeads;  // Array of row head pointers
    int rows, cols, nonZeroCount;
}
```

### Key Design Decisions

1. **Row-Based Storage**: Efficient for row-wise operations
2. **Linked Lists**: Dynamic size, efficient insertion/deletion
3. **Sorted Order**: Elements sorted by column within each row
4. **Dimension Flexibility**: Automatic adjustment for real-world data inconsistencies

### File Format

Our custom format for sparse matrix files:
```
rows=<number_of_rows>
cols=<number_of_columns>
(<row>, <column>, <value>)
(<row>, <column>, <value>)
...
```

**Example:**
```
rows=4
cols=4
(0, 0, 5)
(0, 2, 3)
(1, 1, 8)
(3, 3, 4)
```

## 🚀 Installation & Usage

### Prerequisites
- Java 8 or higher
- Command line access

### Compilation
```bash
# Navigate to the source directory
cd /dsa/sparse_matrix/code/src/

# Compile all Java files
javac *.java
```

### Running the Program
```bash
# Start the interactive calculator
java MatrixMain
```

### Basic Usage Flow
1. **Choose Operation**: Select from addition (1), subtraction (2), multiplication (3), or exit (4)
2. **Enter File Paths**: Provide paths to input matrix files
3. **View Results**: Program displays operation results and saves output file
4. **Continue or Exit**: Choose to perform another operation

## 📖 Operations Explained

### 1. Matrix Addition (A + B)

**Mathematical Definition**: C[i,j] = A[i,j] + B[i,j]

**Requirements**:
- Both matrices must have identical dimensions
- Result matrix has the same dimensions as input matrices

**Algorithm**:
```java
// Merge two sorted linked lists approach
for each row i:
    merge A.row[i] and B.row[i]
    add corresponding elements
    store non-zero results
```

**Example**:
```
A = [1 0 3]    B = [2 1 0]    A + B = [3 1 3]
    [0 2 0]        [0 0 4]            [0 2 4]
```

**Time Complexity**: O(nnz(A) + nnz(B))

### 2. Matrix Subtraction (A - B)

**Mathematical Definition**: C[i,j] = A[i,j] - B[i,j]

**Requirements**: Same as addition

**Algorithm**: Similar to addition but subtract values instead

**Example**:
```
A = [5 0 3]    B = [2 1 0]    A - B = [3 -1 3]
    [0 2 0]        [0 0 4]            [0  2 -4]
```

### 3. Matrix Multiplication (A × B)

**Mathematical Definition**: C[i,j] = Σ(A[i,k] × B[k,j]) for k=0 to cols(A)-1

**Requirements**:
- Number of columns in A must equal number of rows in B
- Result dimensions: rows(A) × cols(B)

**Why Multiplication is Complex**:

1. **Dimension Compatibility**: Unlike addition/subtraction, multiplication requires specific dimension relationships
2. **Computational Intensity**: Each result element requires a dot product calculation
3. **Memory Requirements**: Result matrix can be much larger than inputs
4. **Algorithmic Complexity**: O(rows(A) × cols(B) × cols(A)) operations

**Our Implementation Strategy**:
```java
for each row i in A:
    for each column j in B:
        dotProduct = 0
        for each non-zero element A[i,k]:
            dotProduct += A[i,k] × B[k,j]
        if dotProduct ≠ 0:
            store C[i,j] = dotProduct
```

**Example**:
```
A = [1 2]    B = [5 6]    A × B = [1×5+2×7  1×6+2×8] = [19 22]
    [3 4]        [7 8]            [3×5+4×7  3×6+4×8]   [43 50]
```

**Performance Considerations**:
- **Small matrices**: Fast execution
- **Large matrices**: Can take hours and use significant memory
- **Our protection**: Refuses multiplication if result would exceed 1 million elements

## ⚡ Performance Considerations

### Memory Usage
- **Sparse matrices**: Only non-zero elements stored
- **Memory formula**: ~32 bytes per non-zero element
- **Large matrix warning**: 1M+ result elements may exhaust memory

### Time Complexity
| Operation | Time Complexity | Notes |
|-----------|----------------|-------|
| Addition | O(nnz(A) + nnz(B)) | Linear in non-zeros |
| Subtraction | O(nnz(A) + nnz(B)) | Same as addition |
| Multiplication | O(nnz(A) × cols(B)) | Can be very expensive |
| File I/O | O(nnz) | Linear in non-zeros |

### Performance Optimizations
1. **Efficient Data Structure**: Linked lists avoid array resizing
2. **Skip Zero Operations**: Only process non-zero elements
3. **Progress Tracking**: Shows progress for long operations
4. **Memory Guards**: Prevents operations that would fail
5. **Dimension Adjustment**: Handles real-world data inconsistencies

## 🛠️ Troubleshooting

### Common Issues

#### "File not found" Error
**Problem**: Program can't locate input files
**Solution**: 
- Use full file paths: `../../sample_inputs/filename.txt`
- Or copy files to the `src` directory

#### "Matrix dimensions don't match" 
**Problem**: Incompatible matrix dimensions for the operation
**Solutions**:
- **Addition/Subtraction**: Use matrices with identical dimensions
- **Multiplication**: Ensure cols(A) = rows(B)

#### "Result matrix would be too large"
**Problem**: Multiplication would create huge result matrix
**Solutions**:
- Use smaller test matrices first
- Consider if multiplication is really needed
- Break large matrices into smaller blocks

#### "Input file has wrong format"
**Problem**: File doesn't match expected format
**Solutions**:
- Check file format: `rows=N`, `cols=M`, then `(row,col,value)` entries
- Ensure no extra spaces or invalid characters
- Use provided sample files as templates

### Debug Mode
Run `QuickTest` to debug file loading issues:
```bash
java QuickTest
```

## 📝 Example Usage

### Complete Session Example

```bash
$ java MatrixMain

==============================================
    SPARSE MATRIX OPERATIONS CALCULATOR
==============================================

Available Operations:
1. Matrix Addition (A + B)
2. Matrix Subtraction (A - B)
3. Matrix Multiplication (A × B)
4. Exit Program

Enter your choice (1-4): 1

You selected: Addition
Enter file paths (you can use just filenames if files are in sample_inputs folder):

Enter path for first matrix file: easy_sample_01_2.txt
Found: ../../sample_inputs/easy_sample_01_2.txt
Enter path for second matrix file: easy_sample_01_3.txt
Found: ../../sample_inputs/easy_sample_01_3.txt

==================================================
PROCESSING ADDITION
==================================================
Loading first matrix...
Matrix dimensions: 8433×3181
(Declared: 8433×3180, Actual: 8433×3181)
Successfully loaded 617290 non-zero elements

Loading second matrix...
Matrix dimensions: 3180×8434
(Declared: 3180×8433, Actual: 3180×8434)
Successfully loaded 227966 non-zero elements

Matrix dimensions don't match for addition

✗ Operation failed due to incompatible matrix dimensions

Do you want to perform another operation? (y/n): n
Thank you for using the calculator!
```

### Creating Test Matrices

For testing, create small matrices:

**test_matrix_A.txt**:
```
rows=2
cols=3
(0, 0, 1)
(0, 2, 3)
(1, 1, 2)
```

**test_matrix_B.txt**:
```
rows=3
cols=2
(0, 0, 4)
(1, 1, 5)
(2, 0, 6)
```

These can be multiplied: 2×3 × 3×2 = 2×2

## 🎯 Educational Value

This project demonstrates several important computer science concepts:

### Data Structures
- **Linked Lists**: Dynamic memory allocation
- **Arrays**: Fixed-size collections
- **Hybrid Structures**: Combining arrays and linked lists

### Algorithms
- **Graph Traversal**: Following linked list chains
- **Merging**: Combining sorted sequences
- **Optimization**: Trading space for time

### Software Engineering
- **Modularity**: Separate classes for different responsibilities
- **Error Handling**: Graceful failure and recovery
- **User Experience**: Clear feedback and progress indication
- **Performance**: Efficient algorithms for large data

### Mathematical Computing
- **Numerical Stability**: Handling large numbers
- **Algorithmic Complexity**: Understanding performance implications
- **Memory Management**: Efficient use of system resources

## 🏆 Conclusion

This Sparse Matrix Calculator successfully demonstrates:

1. **Efficient Implementation**: Custom data structures optimized for sparse data
2. **Real-World Applicability**: Handles actual dataset inconsistencies
3. **Educational Value**: Clear examples of fundamental CS concepts
4. **Production Quality**: Robust error handling and user experience
5. **Mathematical Accuracy**: Correct implementation of matrix operations

The project balances theoretical computer science concepts with practical software engineering, creating a tool that is both educational and useful for real sparse matrix computations.

---

**Hasbiyallah Umutoniwabo**: DSA Formative Assignment  
**Language**: Java  
**Paradigm**: Object-Oriented Programming  
**Focus**: Data Structures, Algorithms, Mathematical Computing
