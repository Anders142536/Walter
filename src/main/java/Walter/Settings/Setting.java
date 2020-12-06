package Walter.Settings;

import Walter.exceptions.ReasonedException;

import javax.annotation.Nonnull;

//parent class for all settings in case something shall be introduced they all share
//maybe checks wether or not it is given? a required flag?
public abstract class Setting {
    private String name;

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public abstract void setValue(String value) throws ReasonedException;

    public abstract boolean hasValue();

    public String getName() { return (name == null ? "Unnamed" : name); }

    public abstract String getValueString();
}
