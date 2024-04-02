package ru.practicum.ewm_main_service.user.service;

import ru.practicum.ewm_main_service.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(List<Long> ids, int from, int size);
}
