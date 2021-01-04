package Walter.Parsers;

import Walter.Config;
import Walter.exceptions.ParseException;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class DateTimeOption extends Option {
    private LocalDateTime value = null;

    public DateTimeOption(@Nonnull String[] name, String[] description) {
        super(OptionType.DATETIME, name, description);
    }

    public DateTimeOption(@Nonnull String[] name, String[] description, boolean required) {
        super(OptionType.DATETIME, name, description, required);
    }

    @Override
    public void setValue(String argument) throws ParseException {
        try {
            value = LocalDateTime.parse(argument.replace('.', '/'), Config.dateFormat);
        } catch (DateTimeParseException e) {
            throw new ParseException(new String[] {
                    "Something went wrong when trying to understand your given date:\n" + e.getMessage(),
                    "Etwas ist beim Verstehen des gegebenen Datums fehl geschlagen:\n" + e.getMessage()
            });
        }
    }

    @Override
    public boolean hasValue() {
        return value != null;
    }

    public LocalDateTime getValue() {
        return value;
    }

    @Override
    public void reset() {
        value = null;
    }
}
