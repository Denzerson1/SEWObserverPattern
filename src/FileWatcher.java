import java.io.File;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class FileWatcher {
    private final Path folder;
    private final Map<String, FileStatistics> fileStats = new HashMap<>();
    private FileChangeObserver observer;

    public FileWatcher(Path folder) {
        this.folder = folder;
    }

    public void setObserver(FileChangeObserver observer) {
        this.observer = observer;
    }

    public void startWatching() throws Exception {
        // Initialisiere vorhandene Dateien
        initializeExistingFiles();

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            folder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path changed = folder.resolve((Path) event.context());
                    if (Files.isRegularFile(changed)) {
                        handleFileChange(changed.toFile(), event.kind());
                    }
                }
                key.reset();
            }
        }
    }

    private void initializeExistingFiles() throws Exception {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
            for (Path path : stream) {
                if (Files.isRegularFile(path)) {
                    File file = path.toFile();
                    String extension = getFileExtension(file);

                    fileStats.putIfAbsent(extension, new FileStatistics(extension));
                    fileStats.get(extension).addFile(file); // Verarbeite die Datei
                }
            }
        }

        // Push initiale Daten an den Observer
        if (observer != null) {
            observer.onFileChange(new HashMap<>(fileStats));
        }
    }

    private void handleFileChange(File file, WatchEvent.Kind<?> kind) {
        String extension = getFileExtension(file);
        fileStats.putIfAbsent(extension, new FileStatistics(extension));

        try {
            if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                fileStats.get(extension).addFile(file);
            } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                fileStats.get(extension).removeFile(file);
            } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                fileStats.get(extension).updateFile(file); // Datei aktualisieren
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (observer != null) {
            observer.onFileChange(new HashMap<>(fileStats)); // Push aktualisierte Daten
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        return (lastDot == -1) ? "unknown" : name.substring(lastDot + 1);
    }
}
