import java.util.Map;

public class FileStatisticsPrinter implements FileChangeObserver {
    @Override
    public void onFileChange(Map<String, FileStatistics> fileStats) {
        System.out.println("               Dateien    Zeilen    Davon Code    Davon Leerzeilen");
        System.out.println("---------------------------------------------------------------");
        fileStats.values().forEach(stat ->
                System.out.printf("%-15s %6d %8d %11d %16d%n",
                        stat.getExtension(),
                        stat.getFileCount(),
                        stat.getTotalLines(),
                        stat.getCodeLines(),
                        stat.getEmptyLines()
                )
        );
    }
}
