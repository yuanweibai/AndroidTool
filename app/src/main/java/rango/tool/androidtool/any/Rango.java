package rango.tool.androidtool.any;

import androidx.annotation.Nullable;

public class Rango {

    public String name;

    public Rango(String name) {
        this.name = name;

    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Rango) {
            return this.name.equals(((Rango) obj).name);
        }
        return false;
    }
}
