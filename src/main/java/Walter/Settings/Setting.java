package Walter.Settings;

import Walter.exceptions.ReasonedException;

public abstract class Setting {


    public Setting() {

    }

    public abstract void setValue(String value) throws ReasonedException;


}
