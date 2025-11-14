package mx.ine.sustseycae.models.requests;  
  
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;  
import lombok.ToString;  
  
@NoArgsConstructor  
@AllArgsConstructor  
@Getter  
@Setter  
@ToString  
public class DTOExpedientePorFolio {  
  
    @NotNull  
    @Positive  
    @Digits(integer = 10, fraction = 0)  
    private Integer folio;  
  
    @NotNull  
    @Positive  
    @Digits(integer = 2, fraction = 0)  
    private Integer idEstado;  
  
    @NotNull  
    @Positive  
    @Digits(integer = 2, fraction = 0)  
    private Integer idDistrito;  
  
    private Integer idAspirante;  
      
    private Integer idParticipacion;  
}