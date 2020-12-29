package Walter.Settings;


import Walter.Config;
import Walter.EventScheduler;
import Walter.Helper;
import Walter.entities.BlackRole;
import Walter.exceptions.ReasonedException;
import net.dv8tion.jda.api.entities.Icon;

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
        changeServerLogo();
        changeWalterLogo();
        changeMemberColor();
        EventScheduler.instance.executionNotify(this);
    }

    private void changeServerLogo() {
        try {
            File serverLogoFile;
            if (hasServerLogoFile())
                serverLogoFile = new File(this.serverLogoFile.getValue());
            else if (Config.defaultServerLogoFile.hasValue())
                serverLogoFile = new File(Config.defaultServerLogoFile.getValue());
            else {
                Helper.logError("There was neither a serverlogofile defined in the event " +
                        name + " nor in the defaults. No changes were made");
                return;
            }

            Icon serverLogo = Icon.from(serverLogoFile);
            Helper.getGuildManager().setIcon(serverLogo).queue();
        } catch (IOException e) {
            Helper.logError("There was an exception when trying to change to the serverlogo defined " +
                    "in the " + name + " event:\n" + e.getMessage());
        }
    }

    private void changeWalterLogo() {
        try {
            File walterLogoFile;
            if (hasWalterLogoFile())
                walterLogoFile = new File(this.walterLogoFile.getValue());
            else if (Config.defaultWalterLogoFile.hasValue())
                walterLogoFile = new File(Config.defaultWalterLogoFile.getValue());
            else {
                Helper.logError("There was neither a walterlogofile defined in the event " +
                        name + " nor in the defaults. No changes were made");
                return;
            }

            Icon walterLogo = Icon.from(walterLogoFile);
            Helper.getWalterAccountManager().setAvatar(walterLogo).queue();
        } catch (IOException e) {
            Helper.logError("There was an exception when trying to change to the walterlogo defined " +
                    "in the " + name + " event:\n" + e.getMessage());
        }
    }

    private void changeMemberColor() {
        Color toSet;
        if (hasMemberColor())
            toSet = memberColor.getValue();
        else if (Config.defaultMemberColor.hasValue())
            toSet = Config.defaultMemberColor.getValue();
        else {
            Helper.logError("There was neither a memberColor defined in the event " +
                    name + " nor in the defaults. No changes were made");
            return;
        }

        if (!BlackRole.MEMBER.getColor().equals(toSet))
            BlackRole.MEMBER.setColor(toSet);
    }

    public String toString() {
        return    "name:         " + getName() +
                "\nstart date:   " + (hasStartDate() ? startDate : "Undefined") +
                "\nmember color: " + (memberColor.hasValue() ? memberColor.getValueString() : "DEFAULT") +
                "\nserver logo:  " + (serverLogoFile.hasValue() ? serverLogoFile.getValueString() : "DEFAULT") +
                "\nwalter logo:  " + (walterLogoFile.hasValue() ? walterLogoFile.getValueString() : "DEFAULT");
    }
}
