package fr.pc3r.jetrouvemonjob.beans;

public class NoBodyAvailableException extends Exception {
    public NoBodyAvailableException() {
    }

    public NoBodyAvailableException(String message) {
        super(message);
    }

    public NoBodyAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
