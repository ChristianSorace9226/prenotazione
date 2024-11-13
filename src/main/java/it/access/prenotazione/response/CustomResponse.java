package it.access.prenotazione.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomResponse <T>{
    private int result;
    private T response;
    private String errorMessage;

    public CustomResponse(int result, T response, String errorMessage) {
        this.result = result;
        this.response = response;
        this.errorMessage = errorMessage;
    }

    public static <T> CustomResponse<T> success(T response) {
        return new CustomResponse<>(0, response, "");
    }

    public static <T> CustomResponse<T> error(int result, String errorMessage) {
        return new CustomResponse<>(result, null, errorMessage);
    }
}
