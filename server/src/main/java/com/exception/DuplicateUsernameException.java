package com.exception;

/**
 * @author Dominic
 * @since 23-Oct-16
 * Website: www.dominicheal.com
 * Github: www.github.com/DomHeal
 */
public class DuplicateUsernameException extends Exception {
    public DuplicateUsernameException(String message){
        super(message);
    }
}
