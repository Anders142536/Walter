package Walter.Parsers;

import Walter.entities.Language;
import Walter.exceptions.ParseException;

import javax.annotation.Nonnull;

/** Class representing Options.
 *
 */

public abstract class Option extends Argument {

    private final boolean required;
    private final String[] name;
    private final OptionType type;

    public Option(@Nonnull OptionType type, @Nonnull String[] name, String[] description) {
        this(type, name, description, true);
    }

    public Option(@Nonnull OptionType type, @Nonnull String[] name, String[] description, boolean required) {
        super(description);

        this.type = type;
        this.name = name;
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public OptionType getType() { return type; }

    public abstract boolean hasValue();

    public abstract void setValue(String argument) throws ParseException;

    public String getName(Language lang) {
        if (name.length <= lang.index) return name[0];
        return name[lang.index];
    }
}
