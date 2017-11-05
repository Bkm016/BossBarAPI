package org.inventivetalent.apihelper.exception;

public class MissingHostException extends RuntimeException
{
    public MissingHostException() {
    }
    
    public MissingHostException(final String message) {
        super(message);
    }
    
    public MissingHostException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public MissingHostException(final Throwable cause) {
        super(cause);
    }
    
    public MissingHostException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
