package model;

import java.io.Serializable; //objetos salvos em arquivos

public abstract class Veiculo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String marca;
    private String modelo;
    private int ano;
    private String cor;
    private String placa;
    private int quilometragem;
    private double precoBase;

    public Veiculo(String marca, String modelo, String cor, String placa, int ano, int quilometragem,
            double precoBase) {
        this.marca = marca;
        this.modelo = modelo;
        this.cor = cor;
        this.placa = placa;
        this.ano = ano;
        this.quilometragem = quilometragem;
        this.precoBase = precoBase;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAno() {
        return ano;
    }

    public String getCor() {
        return cor;
    }

    public String getPlaca() {
        return placa;
    }

    public int getQuilometragem() {
        return quilometragem;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setQuilometragem(int quilometragem) {
        this.quilometragem = quilometragem;
    }

    public void setPrecoBase(double precoBase) {
        this.precoBase = precoBase;
    }

    public String getDescricao() {
        return marca + " " + modelo + " - " + ano + " - Placa: " + placa;
    }

    public abstract double calcularPreco();

    public abstract String getTipo();
}
