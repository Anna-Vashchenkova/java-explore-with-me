package ru.practicum.ewm_main_service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.exception.ValidationException;
import ru.practicum.ewm_main_service.user.dto.UserDto;
import ru.practicum.ewm_main_service.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/admin/users")
    public List<UserDto> getUsers(
            @RequestParam (required = false) List<Long> ids,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        log.info("Получен запрос на получение информации о пользователях");
        if ((from < 0) || (size < 1)) {
            throw new ValidationException("Неверные параметры запроса");
        }
        return userService.getUsers(ids, from, size);
    }

    @PostMapping("admin/users")
    public UserDto addNewUser(@RequestBody UserDto dto) {
        return userService.addNewUser(dto);
    }
}
