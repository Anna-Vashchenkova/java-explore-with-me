package ru.practicum.ewm_main_service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.exception.ValidationException;
import ru.practicum.ewm_main_service.user.dto.UserDto;
import ru.practicum.ewm_main_service.user.service.UserService;

import javax.validation.Valid;
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
        log.info("Получен запрос на получение информации о пользователях по {} элементов на {} странице", size, from);
        if ((from < 0) || (size < 1)) {
            throw new ValidationException("Неверные параметры запроса");
        }
        return userService.getUsers(ids, from / size, size);
    }

    @PostMapping("admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addNewUser(@Valid @RequestBody UserDto dto) {
        log.info("Получен запрос на добавление нового пользователя с именем {}", dto.getName());
        UserDto userDto = userService.addNewUser(dto);
        log.info("Создан пользователь с ID={}", userDto.getId());
        return userDto;
    }

    @DeleteMapping("admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long userId) {
        log.info("Получен запрос - удалить данные пользователя с ID '{}'", userId);
        userService.deleteUserById(userId);
    }
}
