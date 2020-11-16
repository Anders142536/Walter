package Walter.Settings;

import Walter.exceptions.ReasonedException;

import javax.annotation.Nonnull;

public class StringSetting extends Setting {
    String value;

    public void setValue(@Nonnull String value) {
        this.value = value;
    }

    @Nonnull
    public String getValue() {
        assert(value != null);
        return value;
    }
}
