package Walter.Settings;


import Walter.exceptions.ReasonedException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;

public class SeasonSetting extends EventSetting {
    /** These could just be public, as all setter, getter and has methods below are just wrappers, but
     * this way we encapsulate it a bit more and the produced yaml file for the config files actually just
     * show the information that is requried.
     */
    private final FileSetting serverLogoFile;
    private final FileSetting walterLogoFile;
    private final ColorSetting memberColor;

    public SeasonSetting() {
        String directory = "/events/";
        serverLogoFile = new FileSetting(directory);
        walterLogoFile = new FileSetting(directory);
        memberColor = new ColorSetting();
    }

    public void setServerLogoFile(String value) throws ReasonedException { this.serverLogoFile.setValue(value); }

    public void setWalterLogoFile(String value) throws ReasonedException { this.walterLogoFile.setValue(value); }

    public void setMemberColor(String value) throws ReasonedException { this.memberColor.setValue(value); }

    public boolean hasServerLogoFile() { return serverLogoFile.hasValue(); }

    public boolean hasWalterLogoFile() { return walterLogoFile.hasValue(); }

    public boolean hasMemberColor() { return memberColor.hasValue(); }

    @Nonnull
    public String getServerLogoFile() { return serverLogoFile.getValueString(); }

    @Nonnull
    public String getWalterLogoFile() { return walterLogoFile.getValueString(); }

    @Nonnull
    public String getMemberColor() { return memberColor.getValueString(); }

    @Nullable
    public String getServerLogoFileValue() { return serverLogoFile.getValue(); }

    @Nullable
    public String getWalterLogoFileValue() { return walterLogoFile.getValue(); }

    @Nullable
    public Color getMemberColorValue() { return memberColor.getValue(); }

    public String toString() {
        return    "name:         " + getName() +
                "\nstart date:   " + (hasStartDate() ? startDate : "Undefined") +
                "\nmember color: " + (memberColor.hasValue() ? memberColor.getValueString() : "DEFAULT") +
                "\nserver logo:  " + (serverLogoFile.hasValue() ? serverLogoFile.getValueString() : "DEFAULT") +
                "\nwalter logo:  " + (walterLogoFile.hasValue() ? walterLogoFile.getValueString() : "DEFAULT");
    }
}
