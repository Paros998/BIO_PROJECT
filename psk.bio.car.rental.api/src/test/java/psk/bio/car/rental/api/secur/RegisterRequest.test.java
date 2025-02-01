package psk.bio.car.rental.api.secur;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import psk.bio.car.rental.api.security.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegisterRequestTest {

    @Test
void shouldCreateRegisterRequestWithValidUsernameAndPassword() {
    // Given
    String username = "testUser";
    String password = "testPassword";

    // When
    RegisterRequest registerRequest = new RegisterRequest(username, password);

    // Then
    assertNotNull(registerRequest);
    assertEquals(username, registerRequest.getUsername());
    assertEquals(password, registerRequest.getPassword());
}
@Test
void shouldThrowNullPointerExceptionWhenUsernameIsNull() {
    // Given
    String password = "testPassword";

    // When & Then
    assertThrows(NullPointerException.class, () -> {
        new RegisterRequest(null, password);
    });
}
@Test
void shouldThrowNullPointerExceptionWhenPasswordIsNull() {
    // Given
    String username = "testUser";

    // When & Then
    assertThrows(NullPointerException.class, () -> {
        new RegisterRequest(username, null);
    });
}
@Test
void shouldCreateEqualRegisterRequestObjectsWithSameUsernameAndPassword() {
    // Given
    String username = "testUser";
    String password = "testPassword";

    // When
    RegisterRequest request1 = new RegisterRequest(username, password);
    RegisterRequest request2 = new RegisterRequest(username, password);

    // Then
    assertEquals(request1, request2);
    assertEquals(request1.hashCode(), request2.hashCode());
}
@Test
void shouldCreateUnequalRegisterRequestObjectsWithDifferentUsernames() {
    // Given
    String username1 = "testUser1";
    String username2 = "testUser2";
    String password = "testPassword";

    // When
    RegisterRequest request1 = new RegisterRequest(username1, password);
    RegisterRequest request2 = new RegisterRequest(username2, password);

    // Then
    assertNotEquals(request1, request2);
    assertNotEquals(request1.hashCode(), request2.hashCode());
}
@Test
void shouldVerifySuperBuilderGeneratesBuilderWithAllFields() {
    // Given
    String username = "testUser";
    String password = "testPassword";

    // When
    RegisterRequest registerRequest = RegisterRequest.builder()
            .username(username)
            .password(password)
            .build();

    // Then
    assertNotNull(registerRequest);
    assertEquals(username, registerRequest.getUsername());
    assertEquals(password, registerRequest.getPassword());
}
@Test
void shouldVerifyHashCodeReturnsTheSameValueForEqualObjects() {
    // Given
    String username = "testUser";
    String password = "testPassword";
    RegisterRequest request1 = new RegisterRequest(username, password);
    RegisterRequest request2 = new RegisterRequest(username, password);

    // When
    int hashCode1 = request1.hashCode();
    int hashCode2 = request2.hashCode();

    // Then
    assertEquals(hashCode1, hashCode2);
}
@Test
void shouldVerifyHashCodeReturnsDifferentValuesForObjectsWithDifferentPasswords() {
    // Given
    String username = "testUser";
    String password1 = "password1";
    String password2 = "password2";
    RegisterRequest request1 = new RegisterRequest(username, password1);
    RegisterRequest request2 = new RegisterRequest(username, password2);

    // When
    int hashCode1 = request1.hashCode();
    int hashCode2 = request2.hashCode();

    // Then
    assertNotEquals(hashCode1, hashCode2);
}
}
