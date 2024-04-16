package ru.practicum.ewm_main_service.user.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

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
    @Length(min = 6, max = 254)
    @NotBlank(message = "Email не должен быть пустым")
    private String email;
    @NotBlank(message = "Имя пользователя не должно быть пустым")
    @Length(min = 2, max = 250)
    private String name;
}
