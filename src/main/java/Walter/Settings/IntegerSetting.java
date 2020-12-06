package Walter.Settings;

import Walter.exceptions.ReasonedException;

import javax.annotation.Nullable;

public class IntegerSetting extends Setting {
    private Integer value;

    private Integer defaultValue;
    private final int upperLimit;
    private final int lowerLimit;

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
                "The value has to be within the limits of " + lowerLimit + " and " + upperLimit,
                "Der Wert muss zwischen " + lowerLimit + " und " + upperLimit + " liegen"
        });
    }

    public void setValue(String value) throws ReasonedException {
        int parsedValue = Integer.parseInt(value);
        if (parsedValue >= lowerLimit && parsedValue <= upperLimit) this.value = parsedValue;
        else throw new ReasonedException(new String[] {
                "The value has to be within the limits of " + lowerLimit + " and " + upperLimit,
                "Der Wert muss zwischen " + lowerLimit + " und " + upperLimit + " liegen"
        });
    }

    @Override
    public boolean hasValue() {
        return value != null;
    }

    @Nullable
    public Integer getValue() {
        return (hasValue() ? value : defaultValue);
    }

    @Override
    public String getValueString() {
        Integer toReturn = getValue();
        return (toReturn == null ? "Undefined" : String.valueOf(toReturn));
    }
}
