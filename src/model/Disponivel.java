package model;

public interface Disponivel {
    boolean podeSerVendido();

    void registrarVenda();

    void retornarAoEstoque();
}
