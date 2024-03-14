package arnnus.importationapi.tests;

import arnnus.importationapi.domain.User;
import arnnus.importationapi.exceptions.AppException;
import arnnus.importationapi.mappers.UserMapper;
import arnnus.importationapi.repo.UserRepository;
import arnnus.importationapi.service.UserService;
import dtos.CredentialsDto;
import dtos.SignUpDto;
import dtos.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.CharBuffer;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;
    private UserDto userDto;
    private SignUpDto signUpDto;
    private CredentialsDto credentialsDto;

    @BeforeEach
    public void setUp() {
        user = new User(1L, "testUser", "testPassword", "testlogin", "testToken");
        userDto = new UserDto(1L, "testUser", "testPassword", "testlogin", "testToken");
        signUpDto = new SignUpDto("John", "Doe", "testUser", "testPassword".toCharArray());
        credentialsDto = new CredentialsDto("testUser", "testPassword".toCharArray());
    }

    @Test
    public void findByLogin_success() {
        when(userRepository.findByLogin(userDto.getLogin())).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto foundUser = userService.findByLogin(userDto.getLogin());

        assertEquals(userDto, foundUser);
    }

    @Test
    public void findByLogin_notFound() {
        when(userRepository.findByLogin(userDto.getLogin())).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> userService.findByLogin(userDto.getLogin()));
    }

    @Test
    public void login_success() {
        when(userRepository.findByLogin(credentialsDto.getLogin())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())).thenReturn(true);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto loggedInUser = userService.login(credentialsDto);

        assertEquals(userDto, loggedInUser);
    }

    @Test
    public void login_invalidPassword() {
        when(userRepository.findByLogin(credentialsDto.getLogin())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())).thenReturn(false);

        assertThrows(AppException.class, () -> userService.login(credentialsDto));
    }

    @Test
    public void testRegisterSuccess() {
        // Arrange
        String firstName = "testFirstName";
        String lastName = "testLastName";
        String login = "testLogin";
        String password = "testPassword";
        String token = "testToken";

        UserMapper userMapper = mock(UserMapper.class);
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        User user = new User();
        user.setId(1L);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));

        UserDto userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getLogin(), token);

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());
        when(userMapper.signUptoUser(any(SignUpDto.class))).thenReturn(user);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserService userService = new UserService(userRepository, userMapper, passwordEncoder);

        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setFirstName(firstName);
        signUpDto.setLastName(lastName);
        signUpDto.setLogin(login);
        signUpDto.setPassword(password.toCharArray());

        // Act
        UserDto result = userService.register(signUpDto);

        // Assert
        assertEquals(userDto, result);
    }

}

