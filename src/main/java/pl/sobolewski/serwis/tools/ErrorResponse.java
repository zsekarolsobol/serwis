package pl.sobolewski.serwis.tools;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Data
@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private int status;
    private String error;
}
