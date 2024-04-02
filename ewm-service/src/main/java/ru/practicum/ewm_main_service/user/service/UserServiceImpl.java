package ru.practicum.ewm_main_service.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main_service.user.User;
import ru.practicum.ewm_main_service.user.dto.UserDto;
import ru.practicum.ewm_main_service.user.dto.UserMapper;
import ru.practicum.ewm_main_service.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        List<User> users;
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        if (ids == null) {
            users = repository.findAll(PageRequest.of(from, size, sortById)).getContent();
        } else {
            users = repository.findAllByIdIn(ids, PageRequest.of(from, size, sortById));
        }
        log.info("Number of users: {}", users.size());
        return UserMapper.toUserDtoList(users);
    }
}
