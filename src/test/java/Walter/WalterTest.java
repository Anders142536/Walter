package Walter;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

//generic parent test that has some functionality in it
public class WalterTest {

    @BeforeAll
    public static void prepare() {
                        // returns the path to the project ../git/walter
        Walter.location = System.getProperty("user.dir") + "/src/test/testfiles";
    }
}
