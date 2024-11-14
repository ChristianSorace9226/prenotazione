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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CustomResponse{");
        sb.append("errorMessage='").append(errorMessage).append('\'');
        sb.append(", result=").append(result);
        sb.append(", response=").append(response);
        sb.append('}');
        return sb.toString();
    }

    public static <T> CustomResponse<T> success(T response) {
        return new CustomResponse<>(0, response, "");
    }

    public static <T> CustomResponse<T> error(int result, String errorMessage) {
        return new CustomResponse<>(result, null, errorMessage);
    }
}
