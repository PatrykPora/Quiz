package pl.elpepe.quiz.database.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@Entity(name = "PLAYERS")
public class PlayerEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    public PlayerEntity(String name) {
        this.name = name;
    }
}
