import java.util.*;

public class PuzzleSolver {
    private char[][] board;
    private List<List<String>> puzzleShapes;
    private int rows, cols;
    private long iterationCount = 0;
    private long startTime, endTime;

    public PuzzleSolver(int rows, int cols, List<List<String>> puzzleShapes) {
        this.rows = rows;
        this.cols = cols;
        this.puzzleShapes = puzzleShapes;
        this.board = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            Arrays.fill(board[i], '.');
        }
    }

    public boolean solve() {
        startTime = System.currentTimeMillis();
        boolean result = placePiece(0);
        endTime = System.currentTimeMillis();
        return result;
    }

    private boolean placePiece(int index) {
        if (index == puzzleShapes.size()) {
            return isBoardFull();
        }

        List<String> piece = puzzleShapes.get(index);
        char pieceChar = getPieceChar(piece);

        List<List<String>> transformations = generateTransformations(piece);

        for (List<String> transformedPiece : transformations) {
            if (transformedPiece.isEmpty() || transformedPiece.get(0).isEmpty()) continue;

            int pieceHeight = transformedPiece.size();
            int pieceWidth = transformedPiece.stream().mapToInt(String::length).max().orElse(0);

            for (int r = 0; r <= rows - pieceHeight; r++) {
                for (int c = 0; c <= cols - pieceWidth; c++) {
                    iterationCount++;
                    if (canPlace(transformedPiece, r, c)) {
                        place(transformedPiece, r, c, pieceChar);
                        if (placePiece(index + 1)) return true;
                        remove(transformedPiece, r, c);
                    }
                }
            }
        }

        return false;
    }

    private char getPieceChar(List<String> piece) {
        return piece.stream().filter(row -> !row.isEmpty()).map(row -> row.charAt(0)).findFirst().orElse('?');
    }

    private boolean canPlace(List<String> piece, int row, int col) {
        for (int r = 0; r < piece.size(); r++) {
            for (int c = 0; c < piece.get(r).length(); c++) {
                if (piece.get(r).charAt(c) != ' ' && (row + r >= rows || col + c >= cols || board[row + r][col + c] != '.')) {
                    return false;
                }
            }
        }
        return true;
    }
    

    private void place(List<String> piece, int row, int col, char pieceChar) {
        for (int r = 0; r < piece.size(); r++) {
            for (int c = 0; c < piece.get(r).length(); c++) {
                if (piece.get(r).charAt(c) != ' ') {
                    board[row + r][col + c] = pieceChar;
                }
            }
        }
    }

    private void remove(List<String> piece, int row, int col) {
        for (int r = 0; r < piece.size(); r++) {
            for (int c = 0; c < piece.get(r).length(); c++) {
                if (piece.get(r).charAt(c) != ' ') {
                    board[row + r][col + c] = '.';
                }
            }
        }
    }

    private boolean isBoardFull() {
        for (char[] row : board) {
            for (char c : row) {
                if (c == '.') return false;
            }
        }
        return true;
    }

    private List<List<String>> generateTransformations(List<String> piece) {
        Set<List<String>> transformations = new LinkedHashSet<>();
        transformations.add(piece);
        transformations.add(flipHorizontally(piece));
        transformations.add(flipVertically(piece));

        List<String> rotated = piece;

        for (int i = 0; i < 3; i++) {
            rotated = rotate90(rotated);
            transformations.add(rotated);
            transformations.add(flipHorizontally(rotated));
            transformations.add(flipVertically(rotated));
        }

        return new ArrayList<>(transformations);
    }

    private List<String> rotate90(List<String> piece) {
        if (piece.isEmpty()) return new ArrayList<>();
    
        int height = piece.size();
        int width = piece.stream().mapToInt(String::length).max().orElse(0);
    
        char[][] rotated = new char[width][height];
    
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < piece.get(r).length(); c++) {
                rotated[c][height - 1 - r] = piece.get(r).charAt(c);
            }
        }
    
        List<String> rotatedStrings = new ArrayList<>();
        for (char[] row : rotated) {
            rotatedStrings.add(new String(row).replace('\0', ' '));  
        }
    
        return rotatedStrings;
    }
    

    private List<String> flipHorizontally(List<String> piece) {
        List<String> flipped = new ArrayList<>();
        for (String row : piece) {
            flipped.add(new StringBuilder(row).reverse().toString());
        }
        return flipped;
    }

    private List<String> flipVertically(List<String> piece) {
        List<String> flipped = new ArrayList<>(piece);
        Collections.reverse(flipped);
        return flipped;
    }

    public void printSolution() {
        for (char[] row : board) {
            for (char c : row) {
                System.out.print(getColoredChar(c) + " ");
            }
            System.out.println();
        }
        System.out.println("Waktu Pencarian: " + (endTime - startTime) + " ms");
        System.out.println("Banyak kasus yang ditinjau: " + iterationCount);
    }

    private String getColoredChar(char c) {
        String[] colors = {
            "\u001B[31m", "\u001B[32m", "\u001B[33m", "\u001B[34m", "\u001B[35m",
            "\u001B[36m", "\u001B[37m", "\u001B[91m", "\u001B[92m", "\u001B[93m"
        };
        return colors[(c - 'A') % colors.length] + c + "\u001B[0m";
    }
}
