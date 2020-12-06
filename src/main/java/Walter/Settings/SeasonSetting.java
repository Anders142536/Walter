package Walter.Settings;


public class SeasonSetting extends EventSetting {
    public final FileSetting serverLogoFile;
    public final FileSetting walterLogoFile;
    public final ColorSetting memberColor;

    public SeasonSetting() {
        String directory = "/events/";
        serverLogoFile = new FileSetting(directory);
        walterLogoFile = new FileSetting(directory);
        memberColor = new ColorSetting();

    }

    public String toString() {
        return    "name:         " + getName() +
                "\nstart date:   " + (hasStartDate() ? startDate : "Undefined") +
                "\nmember color: " + (memberColor.hasValue() ? memberColor.getValueString() : "DEFAULT") +
                "\nserver logo:  " + (serverLogoFile.hasValue() ? serverLogoFile.getValueString() : "DEFAULT") +
                "\nwalter logo:  " + (walterLogoFile.hasValue() ? walterLogoFile.getValueString() : "DEFAULT");
    }
}
