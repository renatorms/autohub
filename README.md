# AutoHub

Sistema desktop desenvolvido em Java para gerenciamento de uma revenda de veículos.

O projeto foi desenvolvido como Trabalho Final da disciplina de Programação Orientada a Objetos, utilizando os conceitos de herança, classe abstrata, interfaces, exceções próprias, coleção polimórfica, manipulação de arquivos, arquitetura em camadas e interface gráfica com Swing.

## Funcionalidades

- Cadastro de carros, motos e caminhões
- Listagem de veículos cadastrados
- Busca por palavra-chave
- Edição de veículos
- Remoção de veículos
- Registro de venda
- Retorno de veículo ao estoque
- Simulação de parcelas
- Salvamento automático dos dados em arquivo
- Carregamento automático dos dados ao abrir o sistema

## Estrutura do Projeto

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
