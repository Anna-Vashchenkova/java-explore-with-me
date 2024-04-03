package ru.practicum.ewm_main_service.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm_main_service.user.User;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class UserMapper {

    public static User toEntityUser(NewUserRequest dto) {
        return User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }

    public static List<UserDto> toUserDtoList(List<User> users) {
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }


}
