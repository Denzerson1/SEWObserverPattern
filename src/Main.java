import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        FileWatcher watcher = new FileWatcher(Paths.get("C:\\Users\\JamesDean\\Downloads\\ObserverPattern\\sewfiles"));
        FileStatisticsPrinter printer = new FileStatisticsPrinter();

        watcher.setObserver(printer); // Observer hinzufügen
        System.out.println("Watcher wird gestartet...");
        watcher.startWatching();
    }
}
