package Walter;


import java.io.File;

//generic parent test that has some functionality in it
public class WalterTest {

    public File getTestfile(String filename) {

        System.out.println(WalterTest.class.getProtectionDomain().getCodeSource().getLocation());
        return null;
    }
}
