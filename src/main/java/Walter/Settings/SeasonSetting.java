package Walter.Settings;


import Walter.Helper;
import Walter.entities.BlackRole;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.managers.GuildManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

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

    @Override
    public void run() {
        if (hasServerLogoFile()) changeToDefinedServerLogo();
        if (hasWalterLogoFile()) changeToDefinedWalterLogo();
        if (hasMemberColor()) changeToDefinedMemberColor();
    }

    private void changeToDefinedServerLogo() {
        assert serverLogoFile.getValue() != null;
        try {
            File serverLogoFile = new File(this.serverLogoFile.getValue());
            Icon serverLogo = Icon.from(serverLogoFile);
            Helper.getGuildManager().setIcon(serverLogo).queue();
        } catch (IOException e) {
            Helper.logError("There was an exception when trying to change to the serverlogo defined " +
                    "in the " + name + " event:\n" + e.getMessage());
        }
    }

    private void changeToDefinedWalterLogo() {
        assert walterLogoFile.getValue() != null;
        try {
            File walterLogoFile = new File(this.walterLogoFile.getValue());
            Icon walterLogo = Icon.from(walterLogoFile);
            Helper.getWalterAccountManager().setAvatar(walterLogo).queue();
        } catch (IOException e) {
            Helper.logError("There was an exception when trying to change to the walterlogo defined " +
                    "in the " + name + " event:\n" + e.getMessage());
        }
    }

    private void changeToDefinedMemberColor() {
        assert memberColor.getValue() != null;
        BlackRole.MEMBER.setColor(memberColor.getValue());
    }

    public String toString() {
        return    "name:         " + getName() +
                "\nstart date:   " + (hasStartDate() ? startDate : "Undefined") +
                "\nmember color: " + (memberColor.hasValue() ? memberColor.getValueString() : "DEFAULT") +
                "\nserver logo:  " + (serverLogoFile.hasValue() ? serverLogoFile.getValueString() : "DEFAULT") +
                "\nwalter logo:  " + (walterLogoFile.hasValue() ? walterLogoFile.getValueString() : "DEFAULT");
    }
}
