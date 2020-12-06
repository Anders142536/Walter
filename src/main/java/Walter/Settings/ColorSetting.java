package Walter.Settings;

import Walter.exceptions.ReasonedException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;

public class ColorSetting extends Setting {
    Color value;

    @Override
    public void setValue(@Nonnull String value) throws ReasonedException {
                        //# followed by 6 hexadecimal digits 0-9 and a-f, both upper and lowercase
        if (!value.matches("^#[0-9a-fA-F]{6}$"))
            throw new ReasonedException("The color value has to be given in hexadecimal format with " +
                    "no alpha channel like this: #00ffad");
        try {
            this.value = Color.decode(value);
        } catch (NumberFormatException e) {
            throw new ReasonedException("Something went wrong when trying to understand the " +
                    "given color \"" + value + "\". It has to be in hexadecimal format with " +
                    "no alpha channel like this: #00ffad\n" + e.getMessage());
        }
    }

    @Override
    public boolean hasValue() { return value != null; }

    @Nullable
    public Color getValue() { return value; }

    @Override @Nonnull
    public String getValueString() {
        return (hasValue() ?    // #0000ff style hex notation
                String.format("#%02x%02x%02x", value.getRed(), value.getGreen(), value.getBlue()) :
                "Undefined");
    }
}
