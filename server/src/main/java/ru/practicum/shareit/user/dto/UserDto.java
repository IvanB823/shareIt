package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Имя не должно быть пустым")
    private String name;

    @NotNull(message = "Email не должен быть null")
    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Email должен быть валидным")
    private String email;
}