package test.psk.bio.car.rental.api.secur;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import psk.bio.car.rental.api.security.LoginRequest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginRequestTest {
    
    @Test
void shouldCreateValidLoginRequestObject() {
    // Given
    String username = "testUser";
    String password = "testPassword";

    // When
    LoginRequest loginRequest = new LoginRequest(username, password);

    // Then
    assertNotNull(loginRequest);
    assertEquals(username, loginRequest.getUsername());
    assertEquals(password, loginRequest.getPassword());
}
@Test
void shouldThrowNullPointerExceptionWhenCreatingLoginRequestWithNullUsername() {
    // Given
    String username = null;
    String password = "testPassword";

    // When & Then
    assertThrows(NullPointerException.class, () -> {
        new LoginRequest(username, password);
    });
}
@Test
void shouldThrowNullPointerExceptionWhenCreatingLoginRequestWithNullPassword() {
    // Given
    String username = "testUser";
    String password = null;

    // When & Then
    assertThrows(NullPointerException.class, () -> {
        new LoginRequest(username, password);
    });
}
@Test
void shouldReturnTrueForEqualsMethodWithSameUsernameAndPassword() {
    // Given
    String username = "testUser";
    String password = "testPassword";
    LoginRequest loginRequest1 = new LoginRequest(username, password);
    LoginRequest loginRequest2 = new LoginRequest(username, password);

    // When
    boolean areEqual = loginRequest1.equals(loginRequest2);

    // Then
    assertTrue(areEqual);
    assertEquals(loginRequest1.hashCode(), loginRequest2.hashCode());
}
@Test
void shouldReturnFalseForEqualsMethodWithDifferentUsernames() {
    // Given
    String username1 = "testUser1";
    String username2 = "testUser2";
    String password = "testPassword";
    LoginRequest loginRequest1 = new LoginRequest(username1, password);
    LoginRequest loginRequest2 = new LoginRequest(username2, password);

    // When
    boolean areEqual = loginRequest1.equals(loginRequest2);

    // Then
    assertFalse(areEqual);
    assertNotEquals(loginRequest1.hashCode(), loginRequest2.hashCode());
}
@Test
void shouldGenerateIdenticalHashCodeForTwoLoginRequestObjectsWithSameUsernameAndPassword() {
    // Given
    String username = "testUser";
    String password = "testPassword";
    LoginRequest loginRequest1 = new LoginRequest(username, password);
    LoginRequest loginRequest2 = new LoginRequest(username, password);

    // When
    int hashCode1 = loginRequest1.hashCode();
    int hashCode2 = loginRequest2.hashCode();

    // Then
    assertEquals(hashCode1, hashCode2);
}
@Test
void shouldGenerateDifferentHashCodeForTwoLoginRequestObjectsWithDifferentUsernameOrPassword() {
    // Given
    String username1 = "testUser1";
    String username2 = "testUser2";
    String password1 = "testPassword1";
    String password2 = "testPassword2";
    LoginRequest loginRequest1 = new LoginRequest(username1, password1);
    LoginRequest loginRequest2 = new LoginRequest(username2, password2);
    LoginRequest loginRequest3 = new LoginRequest(username1, password2);

    // When
    int hashCode1 = loginRequest1.hashCode();
    int hashCode2 = loginRequest2.hashCode();
    int hashCode3 = loginRequest3.hashCode();

    // Then
    assertNotEquals(hashCode1, hashCode2);
    assertNotEquals(hashCode1, hashCode3);
    assertNotEquals(hashCode2, hashCode3);
}
@Test
void shouldCorrectlyRepresentLoginRequestObjectAsString() {
    // Given
    String username = "testUser";
    String password = "testPassword";
    LoginRequest loginRequest = new LoginRequest(username, password);

    // When
    String result = loginRequest.toString();

    // Then
    assertTrue(result.contains("username=" + username));
    assertTrue(result.contains("password=" + password));
    assertTrue(result.startsWith("LoginRequest("));
    assertTrue(result.endsWith(")"));
}
}
