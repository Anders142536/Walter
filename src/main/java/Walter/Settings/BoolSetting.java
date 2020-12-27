package Walter.Settings;

import Walter.exceptions.ReasonedException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Doesn't have a default yet as booleans are false by default
 * If there is the case that true has to be the default it can easily be added
 */
public class BoolSetting extends Setting {
    Boolean value;

    public void setValue(boolean value) { this.value = value; }

    public void setValue(@Nonnull String value) throws ReasonedException {
        if (value.equals("Undefined")) this.value = null;
        else if (value.equals("true") || value.equals("false")) this.value = Boolean.parseBoolean(value);
        else throw new ReasonedException("Value must be either \"true\" or \"false\"");
    }

    @Override
    public boolean hasValue() {
        return value != null;
    }

    @Nullable
    public Boolean getValue() { return value; }

    @Override @Nonnull
    public String getValueString() {
        return (hasValue() ? String.valueOf(value) : "Undefined");
    }
}
