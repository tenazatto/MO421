UNIVERSIDADE ESTADUAL DE CAMPINAS
Instituto de Computação – IC
MO421 – Introdução a Criptografia
Prof. Dr. Diego de Freitas Aranha

Projeto 2

Aluno: Thales Eduardo Nazatto
RA: 074388

Neste pacote estão incluídos:

- O relatório contendo detalhes do desenvolvimento e resultados, em formato PDF.
- A pasta "code", contendo os códigos feitos durante o desenvolvimento do projeto (C).
- A pasta "images", contendo as imagens cifradas, amostras e imagens para detalhes adicionais.

Instruções:

Para compilação, foi feito um Makefile. Com isso, a compilação será feita com o comando "make image-cipher mostlyclean" na pasta onde estão os códigos.

Para execução:

Cifra Vigenere: O código base é o mesmo do que foi passado. Para executá-la são as mesmas instruções.

Cifra Affine:
Cifrar: ./image-cipher -e -a input.ppm output.ppm
Decifrar: ./image-cipher -d -a input.ppm output.ppm

Cifra TEA no modo ECB:
Cifrar: ./image-cipher -e -t input.ppm output.ppm (ou ./image-cipher -e -te input.ppm output.ppm)
Decifrar: ./image-cipher -d -t input.ppm output.ppm (ou ./image-cipher -d -te input.ppm output.ppm)

Cifra TEA no modo CFB:
Cifrar: ./image-cipher -e -tc input.ppm output.ppm
Decifrar: ./image-cipher -d -tc input.ppm output.ppm

Bom divertimento! =D
