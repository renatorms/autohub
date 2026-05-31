# Relatório Técnico — AutoHub

**Aluno:** Renato Ramos
**Disciplina:** Programação Orientada a Objetos
**Projeto:** AutoHub — Sistema de Revenda de Veículos

## 1. Descrição do sistema

O AutoHub é um sistema desktop desenvolvido em Java para gerenciamento de uma revenda de veículos. O sistema permite cadastrar, listar, buscar, editar e remover veículos do estoque, além de registrar vendas, retornar veículos ao estoque e simular parcelas de financiamento.

A aplicação possui interface gráfica desenvolvida com Swing e utiliza persistência em arquivo para salvar e carregar os dados do estoque. O projeto foi organizado em camadas, separando as classes de domínio, regras de negócio, persistência, exceções próprias e interface gráfica.

## 2. Arquitetura do projeto

O projeto foi organizado nos seguintes pacotes:

```text
src/
├── model/
│   ├── Veiculo.java
│   ├── Carro.java
│   ├── Moto.java
│   ├── Caminhao.java
│   ├── Disponivel.java
│   └── Negociavel.java
│
├── service/
│   └── EstoqueService.java
│
├── persistence/
│   └── EstoqueRepository.java
│
├── exception/
│   ├── DadosInvalidosException.java
│   ├── FalhaPersistenciaException.java
│   ├── VeiculoIndisponivelException.java
│   └── VeiculoNaoEncontradoException.java
│
├── view/
│   └── AutoHubFrame.java
│
└── Main.java
```

O pacote `model` contém as classes principais do domínio do sistema. O pacote `service` concentra as regras de negócio. O pacote `persistence` é responsável por salvar e carregar os dados em arquivo. O pacote `exception` contém as exceções próprias do sistema. O pacote `view` contém a interface gráfica.

## 3. Diagrama UML simplificado

```text
                 <<abstract>>
                  Veiculo
 ------------------------------------------------
 - marca: String
 - modelo: String
 - ano: int
 - cor: String
 - placa: String
 - quilometragem: int
 - precoBase: double
 - vendido: boolean
 ------------------------------------------------
 + calcularPreco(): double
 + getTipo(): String
 + getDescricao(): String
 + podeSerVendido(): boolean
 + registrarVenda(): void
 + retornarAoEstoque(): void
 ------------------------------------------------
        /|\                 implements
         |----------------------------> Disponivel
         |
   -----------------------------
   |             |             |
 Carro         Moto        Caminhao
   |             |             |
 implements   implements    implements
   |             |             |
 Negociavel   Negociavel   Negociavel
```

```text
EstoqueService
------------------------------------------------
- estoque: List<Veiculo>
------------------------------------------------
+ adicionar(Veiculo): void
+ listarTodos(): List<Veiculo>
+ buscar(String): List<Veiculo>
+ editar(String, Veiculo): void
+ remover(String): void
+ registrarVenda(String): void
+ retornarAoEstoque(String): void
```

```text
EstoqueRepository
------------------------------------------------
+ salvar(List<Veiculo>, String): void
+ carregar(String): List<Veiculo>
```

## 4. Hierarquia de classes

A classe abstrata `Veiculo` representa o conceito geral de veículo no sistema. Ela possui atributos comuns, como marca, modelo, ano, cor, placa, quilometragem e preço base.

As classes `Carro`, `Moto` e `Caminhao` herdam de `Veiculo`. Cada uma possui atributos específicos:

- `Carro`: quantidade de portas e informação sobre câmbio automático;
- `Moto`: cilindradas e versão básica ou completa;
- `Caminhao`: capacidade de carga e quantidade de eixos.

A classe `Veiculo` possui métodos abstratos, como `calcularPreco()` e `getTipo()`. Cada subclasse implementa esses métodos de acordo com suas próprias regras.

## 5. Interfaces utilizadas

O sistema utiliza duas interfaces próprias:

### Interface `Disponivel`

A interface `Disponivel` representa comportamentos relacionados à disponibilidade do veículo no estoque. Ela define métodos como:

```java
boolean podeSerVendido();
void registrarVenda();
void retornarAoEstoque();
```

A classe `Veiculo` implementa essa interface, permitindo controlar se um veículo está disponível ou vendido.

### Interface `Negociavel`

A interface `Negociavel` representa comportamentos relacionados à negociação financeira de um veículo. Ela define os métodos:

```java
double calcularEntrada();
double calcularParcela(int quantidadeParcelas);
```

