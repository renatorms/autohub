public abstract class Veiculo {
    private String marca;
    private String modelo;
    private int ano;
    private String cor;
    private String placa;
    private int quilometragem;

    public Veiculo(String marca, String modelo, String cor, String placa, int ano, int quilometragem) {
        this.marca = marca;
        this.modelo = modelo;
        this.cor = cor;
        this.placa = placa;
        this.ano = ano;
        this.quilometragem = quilometragem;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getCor() {
        return cor;
    }

    public String getPlaca() {
        return placa;
    }

    public int getAno() {
        return ano;
    }

    public int getQuilometragem() {
        return quilometragem;
    }

    public abstract double calcularPreco();
}
