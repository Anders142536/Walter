package Walter.Settings;

import Walter.Walter;
import Walter.exceptions.ReasonedException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;

public class FileSetting extends Setting {
    private String fileName;
    private final String directoryPath;

    public FileSetting(@Nonnull String directoryPath) {
        this.directoryPath = Walter.location + directoryPath;
    }

    @Override
    public void setValue(@Nonnull String fileName) throws ReasonedException {
        if (!fileName.isBlank() && fileExists(fileName)) this.fileName = fileName;
        //TODO: make this return a list of available files
        else throw new ReasonedException("File not found under path: " + directoryPath + fileName);
    }

    private boolean fileExists(String filename) {
        File toTest = new File(directoryPath + filename);
        return toTest.exists();
    }

    @Override
    public boolean hasValue() { return fileName != null; }

    @Nullable
    public String getValue() {
        return (hasValue() ? directoryPath + fileName : null);
    }

    @Nonnull
    public String getValueString() {
        return (hasValue() ? directoryPath + fileName : "Undefined");
    }
}
