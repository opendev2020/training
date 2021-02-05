package edu.cust.login;

/**
 * Created by qh on 2017/4/11.
 */
public class NoLoginException extends RuntimeException {
    public NoLoginException(){
        super();
    }

    public NoLoginException(String msg){
        super(msg);
    }
}
