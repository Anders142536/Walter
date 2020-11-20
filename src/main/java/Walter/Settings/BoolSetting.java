package Walter.Settings;

/**
 * Doesn't have a default yet as booleans are false by default
 * If there is the case that true has to be the default it can easily be added
 */
public class BoolSetting extends Setting {
    boolean value;

    public void setValue(boolean value) { this.value = value; }

    public void setValue(String value) {
        assert(value.equals("true") || value.equals("false"));

        boolean parsedValue = Boolean.parseBoolean(value);
        this.value = parsedValue;
    }

    public boolean getValue() { return value; }
}
