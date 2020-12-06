package Walter.Settings;

import Walter.exceptions.ReasonedException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LongSetting extends Setting {
    Long value;

    Long defaultValue;

    final long upperLimit;
    final long lowerLimit;

    public LongSetting() {
        this(Long.MAX_VALUE, Long.MIN_VALUE);
    }

    public LongSetting(long upperLimit, long lowerLimit) {
        assert(upperLimit >= lowerLimit);
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

    @Override
    public void setValue(String value) throws ReasonedException {
        long parsedValue = Long.parseLong(value);
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
    public Long getValue() {
        return (hasValue() ? value : defaultValue);
    }

    @Override @Nonnull
    public String getValueString() {
        Long toReturn = getValue();
        return (toReturn == null ? "Undefined" : String.valueOf(toReturn));
    }
}
