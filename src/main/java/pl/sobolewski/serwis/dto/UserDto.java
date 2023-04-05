package pl.sobolewski.serwis.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.jetbrains.annotations.NotNull;

@Data
public class UserDto {
    // trzeba uwazac z validacjami poniewaz jezeli wymagane jest jedno pole to w kazdej metodzie bedzie wymaga np w buliderze
    //  @NotBlank
    // @Length(min = 3)
    private String username;
    //@Length(min = 10)
    private String password;
    // @Email
    private String email;
}
