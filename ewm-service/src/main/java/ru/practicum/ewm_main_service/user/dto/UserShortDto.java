package ru.practicum.ewm_main_service.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserShortDto {
    @NotNull
    Long id;
    @Email(message = "Email должен быть корректным адресом электронной почты")
    @NotBlank(message = "Email не должен быть пустым")
    private String email;
}
