package org.inventivetalent.apihelper.exception;

public class HostRegistrationException extends RuntimeException
{
    public HostRegistrationException() {
    }
    
    public HostRegistrationException(final String message) {
        super(message);
    }
    
    public HostRegistrationException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public HostRegistrationException(final Throwable cause) {
        super(cause);
    }
    
    public HostRegistrationException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
