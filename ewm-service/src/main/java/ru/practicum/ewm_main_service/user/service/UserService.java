package ru.practicum.ewm_main_service.user.service;

import ru.practicum.ewm_main_service.user.User;
import ru.practicum.ewm_main_service.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto addNewUser(UserDto dto);

    void deleteUserById(Long userId);

    User get(Long userId);

    Optional<User> findById(long userId);
}
