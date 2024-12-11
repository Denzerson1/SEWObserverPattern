import java.io.File;
import java.nio.file.*;
import java.util.List;

public abstract class Watcher {
    private final Path folder;

    public Watcher(Path folder) {
        this.folder = folder;
    }

    public void startWatching() throws Exception {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            folder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path changed = folder.resolve((Path) event.context());
                    if (Files.isRegularFile(changed)) {
                        onFileChange(changed.toFile());
                    }
                }
                key.reset();
            }
        }
    }

    protected abstract void onFileChange(File file);
}
