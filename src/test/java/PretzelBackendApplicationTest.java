
import com.pretzel.backend.PretzelBackendApplication;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PretzelBackendApplicationTest {

    private PretzelBackendApplication underTest;

    @Test
    void testMainShouldRunSpringApplicationWhenCalled() {
        underTest = new PretzelBackendApplication();

        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {

            String[] args = {};
            assertDoesNotThrow(() -> PretzelBackendApplication.main(args));

            mocked.verify(() -> SpringApplication.run(PretzelBackendApplication.class, args), Mockito.times(1));
        }
    }
}
