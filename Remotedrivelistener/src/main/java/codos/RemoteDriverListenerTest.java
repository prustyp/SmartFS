
package codos;


import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import org.junit.Test;

public class RemoteDriverListenerTest {
    @Test
    public void testRemoteDriverListener() throws InvalidKeyException, MalformedURLException {
      RemoteDriveListener remoteDriveListener = new RemoteDriveListener();
      System.out.println(remoteDriveListener.containerSize + "");
    }


}

