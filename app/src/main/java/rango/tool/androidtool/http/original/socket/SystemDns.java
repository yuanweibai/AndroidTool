package rango.tool.androidtool.http.original.socket;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public final class SystemDns {

    public static List<InetAddress> lookup(String hostname) throws UnknownHostException {
        if (hostname == null) throw new UnknownHostException("hostname == null");
        try {
            return Arrays.asList(InetAddress.getAllByName(hostname));
        } catch (NullPointerException e) {
            UnknownHostException unknownHostException =
                    new UnknownHostException("Broken system behaviour for dns lookup of " + hostname);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }
}
