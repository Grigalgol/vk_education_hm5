package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {

    private @NotNull int id;
    private @NotNull int idInvoice;
    private @NotNull int productCode;
    private @NotNull int price;
    private @NotNull int count;

}
