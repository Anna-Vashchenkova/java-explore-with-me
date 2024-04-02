package ru.practicum.ewm_main_service.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    Long id;
    @Email(message = "Email должен быть корректным адресом электронной почты")
    @NotBlank(message = "Email не должен быть пустым")
    private String email;
    @NotBlank(message = "Имя пользователя не должно быть пустым")
    private String name;
}
