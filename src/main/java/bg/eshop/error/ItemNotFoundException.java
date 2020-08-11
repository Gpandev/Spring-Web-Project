package bg.eshop.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Item Not Found.")
public class ItemNotFoundException extends RuntimeException {

    private int statusCode;

    public ItemNotFoundException() {
        this.statusCode = 404;
    }


    public ItemNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
