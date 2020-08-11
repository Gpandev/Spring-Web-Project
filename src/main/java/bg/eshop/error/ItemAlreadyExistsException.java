package bg.eshop.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Item exists.")
public class ItemAlreadyExistsException extends RuntimeException {

    private int statusCode;

    public ItemAlreadyExistsException() {
        this.statusCode = 409;
    }

    public ItemAlreadyExistsException(String message) {
        super(message);
        this.statusCode = 409;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
