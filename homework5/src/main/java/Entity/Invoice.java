package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    private @NotNull int id;
    private @NotNull Timestamp date;
    private @NotNull int organizationSender;

}
