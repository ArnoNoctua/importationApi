package arnnus.importationapi.mappers;

import arnnus.importationapi.domain.User;
import dtos.SignUpDto;
import dtos.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target =  "password", ignore = true)
    User signUptoUser(SignUpDto userDto);


}
