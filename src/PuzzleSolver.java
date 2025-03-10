import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
public class PuzzleSolver {
    private char[][] board;
    private List<List<String>> puzzle;
    private int rows, cols;
    private long count = 0;
    private long startTime, endTime;
    public static final Map<Character, Color> colorMap = new HashMap<>();

    static {
        char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        Color[] colors = {
            new Color(255, 0, 0), new Color(0, 255, 0), new Color(0, 0, 255),
            new Color(255, 255, 0), new Color(255, 165, 0), new Color(255, 20, 147),
            new Color(0, 255, 255), new Color(128, 0, 128), new Color(165, 42, 42),
            new Color(255, 192, 203), new Color(0, 128, 0), new Color(75, 0, 130),
            new Color(128, 128, 0), new Color(255, 69, 0), new Color(0, 191, 255),
            new Color(46, 139, 87), new Color(255, 105, 180), new Color(218, 112, 214),
            new Color(112, 128, 144), new Color(240, 230, 140), new Color(139, 69, 19),
            new Color(47, 79, 79), new Color(60, 179, 113), new Color(123, 104, 238),
            new Color(176, 224, 230), new Color(199, 21, 133)
        };

        for (int i = 0; i < letters.length; i++) {
            colorMap.put(letters[i], colors[i % colors.length]);
        }
    }

    public PuzzleSolver(int rows, int cols, List<List<String>> puzzle) {
        this.rows = rows;
        this.cols = cols;
        this.puzzle = new ArrayList<>(puzzle);
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
        if (index == puzzle.size()) {
            return isBoardFull();
        }

        List<String> piece = puzzle.get(index);
        char pieceChar = getPieceChar(piece);
        List<List<String>> transformations = transform(piece);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                for (List<String> transformedPiece : transformations) {
                    if (transformedPiece.isEmpty() || transformedPiece.get(0).isEmpty()) continue;
                    count++;
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
        return piece.stream()
                .filter(row -> row.trim().length() > 0) 
                .map(row -> row.trim().charAt(0)) 
                .findFirst()
                .orElse('?'); 
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

    public long getIterationCount() {
        return count;
    }

    public String getSolutionText() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                sb.append(board[r][c]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private List<List<String>> transform(List<String> piece) {
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
            rotatedStrings.add(new String(row).replace(' ', ' '));
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

    public void printSolution(String filename) {
        StringBuilder output = new StringBuilder();
        for (char[] row : board) {
            for (char c : row) {
                System.out.print(getColoredChar(c));
                output.append(c);
            }
            System.out.println();
            output.append("\n");
        }
        System.out.println("Waktu Pencarian: " + (endTime - startTime) + " ms");
        System.out.println("Banyak kasus yang ditinjau: " + count);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Apakah Anda ingin menyimpan solusi dalam txt? (ya/tidak): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("ya")) {
            saveSolution(output.toString(), filename);
        }

        Scanner scan = new Scanner(System.in);
        System.out.print("Apakah Anda ingin menyimpan solusi dalam gambar? (ya/tidak): ");
        String respon = scan.nextLine().trim().toLowerCase();
        
        if (respon.equals("ya")) {
            saveSolutionAsImage(filename);
        }
    }

    private String getColoredChar(char c) {
        Map<Character, String> colorMap = new HashMap<>();
        
        char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        String[] colors = {
            "\u001B[31m", "\u001B[32m", "\u001B[33m", "\u001B[34m", "\u001B[35m",
            "\u001B[36m", "\u001B[91m", "\u001B[92m", "\u001B[93m", "\u001B[94m",
            "\u001B[95m", "\u001B[96m", "\u001B[97m", "\u001B[90m", "\u001B[100m",
            "\u001B[101m", "\u001B[102m", "\u001B[103m", "\u001B[104m", "\u001B[105m",
            "\u001B[106m", "\u001B[107m", "\u001B[37m", "\u001B[95m", "\u001B[94m",
            "\u001B[92m"
        };
    
        for (int i = 0; i < letters.length; i++) {
            colorMap.put(letters[i], colors[i % colors.length]);
        }
        return colorMap.getOrDefault(c, "\u001B[0m") + c + "\u001B[0m";
    }

    public void saveSolution(String solutionText, String fileName) {
        String folderPath = "test/output/";
        String filePath = folderPath + "solusi_"+ fileName + ".txt";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(solutionText);
            writer.write("Waktu Pencarian: " + (endTime - startTime) + " ms\n");
            writer.write("Banyak kasus yang ditinjau: " + count + "\n\n");
        } catch (IOException e) {
            System.out.println("Gagal menyimpan solusi: " + e.getMessage());
        }
    }
    public void saveSolutionAsImage(String fileName) {
        int cellSize = 50;
        int width = cols * cellSize;
        int height = rows * cellSize;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setFont(new Font("Arial", Font.BOLD, cellSize / 2));

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char pieceChar = board[r][c];
                g2d.setColor(colorMap.getOrDefault(pieceChar, Color.WHITE));
                g2d.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(c * cellSize, r * cellSize, cellSize, cellSize);
                if (pieceChar != '.') {
                    g2d.setColor(Color.BLACK);
                    int x = c * cellSize + cellSize / 3;
                    int y = r * cellSize + 2 * cellSize / 3;
                    g2d.drawString(String.valueOf(pieceChar), x, y);
                }
            }
        }
        g2d.dispose();

        try {
            File outputFile = new File("test/output/solusi_" + fileName + ".png");
            ImageIO.write(image, "png", outputFile);
            System.out.println("Solusi berhasil disimpan sebagai gambar di " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Gagal menyimpan gambar: " + e.getMessage());
        }
    }

    public char[][] getBoard() { return board; }
}