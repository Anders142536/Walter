package Walter.Settings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StringSetting extends Setting {
    String value;

    @Override
    public void setValue(@Nonnull String value) {
        this.value = value;
    }

    @Override
    public boolean hasValue() { return value != null; }

    @Nullable
    public String getValue() { return value; }

    @Override @Nonnull
    public String getValueString() {
        return (value == null ? "Undefined" : value);
    }
}
