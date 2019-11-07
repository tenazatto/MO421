#include <stdio.h>

#include "help.h"

void help_all() {
	help_intro();
	fprintf(stdout, "Usos:\n");
	fprintf(stdout, "\n");
	fprintf(stdout, "./otp-break ([-d] (-key | [-cipher]) | -help (-key | -cipher | [-all]))\n");
	fprintf(stdout, "\n");
	fprintf(stdout, "-d: Ativa escrita para depuração. Opcional\n");
	fprintf(stdout, "-key: Modo de descoberta da chave\n");
	fprintf(stdout, "-cipher: Modo de quebra da cifra por crib dragging. Opcional, opção padrão\n");
	fprintf(stdout, "-help/-help -all: Modo de ajuda\n");
	fprintf(stdout, "-help -key: Modo de ajuda para a descoberta da chave\n");
	fprintf(stdout, "-help -cipher: Modo de ajuda para a quebra da cifra\n");
}
void help_key() {
	help_intro();
	fprintf(stdout, "Modo de descoberta da chave\n");
	fprintf(stdout, "Usos:\n");
	fprintf(stdout, "\n");
	fprintf(stdout, "./otp-break -key plaintext-file ciphertext-file [key-file-generated]\n");
	fprintf(stdout, "\n");
	fprintf(stdout, "Entradas:\n");
	fprintf(stdout, "plaintext-file: Endereço do arquivo do texto plano\n");
	fprintf(stdout, "ciphertext-file: Endereço do arquivo do cifrotexto\n");
	fprintf(stdout, "key-file-generated: Nome do arquivo da chave a ser gerada. Opcional\n");
	fprintf(stdout, "\n");
	fprintf(stdout, "Saída:\n");
	fprintf(stdout, "Será gerado um arquivo em uma pasta de nome keys. Se a pasta não existir, a mesma será criada.\n");
	fprintf(stdout, "Se o nome do arquivo não for informado em key-file-generated, o nome dele será key.txt como padrão.\n");
}
void help_cipher() {
	help_intro();
	fprintf(stdout, "Modo de quebra da cifra\n");
	fprintf(stdout, "Usos:\n");
	fprintf(stdout, "\n");
	fprintf(stdout, "./otp-break -cipher ciphertext-file-1 ciphertext-file-2 dictionary-file\n");
	fprintf(stdout, "\n");
	fprintf(stdout, "Entradas:\n");
	fprintf(stdout, "ciphertext-file-1: Endereço do arquivo do cifrotexto 1\n");
	fprintf(stdout, "ciphertext-file-2: Endereço do arquivo do cifrotexto 2\n");
	fprintf(stdout, "dictionary-file: Endereço do arquivo do dicionário de possíveis palavras\n");
	fprintf(stdout, "\n");
	fprintf(stdout, "Saída:\n");
	fprintf(stdout, "Serão gerados arquivos em uma pasta de nome cribs. Se a pasta não existir, a mesma será criada.\n");
	fprintf(stdout, "Os nomes dos arquivos terão o nome de cada uma das possíveis palavras.\n");
	fprintf(stdout, "Neles, estarão as possíveis saídas em diferentes posições iniciais do texto.\n");
}

void help_intro() {
	fprintf(stdout, "otp-break: Uso de Crib Dragging para quebra de duas cifras One-Time Pad\n");
	fprintf(stdout, "Disciplina MO421A - Professor Diogo de Freitas Aranha\n");
	fprintf(stdout, "Aluno 074388 - Thales Eduardo Nazatto\n");
}
