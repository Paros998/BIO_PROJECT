package psk.bio.car.rental.api.errors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse implements Serializable {
    public static final String SERVER_ERROR_CODE = "general-server-error";
    public static final String BUSINESS_ERROR_CODE = "business-error";

    private int status;
    private String code;
    private String businessError;
}
