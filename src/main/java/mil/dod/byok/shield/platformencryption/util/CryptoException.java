package mil.dod.dha.byok.shield.platformencryption.util;

public class CryptoException extends Exception{

    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }

    public CryptoException(String s){
        super(s);
    }

    public CryptoException(Exception e){
        super(e);
    }

}
