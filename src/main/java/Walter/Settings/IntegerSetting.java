package Walter.Settings;

import Walter.exceptions.ReasonedException;

public class IntegerSetting extends Setting {
    Integer value;

    Integer defaultValue;

    final int upperLimit;
    final int lowerLimit;

    public IntegerSetting() {
        upperLimit = Integer.MAX_VALUE;
        lowerLimit = Integer.MIN_VALUE;
    }

    public IntegerSetting(int upperLimit, int lowerLimit) {
        assert(upperLimit >= lowerLimit);
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }

    public void setDefault(int defaultValue) throws ReasonedException {
        if (defaultValue >= lowerLimit && defaultValue <= upperLimit) this.defaultValue = defaultValue;
        else throw new ReasonedException(new String[] {
                "The value has to be withing the limits of " + lowerLimit + " and " + upperLimit,
                "Der Wert muss zwischen " + lowerLimit + " und " + upperLimit + " liegen"
        });
    }

    public void setValue(String value) throws ReasonedException {
        int parsedValue = Integer.parseInt(value);
        if (parsedValue >= lowerLimit && parsedValue <= upperLimit) this.value = parsedValue;
        else throw new ReasonedException(new String[] {
                "The value has to be withing the limits of " + lowerLimit + " and " + upperLimit,
                "Der Wert muss zwischen " + lowerLimit + " und " + upperLimit + " liegen"
        });
    }

    public int getValue() {
        assert(value != null || defaultValue != null);
        return (value == null ? defaultValue : value);
    }
}
