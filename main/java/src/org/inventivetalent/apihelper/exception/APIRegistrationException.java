package org.inventivetalent.apihelper.exception;

public class APIRegistrationException extends RuntimeException
{
    public APIRegistrationException() {
    }
    
    public APIRegistrationException(final String message) {
        super(message);
    }
    
    public APIRegistrationException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public APIRegistrationException(final Throwable cause) {
        super(cause);
    }
    
    public APIRegistrationException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
