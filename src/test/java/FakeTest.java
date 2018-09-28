import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

// Initial fake tests just to make sure your installation is working properly
public class FakeTest {

    @Test
    public void testFakeFunc() {
        final boolean myFlag = true;
        assertThat(myFlag, is(true));
    }
}
