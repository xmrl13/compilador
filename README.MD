# Analisador Léxico e Sintático para uma Linguagem Personalizada

Este projeto implementa um analisador léxico e sintático para uma linguagem personalizada utilizando **Java**. O analisador léxico foi gerado com o auxílio da ferramenta **JFLEX**, baseado no arquivo `lexer.jflex`. O analisador sintático foi implementado manualmente com base em uma gramática revisada.

## Problemas Resolvidos pela Gramática Revisada

A gramática original apresentava os seguintes problemas:
- **Recursão à esquerda**, que dificultava sua utilização com analisadores sintáticos do tipo LL(1).
- **Ambiguidade em identificadores**, dificultando a diferenciação entre palavras-chave e identificadores.

Esses problemas foram corrigidos na gramática revisada, que está disponível no arquivo [`gramatica-final.pdf`](./src/gramatica-final.pdf).

## Compilação

Para compilar o projeto, siga os passos abaixo:

1. Navegue até o diretório `/src` no terminal.
2. Execute o comando a seguir para compilar os arquivos necessários:
   ```bash
   javac AnalisadorLexico.java AnalisadorSintatico.java Token.java TokenUtils.java
