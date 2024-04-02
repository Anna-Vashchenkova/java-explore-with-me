package ru.practicum.ewm_main_service.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm_main_service.user.User;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class UserMapper {

    public static User toEntity(NewUserRequest dto) {
        return User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .build();
    }

    public static UserDto toUserDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }

    public static UserShortDto toUserShortDto(User entity) {
        return UserShortDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .build();
    }

    public static List<UserDto> toUserDtoList(List<User> users) {
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }


}
