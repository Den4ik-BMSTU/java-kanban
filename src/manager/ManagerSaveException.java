package manager;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message, IOException e){
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
