import java.awt.*;
import java.util.List;
import javax.swing.*;

public class PuzzleGUI extends JFrame {
    private JTextField fileInputField;
    private JPanel boardPanel;
    private JTextArea outputArea;
    private JButton solveButton, saveTextButton, saveImageButton;
    private PuzzleSolver solver;
    private int rows, cols;
    private List<List<String>> puzzleShapes;

    public PuzzleGUI() {
        setTitle("IQ Puzzler Solver");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // input
        JPanel inputPanel = new JPanel();
        fileInputField = new JTextField(20);
        inputPanel.add(new JLabel("Masukkan nama file: "));
        inputPanel.add(fileInputField);
        add(inputPanel, BorderLayout.NORTH);

        // board
        boardPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g);
            }
        };
        boardPanel.setPreferredSize(new Dimension(400, 400));
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(boardPanel, BorderLayout.NORTH);
        
        // info
        outputArea = new JTextArea(5, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // solve save
        solveButton = new JButton("Solve");
        saveTextButton = new JButton("Simpan sebagai Teks");
        saveImageButton = new JButton("Simpan sebagai Gambar");
        saveTextButton.setVisible(false);
        saveImageButton.setVisible(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(solveButton);
        buttonPanel.add(saveTextButton);
        buttonPanel.add(saveImageButton);
        add(buttonPanel, BorderLayout.SOUTH);
        solveButton.addActionListener(e -> loadPuzzle());
        saveTextButton.addActionListener(e -> saveSolutionAsText());
        saveImageButton.addActionListener(e -> saveSolutionAsImage());
    }

    private void loadPuzzle() {
        String fileName = fileInputField.getText().trim();
        if (fileName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan nama file terlebih dahulu!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            PuzzleInput puzzleInput = new PuzzleInput(fileName);
            this.rows = puzzleInput.getRows();
            this.cols = puzzleInput.getCols();
            this.puzzleShapes = puzzleInput.getPuzzleShapes();
            this.solver = new PuzzleSolver(rows, cols, puzzleShapes);
            boardPanel.setPreferredSize(new Dimension(cols * 50, rows * 50));
            boardPanel.revalidate();
            boardPanel.repaint();
            saveTextButton.setVisible(false);
            saveImageButton.setVisible(false);
            solvePuzzle();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void solvePuzzle() {
        outputArea.setText("");
        long startTime = System.currentTimeMillis();
        boolean solved = solver.solve();
        long endTime = System.currentTimeMillis();
        double executionTime = endTime - startTime;
        boardPanel.repaint();
    
        if (solved) {
            outputArea.append(
                    "Waktu eksekusi: " + executionTime + " ms\n" +
                    "Jumlah iterasi: " + solver.getIterationCount() + "\n");
    
            saveTextButton.setVisible(true);
            saveImageButton.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Tidak ada solusi yang ditemukan!");
        }
    }
    

    private void saveSolutionAsText() {
        if (solver == null) return;
        String fileName = fileInputField.getText().trim();
        solver.saveSolution(solver.getSolutionText(), fileName);
    }

    private void saveSolutionAsImage() {
        if (solver == null) return;
        String fileName = fileInputField.getText().trim();
        solver.saveSolutionAsImage(fileName);
    }

    private void drawBoard(Graphics g) {
        if (solver == null) return;
        int cellSize = 50;
        FontMetrics fm = g.getFontMetrics();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char piece = solver.getBoard()[r][c];
                g.setColor(PuzzleSolver.colorMap.getOrDefault(piece, Color.WHITE));
                g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(c * cellSize, r * cellSize, cellSize, cellSize);
                if (piece != ' ') {
                    String pieceText = String.valueOf(piece);
                    int textWidth = fm.stringWidth(pieceText);
                    int textHeight = fm.getAscent();
                    int x = c * cellSize + (cellSize - textWidth) / 2;
                    int y = r * cellSize + (cellSize + textHeight) / 2 - 4;
                    g.drawString(pieceText, x, y);
                }
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PuzzleGUI().setVisible(true));
    }
}