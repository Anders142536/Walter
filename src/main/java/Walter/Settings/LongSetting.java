package Walter.Settings;

import Walter.exceptions.ReasonedException;

public class LongSetting extends Setting {
    Long value;

    Long defaultValue;

    final long upperLimit;
    final long lowerLimit;

    public LongSetting() {
        upperLimit = Long.MAX_VALUE;
        lowerLimit = Long.MIN_VALUE;
    }

    public LongSetting(long upperLimit, long lowerLimit) {
        assert(upperLimit > lowerLimit);
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }

    public void setDefault(long defaultValue) throws ReasonedException {
        if (defaultValue >= lowerLimit && defaultValue <= upperLimit) this.defaultValue = defaultValue;
        else throw new ReasonedException(new String[] {
                "The value has to be within the limits of " +  lowerLimit + " and " + upperLimit,
                "Der Wert muss zwischen " + lowerLimit + " und " + upperLimit + " liegen"
        });
    }

    public void setValue(String value) throws ReasonedException {
        long parsedValue = Long.parseLong(value);
        if (parsedValue >= lowerLimit && parsedValue <= upperLimit) this.value = parsedValue;
        else throw new ReasonedException(new String[] {
                "The value has to be within the limits of " + lowerLimit + " and " + upperLimit,
                "Der Wert muss zwischen " + lowerLimit + " und " + upperLimit + " liegen"
        });
    }

    public long getValue() {
        assert(value != null || defaultValue != null);
        return (value == null ? defaultValue : value);
    }
}
