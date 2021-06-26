package kr.seok.admin.exception;

public class NotFoundDataException extends RuntimeException {
    public NotFoundDataException() {
    }

    public NotFoundDataException(String message) {
        super(message);
    }
}
