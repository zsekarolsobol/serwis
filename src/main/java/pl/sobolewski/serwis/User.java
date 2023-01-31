package pl.sobolewski.serwis;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.IdGeneratorType;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private String username;
    @NonNull
    private String password;
}
