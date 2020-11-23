package Walter.Settings;

import Walter.Walter;
import Walter.exceptions.ReasonedException;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

public class SeasonSetting extends EventSetting {
    private Date endDate;
    private String serverLogoFile;
    private String walterLogoFile;
    private Color memberColor;


    public void setEndDate(@Nonnull Date endDate) throws ReasonedException {
        if (startDate == null || startDate.after(endDate))
            throw new ReasonedException("Enddate must not be before startdate");
        this.endDate = endDate;
    }

    public void setServerLogoFile(@Nonnull String filename) throws FileNotFoundException {
        if (fileExists(filename)) serverLogoFile = filename;
        else throw new FileNotFoundException("The given server logo file was not found in the events folder");
    }

    public void setWalterLogoFile(@Nonnull String filename) throws FileNotFoundException {
        if (fileExists(filename)) walterLogoFile = filename;
        else throw new FileNotFoundException("The given walter logo file was not found in the events folder");
    }

    public void setMemberColor(@Nonnull String color) throws ReasonedException {
        try {
            memberColor = Color.decode(color);
        } catch (NumberFormatException e) {
            throw new ReasonedException("Could not read a color from given color " + color);
        }
    }

    private boolean fileExists(String filename) {
        File toTest = new File(Walter.location + "/events/" + filename);
        return toTest.exists();
    }

    public boolean hasEndDate() { return endDate != null; }

    public boolean hasServerLogoFile() { return serverLogoFile != null; }

    public boolean hasWalterLogoFile() { return walterLogoFile != null; }

    public boolean hasMemberColor() { return memberColor != null; }

    public Date getEndDate() { return endDate; }

    public String getServerLogoFile() { return serverLogoFile; }

    public String getWalterLogoFile() { return walterLogoFile; }

    public Color getMemberColor() { return memberColor; }

    public String toString() {
        return "";
    }
}
