import java.util.Map;

public interface FileChangeObserver {
    void onFileChange(Map<String, FileStatistics> fileStats);
}
