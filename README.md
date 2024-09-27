# Computação Gráfica - Trabalho Prático

Este repositório contém o trabalho prático desenvolvido para a disciplina de **Computação Gráfica** no segundo semestre na **PUC Minas**, por **Leon Junio Martins Ferreira**.

## Descrição do Projeto

O projeto foi desenvolvido utilizando **JavaFX** (versão 22) com **JDK 17**, e apresenta uma interface gráfica moderna construída com a biblioteca **AtlantaFX**. O objetivo principal é implementar e demonstrar conceitos fundamentais de computação gráfica, como transformações geométricas 2D, rasterização e recorte.

## Funcionalidades Implementadas

### Transformações Geométricas 2D
O projeto permite ao usuário aplicar as seguintes transformações geométricas sobre os objetos desenhados:
- **Translação**
- **Rotação**
- **Escala**
- **Reflexões** (nos eixos X, Y e XY)

Os fatores de transformação são definidos pelo usuário via interface gráfica.

### Rasterização
- **Retas:**
  - Algoritmo **DDA**
  - Algoritmo **Bresenham**
  
- **Circunferências:**
  - Algoritmo **Bresenham**

### Recorte (Clipping)
- **Algoritmo de Cohen-Sutherland** para regiões codificadas
- **Algoritmo de Liang-Barsky** com equação paramétrica

### Estrutura de Dados
A estrutura de dados suporta:
- **Vértices/Pontos**
- **Retas**
- **Polígonos**

Os objetos desenhados podem ser selecionados utilizando uma área de seleção retangular, indicada diretamente pela interface gráfica através de cliques na Área de Desenho, sem necessidade de interações via teclado.

## Instalação

O instalador do projeto já contém todos os conteúdos necessários, incluindo o **JavaFX**. Para executar, basta seguir as instruções fornecidas pelo instalador.

### Requisitos:
- **JDK 17**
- **JavaFX 22**

## Executando o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/leon-junio/computer_graphics_pratical_1.git
2. Para executar utilize o maven com o comando:
   ```bash
   mvn clean javafx:run
   ```
3. Ou crie sua build com arquivo JAR usando o maven:
   ```bash
   mvn clean package
   java -jar target/computer_graphics-1.0.jar
    ```# computer_graphics_pratical_1
