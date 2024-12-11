import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileStatistics {
    private final String extension;
    private int fileCount;
    private int totalLines;
    private int codeLines;
    private int emptyLines;

    public FileStatistics(String extension) {
        this.extension = extension;
        this.fileCount = 0;
        this.totalLines = 0;
        this.codeLines = 0;
        this.emptyLines = 0;
    }

    public void addFile(File file) throws Exception {
        fileCount++;
        processFile(file);
    }

    public void removeFile(File file) {
        fileCount = Math.max(0, fileCount - 1);
        // Zeilenstatistiken werden hier nicht entfernt, da wir die Datei-Inhalte nicht rückgängig machen können.
    }

    public void updateFile(File file) throws Exception {
        resetFileStats();
        processFile(file);
    }

    private void processFile(File file) throws Exception {
        List<String> lines = Files.readAllLines(file.toPath());
        int fileTotalLines = 0, fileCodeLines = 0, fileEmptyLines = 0;

        for (String line : lines) {
            fileTotalLines++;
            if (line.trim().isEmpty()) {
                fileEmptyLines++;
            } else {
                fileCodeLines++;
            }
        }

        totalLines += fileTotalLines;
        codeLines += fileCodeLines;
        emptyLines += fileEmptyLines;
    }

    private void resetFileStats() {
        totalLines = 0;
        codeLines = 0;
        emptyLines = 0;
    }

    public String getExtension() {
        return extension;
    }

    public int getFileCount() {
        return fileCount;
    }

    public int getTotalLines() {
        return totalLines;
    }

    public int getCodeLines() {
        return codeLines;
    }

    public int getEmptyLines() {
        return emptyLines;
    }
}
