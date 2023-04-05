package pl.sobolewski.serwis.mapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import pl.sobolewski.serwis.User;
import pl.sobolewski.serwis.dto.UserDto;
import pl.sobolewski.serwis.dto.UserEmailDto;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component

public class UserDtoMapper {

    public static List<UserEmailDto> mapUserToUserEmailDto(List<User> allUsers) {
        List<UserEmailDto> userEmailDtoList = allUsers.stream()
                .map(user -> new UserEmailDto((long) user.getId(), user.getEmail()))
                .collect(Collectors.toList());
        return userEmailDtoList;
    }

    public static User mapToUser(UserDto userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .build();
    }
}
