package exception;

public class ManagerSaveException extends RuntimeException { //с тестами на эксепшен пока не разобрался до конца

    public ManagerSaveException(String message) {
        super(message);
    }
}