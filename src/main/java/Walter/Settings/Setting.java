package Walter.Settings;

import javax.annotation.Nonnull;

//parent class for all settings in case something shall be introduced they all share
//maybe checks wether or not it is given? a required flag?
public abstract class Setting {
    private String name;

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public String getName() { return (name == null ? "Unnamed" : name); }
}
