import java.io.*;
import java.util.*;

public class PuzzleInput {
    private int rows, cols, pieces;
    private String type;
    private String filename;
    private List<List<String>> puzzleShapes = new ArrayList<>();

    public PuzzleInput(String filename) throws IOException {
        this.filename = filename;
        readFromFile(filename);
    }

    private void readFromFile(String filename) throws IOException {
        String inputPath = "test/input/" + filename;
        BufferedReader br = new BufferedReader(new FileReader(inputPath));

        // Baca baris pertama (N M P)
        String[] firstLine = br.readLine().trim().split(" ");
        if (firstLine.length != 3) {
            throw new IllegalArgumentException("Format input salah! Baris pertama harus berisi 3 angka (N M P).");
        }

        rows = Integer.parseInt(firstLine[0]);
        cols = Integer.parseInt(firstLine[1]);
        pieces = Integer.parseInt(firstLine[2]);
        type = br.readLine().trim();

        List<String> currentPiece = new ArrayList<>();
        String prevFirstChar = ""; 
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue; 
            }

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

        if (puzzleShapes.size() != pieces) {
            throw new IllegalArgumentException(
                "Jumlah puzzle = " + pieces + ", tidak sesuai dengan jumlah puzzle yang ditemukan = " + puzzleShapes.size()
            );
        }
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public int getPieces() { return pieces; }
    public String getType() { return type; }
    public List<List<String>> getPuzzleShapes() { return puzzleShapes; }
}