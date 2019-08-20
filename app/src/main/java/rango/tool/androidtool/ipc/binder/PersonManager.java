package rango.tool.androidtool.ipc.binder;

import android.os.IInterface;

import java.util.List;

public interface PersonManager extends IInterface {

    void addPerson(Person person);

    List<Person> getPersonList();
}