As classes `Carro`, `Moto` e `Caminhao` implementam essa interface, permitindo simular entrada e parcelas para diferentes tipos de veículos.

## 6. Coleção polimórfica

O sistema utiliza uma coleção polimórfica na classe `EstoqueService`:

```java
private List<Veiculo> estoque;
```

Essa lista permite armazenar objetos de diferentes subclasses, como `Carro`, `Moto` e `Caminhao`, dentro de uma única coleção do tipo `Veiculo`.

Isso permite aplicar polimorfismo no sistema, pois os veículos são tratados genericamente como `Veiculo`, mas cada objeto executa seu próprio comportamento ao chamar métodos como `calcularPreco()` e `getTipo()`.

## 7. Exceções próprias

O sistema possui exceções próprias para representar erros específicos do domínio:

- `DadosInvalidosException`: usada quando o usuário informa dados inválidos, como campos vazios, ano inválido, quilometragem negativa ou preço menor ou igual a zero.
- `VeiculoNaoEncontradoException`: usada quando uma busca, edição ou remoção não encontra nenhum veículo correspondente.
- `VeiculoIndisponivelException`: usada quando o usuário tenta vender um veículo que já foi vendido.
- `FalhaPersistenciaException`: usada quando ocorre erro ao salvar ou carregar os dados em arquivo.

Essas exceções tornam o tratamento de erros mais claro e relacionado às regras do sistema.

## 8. Exemplo de exceção em camadas

Um exemplo de propagação de exceção ocorre ao tentar vender um veículo já vendido.

Na camada `service`, o método `registrarVenda` verifica se o veículo pode ser vendido. Caso ele já esteja vendido, é lançada a exceção `VeiculoIndisponivelException`.

```java
if (!veiculo.podeSerVendido()) {
    throw new VeiculoIndisponivelException("Este veículo já foi vendido.");
}
```

Essa exceção não é tratada diretamente na camada de serviço. Ela é propagada para a camada `view`, onde é capturada por um bloco `try/catch` e exibida ao usuário por meio de uma janela de mensagem.

```java
catch (DadosInvalidosException | VeiculoNaoEncontradoException | VeiculoIndisponivelException erro) {
    mostrarErro("Erro ao registrar venda", erro.getMessage());
}
```

Esse fluxo demonstra a separação de responsabilidades entre as camadas: a camada de serviço aplica a regra de negócio, enquanto a interface gráfica trata a comunicação com o usuário.

## 9. Persistência em arquivo

A classe `EstoqueRepository`, localizada no pacote `persistence`, é responsável pela manipulação de arquivos. Ela possui métodos para salvar e carregar a lista de veículos.

O salvamento utiliza `ObjectOutputStream`, enquanto o carregamento utiliza `ObjectInputStream`. Os dados são armazenados no arquivo:

```text
dados/estoque.dat
```

O sistema também realiza carregamento automático dos dados ao abrir a aplicação e salvamento automático após operações que modificam o estoque, como cadastro, edição, remoção, registro de venda e retorno ao estoque.

## 10. Interface gráfica

A interface gráfica foi desenvolvida com Swing, na classe `AutoHubFrame`. A tela principal permite ao usuário realizar as principais operações do sistema:

- adicionar carro;
- adicionar moto;
- adicionar caminhão;
- buscar veículos;
- editar veículos;
- remover veículos;
- registrar venda;
- retornar veículo ao estoque;
- simular parcelas;
- salvar e carregar dados;
- listar todos os veículos.

A interface também exibe mensagens de erro e sucesso usando `JOptionPane`.

## 11. Prints da interface

Inserir nesta seção os prints da aplicação em funcionamento:

**Print 1 — Tela inicial do sistema**

[Inserir print da tela inicial]

**Print 2 — Cadastro de veículo**

[Inserir print do cadastro de carro, moto ou caminhão]

**Print 3 — Listagem de veículos**

[Inserir print mostrando veículos cadastrados]

**Print 4 — Tratamento de exceção**

[Inserir print da mensagem “Este veículo já foi vendido.” ou outra validação]

## 12. Conclusão

O desenvolvimento do AutoHub permitiu aplicar os principais conceitos de Programação Orientada a Objetos em Java. O sistema utiliza classe abstrata, herança, interfaces, polimorfismo, exceções próprias, coleção polimórfica, manipulação de arquivos e interface gráfica.

Além disso, a organização em camadas tornou o projeto mais claro e modular, separando as responsabilidades entre domínio, regras de negócio, persistência, exceções e interface gráfica.
