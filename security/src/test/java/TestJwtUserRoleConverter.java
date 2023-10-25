import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.aktin.broker.manager.security.conf.JwtUserRoleConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

class TestJwtUserRoleConverter {

  @Mock
  private Jwt mockJwt;

  @InjectMocks
  private JwtUserRoleConverter converter;

  @BeforeEach
  public void setUp() throws NoSuchFieldException, IllegalAccessException {
    MockitoAnnotations.openMocks(this);
    setPrivateField(converter, "rolesPrefix", "ROLE_");
    setPrivateField(converter, "rolesKeyChain", "resource_access.broker-manager-client.roles");
    mockJwtClaims(Map.of("resource_access",
        Map.of("broker-manager-client", Map.of("roles", List.of("ADMIN", "USER")))));
    converter.init();
  }

  private void setPrivateField(Object object, String fieldName, Object value)
      throws NoSuchFieldException, IllegalAccessException {
    Field field = object.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(object, value);
  }

  private void mockJwtClaims(Map<String, Object> claims) {
    Mockito.when(mockJwt.getClaims()).thenReturn(claims);
  }

  @Test
  void testExtractAuthorities() {
    AbstractAuthenticationToken result = converter.convert(mockJwt);
    Collection<GrantedAuthority> authorities = result.getAuthorities();
    Assertions.assertEquals(2, authorities.size());
    List<String> expectedRoles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
    for (GrantedAuthority authority : authorities) {
      Assertions.assertTrue(expectedRoles.contains(authority.getAuthority()));
    }
  }

  @Test
  void firstMapEntryIsMissing() {
    mockJwtClaims(Map.of("broker-manager-client", Map.of("roles", List.of("ADMIN", "USER"))));
    Assertions.assertThrows(IllegalArgumentException.class, () -> converter.convert(mockJwt),
        "Key 'resource_access' not found in claims.");
  }

  @Test
  void lastMapEntryIsMissing() {
    mockJwtClaims(
        Map.of("resource_access", Map.of("broker-manager-client", Map.of("selor", List.of()))));
    Assertions.assertThrows(IllegalArgumentException.class, () -> converter.convert(mockJwt),
        "Key 'roles' not found in claims.");
  }

  @Test
  void firstKeyIsMissing() throws NoSuchFieldException, IllegalAccessException {
    setPrivateField(converter, "rolesKeyChain", "broker-manager-client.roles");
    converter.init();
    try {
      converter.convert(mockJwt);
    } catch (IllegalArgumentException e) {
      Assertions.assertEquals("Key 'broker-manager-client' not found in claims.",
          e.getMessage());
    }
  }

  @Test
  void lastKeyIsMissing() throws NoSuchFieldException, IllegalAccessException {
    setPrivateField(converter, "rolesKeyChain", "resource_access.broker-manager-client");
    converter.init();
    try {
      converter.convert(mockJwt);
    } catch (IllegalArgumentException e) {
      Assertions.assertEquals(
          "Last key should contain a list or array of strings, but it does not.", e.getMessage());
    }
  }
}
