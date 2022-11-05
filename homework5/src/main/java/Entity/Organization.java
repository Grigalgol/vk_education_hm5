package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization {

    private @NotNull String name;
    private @NotNull int INN;
    private @NotNull int paymentAccount;

}
