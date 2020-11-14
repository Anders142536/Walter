package Walter.Settings;

import Walter.exceptions.ReasonedException;

public class IntegerSetting extends Setting {
    Integer value;

    Integer defaultValue;

    int upperLimit;
    int lowerLimit;

    public IntegerSetting() {
        upperLimit = Integer.MAX_VALUE;
        lowerLimit = Integer.MIN_VALUE;
    }

    public IntegerSetting(int upperLimit, int lowerLimit) {
        assert(upperLimit >= lowerLimit);
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }

    public void setDefault(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setValue(String value) throws ReasonedException {
        int parsedValue = Integer.parseInt(value);
        if (parsedValue > lowerLimit && parsedValue < upperLimit) this.value = parsedValue;
        else throw new ReasonedException(new String[] {
                "The value has to be withing the limits of " + lowerLimit + " and " + upperLimit,
                "Der Wert muss zwischen " + lowerLimit + " und " + upperLimit + " liegen"
        });
    }
}
