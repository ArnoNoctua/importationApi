package arnnus.importationapi.tests;

import arnnus.importationapi.config.UserAuthenticationProvider;
import arnnus.importationapi.resource.AuthResource;
import arnnus.importationapi.service.UserService;
import dtos.CredentialsDto;
import dtos.SignUpDto;
import dtos.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthResourceTest {

    private final UserService userService = mock(UserService.class);
    private final UserAuthenticationProvider userAuthenticationProvider = mock(UserAuthenticationProvider.class);
    private final AuthResource authResource = new AuthResource(userService, userAuthenticationProvider);

    @Test
    public void testLogin() {
        CredentialsDto credentialsDto = new CredentialsDto();
        credentialsDto.setLogin("testUser");
        credentialsDto.setPassword("testPassword".toCharArray());

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("testUser");
        userDto.setLastName("testPassword");
        userDto.setToken("testToken");

        when(userService.login(credentialsDto)).thenReturn(userDto);
        when(userAuthenticationProvider.createToken(credentialsDto.getLogin())).thenReturn("testToken");

        ResponseEntity<UserDto> response = authResource.login(credentialsDto);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(userDto, response.getBody());
    }

    @Test
    public void testRegister() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setFirstName("testUser");
        signUpDto.setLastName("testPassword");
        signUpDto.setLogin("testUser");
        signUpDto.setPassword("testPassword".toCharArray());

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("testUser");
        userDto.setLastName("testPassword");
        userDto.setToken("testToken");

        when(userService.register(signUpDto)).thenReturn(userDto);
        when(userAuthenticationProvider.createToken(signUpDto.getLogin())).thenReturn("testToken");

        ResponseEntity<UserDto> response = authResource.register(signUpDto);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(userDto, response.getBody());
    }
}

