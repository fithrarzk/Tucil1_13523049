import java.io.*;
import java.util.*;

public class PuzzleInput {
    private int rows, cols, numPieces;
    private String caseType;
    private List<List<String>> puzzleShapes = new ArrayList<>();

    public PuzzleInput(String filename) throws IOException {
        readFromFile(filename);
    }

    private void readFromFile(String filename) throws IOException {
        String inputPath = "test/input/" + filename;
        BufferedReader br = new BufferedReader(new FileReader(inputPath));
        String[] firstLine = br.readLine().trim().split(" ");
    
        rows = Integer.parseInt(firstLine[0]);
        cols = Integer.parseInt(firstLine[1]);
        numPieces = Integer.parseInt(firstLine[2]);
    
        caseType = br.readLine().trim(); 
    
        List<String> currentPiece = new ArrayList<>();
        String prevFirstChar = ""; 
        String line;
    
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue; 
    
            String firstChar = String.valueOf(line.charAt(0)); 
    
            if (!prevFirstChar.equals(firstChar) && !currentPiece.isEmpty()) {
                puzzleShapes.add(new ArrayList<>(currentPiece));
                currentPiece.clear();
            }
    
            currentPiece.add(line);
            prevFirstChar = firstChar;
        }

        if (!currentPiece.isEmpty()) {
            puzzleShapes.add(new ArrayList<>(currentPiece));
        }
    
        br.close();
    }
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public int getNumPieces() { return numPieces; }
    public String getCaseType() { return caseType; }
    public List<List<String>> getPuzzleShapes() { return puzzleShapes; }
}
