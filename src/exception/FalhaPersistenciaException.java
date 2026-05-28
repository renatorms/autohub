package exception;

public class FalhaPersistenciaException extends Exception {
    public FalhaPersistenciaException(String mensagem) {
        super(mensagem);
    }

    public FalhaPersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}