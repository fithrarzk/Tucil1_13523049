import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan nama file: ");
        String fileName = scanner.nextLine();
        PuzzleInput puzzleInput;
        try {
            puzzleInput = new PuzzleInput(fileName);

            // System.out.println("Dimensi papan: " + puzzleInput.getRows() + "x" + puzzleInput.getCols());
            // System.out.println("Jumlah puzzle: " + puzzleInput.getNumPieces());
            // System.out.println("Jenis kasus: " + puzzleInput.getCaseType());
            // System.out.println("Bentuk puzzle: " + puzzleInput.getPuzzleShapes());

            PuzzleSolver solver = new PuzzleSolver(
                puzzleInput.getRows(),
                puzzleInput.getCols(),
                puzzleInput.getPuzzleShapes()
            );

            boolean found = solver.solve();
            if (found) {
                solver.printSolution();
            } else {
                System.out.println("Tidak ada solusi yang ditemukan.");
            }

        } catch (Exception e) {
            System.err.println("Terjadi kesalahan: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
