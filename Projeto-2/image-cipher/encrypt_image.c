/*
 * Image encryption tool.
 * 
 * Usage: encrypt_image [-e|-d] [-v|-a|-t] input_image output_image
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <stdint.h>

#include "ppm.h"
#include "tea.h"
#include "affine.h"
#include "math.h"

#define ECB_MODE 1
#define CFB_MODE 2

int block_cipher_mode = ECB_MODE;

/* Function prototypes. */
void vigenere(unsigned char *buf, int len);
void affine_enc(unsigned char *buf, int len);
void affine_dec(unsigned char *buf, int len);
void tea_enc(unsigned char *buf, int len);
void tea_dec(unsigned char *buf, int len);
unsigned char get_block_byte(uint32_t i, int pos);

/* Program arguments. */
FILE *input, *output;

/* Main program. */
int main(int argc, char *argv[]) {
	int encrypt = 0;
	pic image;

	/* Check number of arguments. */
	if (argc != 5) {
		fprintf(stderr,
				"\nUsage: image-cipher [-e|-d] [-v|-a|-t|-te|-to] input_image output_image\n");
		exit(1);
	}
	/* Check mode. */
	if (argv[1][0] != '-' || strlen(argv[1]) != 2) {
		fprintf(stderr, "\nUse -e to encrypt and -d to decrypt..\n");
		exit(1);
	}
	encrypt = (argv[1][1] == 'e');
	/* Check algorithm. */
	if (argv[2][0] != '-' || (strlen(argv[1]) != 2 && strlen(argv[1]) != 3)) {
		fprintf(stderr, "\nUnknown algorithm, use [-v|-a|-t|-te|-to].\n");
		exit(1);
	}
	/* Check files. */
	if ((input = fopen(argv[3], "rb")) == NULL) {
		fprintf(stderr, "\nNão é possível ler arquivo de entrada.\n");
		exit(1);
	}
	if ((output = fopen(argv[4], "wb")) == NULL) {
		fprintf(stderr, "\nNão é possível gravar em arquivo de saída.\n");
		exit(1);
	}

	if (ppm_read(input, &image) == 0) {
		fprintf(stderr, "\nNão é possível ler image de entrada.\n");
		exit(1);
	}

	/* Pass control to chosen algorithm. */
	switch (argv[2][1]) {
		case 'v':
			vigenere(image.pix, image.nx * image.ny * 3);
			break;
		case 'a':
			if (encrypt)
				affine_enc(image.pix, image.nx * image.ny * 3);
			else
				affine_dec(image.pix, image.nx * image.ny * 3);
			break;
		case 't':
			switch (argv[2][2]) {
				case 'c' :
					block_cipher_mode = CFB_MODE;
					break;
				case 'e':
					block_cipher_mode = ECB_MODE;
					break;
				default: block_cipher_mode = ECB_MODE;
			}
                        if (encrypt)
                                tea_enc(image.pix, image.nx * image.ny * 3);
                        else
                                tea_dec(image.pix, image.nx * image.ny * 3);
                        break;
	}

	if (ppm_write(output, &image) == 0) {
		fprintf(stderr, "\nNão é possível gravar image de saída.\n");
		exit(1);
	}

	fclose(input);
	fclose(output);
}

/* Implements the Vigenere cipher. */
void vigenere(unsigned char *buf, int len) {
	int i, length;
	unsigned int byte;
	unsigned char *key;

	printf("Key size: ");
	if (scanf("%d", &length) != 1) {
		fprintf(stderr, "\nInvalid key size.\n");
		exit(1);
	}
	key = (char *)malloc((length + 1) * (sizeof(char)));
	getchar();

	printf("Encryption key: ");
	for (i = 0; i < length; i++) {
		if (scanf("%u", &byte) != 1) {
			fprintf(stderr, "\nError reading encryption key.\n");
			exit(1);
		}
		key[i] = byte % 256;
	}
	key[length] = '\0';

	for (i = 0; i < len; i++) {
		buf[i] = buf[i] ^ key[i % length];
	}
	free(key);
}

void affine_enc(unsigned char *buf, int len) {
	int i;
	unsigned int byte;
	affine_key ak;

	printf("Encryption key: ");
	for (i = 0; i < 2; i++) {
		if (scanf("%u", &byte) != 1) {
			fprintf(stderr, "\nError reading encryption key.\n");
			exit(1);
		}

		if (byte < 0 || byte > 255) {
			fprintf(stderr, "\nInvalid encryption key. Interval of numbers should be between 0 and 255\n");
			exit(1);	
		}

		if (i == 0) {
			if (!coprimality(byte, 256)) {
				fprintf(stderr, "\nInvalid encryption key. First number must be co-prime with 256\n");
				exit(1);	
			}
			ak.a = byte;
		}
		else ak.b = byte;
	}

	for (i = 0; i < len; i++) {
		buf[i] = affine_encrypt(ak, buf[i], 256);
	}
}

void affine_dec(unsigned char *buf, int len) {
	int i;
	unsigned int byte;
	affine_key ak;

	printf("Decryption key: ");
	for (i = 0; i < 2; i++) {
		if (scanf("%u", &byte) != 1) {
			fprintf(stderr, "\nError reading decryption key.\n");
			exit(1);
		}

		if (byte < 0 || byte > 255) {
			fprintf(stderr, "\nInvalid decryption key. Interval of numbers should be between 0 and 255\n");
			exit(1);	
		}

		if (i == 0) {
			if (!coprimality(byte, 256)) {
				fprintf(stderr, "\nInvalid decryption key. First number must be co-prime with 256\n");
				exit(1);	
			}
			ak.a = byte;
		}
		else ak.b = byte;
	}


	for (i = 0; i < len; i++) {
		buf[i] = affine_decrypt(ak, buf[i], 256);
	}
}

void tea_enc(unsigned char *buf, int len) {
	int i,j;
	uint32_t byte;
	tea_key tk;
	uint32_t *block;

	block = (uint32_t *)malloc(2 * (sizeof(uint32_t)));
	block[0] = 0;
	block[1] = 0;

	printf("Encryption key: ");
	for (i = 0; i < 4; i++) {
		if (scanf("%x", &byte) != 1) {
			fprintf(stderr, "\nError reading encryption key.\n");
			exit(1);
		}
		tk.key[i] = byte;
	}

	//ECB Mode
	if (block_cipher_mode == ECB_MODE) {
		for (i = 0; i < len; i++) {
			block[(i % 8) / 4] += buf[i];
			if (i % 4 < 3) block[(i % 8) / 4] <<= 8;
			fprintf(stdout, "Pos. Buffer Atual: %d Tam. Total Buffer: %d Pos. Bloco: %d\n", i ,len, (i % 8) / 4);
			fprintf(stdout, "Byte do Buffer Atual: %d\n", buf[i]);
			fprintf(stdout, "Bloco: %x\n", block[(i % 8) / 4]);

			if(i % 8 == 7) {
				tea_encrypt(block, tk);
				fprintf(stdout, "Resultado cifracao: %x %x\n", block[0], block[1]);
				for(j = i - 7; j <= i; j++) {
					buf[j] = get_block_byte(block[(j % 8) / 4], j % 4);
				}
				block[0] = 0;
				block[1] = 0;
			}
		}
	} else { //CFB Mode
		uint32_t *ct_block;
		ct_block = (uint32_t *)malloc(2 * (sizeof(uint32_t)));
		ct_block[0] = 0;
		ct_block[1] = 0;

		printf("Initialization Vector (IV): ");
		for (i = 0; i < 2; i++) {
			if (scanf("%x", &byte) != 1) {
				fprintf(stderr, "\nError reading initialization vector.\n");
				exit(1);
			}
			block[i] = byte;
		}

		for (i = 0; i < len; i++) {
			ct_block[(i % 8) / 4] += buf[i];
			if (i % 4 < 3) ct_block[(i % 8) / 4] <<= 8;
			fprintf(stdout, "Pos. Buffer Atual: %d Tam. Total Buffer: %d Pos. Bloco: %d\n", i ,len, (i % 8) / 4);
			fprintf(stdout, "Byte do Buffer Atual: %d\n", buf[i]);
			fprintf(stdout, "Bloco: %x\n", ct_block[(i % 8) / 4]);

			if(i % 8 == 7) {
				tea_encrypt(block, tk);
				ct_block[0] = ct_block[0] ^ block[0];
				ct_block[1] = ct_block[1] ^ block[1];
				fprintf(stdout, "Resultado cifracao: %x %x\n", ct_block[0], ct_block[1]);
				for(j = i - 7; j <= i; j++) {
					buf[j] = get_block_byte(ct_block[(j % 8) / 4], j % 4);
				}
				block[0] = ct_block[0];
				block[1] = ct_block[1];
				ct_block[0] = 0;
				ct_block[1] = 0;
			} else {
				if (i == len - 1) {
					int k = i + 1;
					int rem = i % 8;

					while (k % 8 != 0) {
						if (k % 4 < 3) ct_block[(k % 8) / 4] <<= 8;
						k++;
					}
					fprintf(stdout, "Bloco final: %x %x\n", ct_block[0], ct_block[1]);

					ct_block[0] = ct_block[0] ^ block[0];
					ct_block[1] = ct_block[1] ^ block[1];
					fprintf(stdout, "Resultado cifracao: %x %x\n", ct_block[0], ct_block[1]);
					for(j = i - rem; j <= i; j++) {
						buf[j] = get_block_byte(ct_block[(j % 8) / 4], j % 4);
					}
				}
			}
		}

		free(ct_block);
	}
	free(block);
}

void tea_dec(unsigned char *buf, int len) {
	int i,j;
	uint32_t byte;
	tea_key tk;
	uint32_t *block;

	block = (uint32_t *)malloc(2 * (sizeof(uint32_t)));

	printf("Decryption key: ");
	for (i = 0; i < 4; i++) {
		if (scanf("%x", &byte) != 1) {
			fprintf(stderr, "\nError reading decryption key.\n");
			exit(1);
		}
		tk.key[i] = byte;
	}

	//ECB Mode
	if (block_cipher_mode == ECB_MODE) {
		for (i = 0; i < len; i++) {
			block[(i % 8) / 4] += buf[i];
			if (i % 4 < 3) block[(i % 8) / 4] <<= 8;
			fprintf(stdout, "Pos. Buffer Atual: %d Tam. Total Buffer: %d Pos. Bloco: %d\n", i ,len, (i % 8) / 4);
			fprintf(stdout, "Byte do Buffer Atual: %d\n", buf[i]);
			fprintf(stdout, "Bloco: %x\n", block[(i % 8) / 4]);

			if(i % 8 == 7) {
				tea_decrypt(block, tk);
				fprintf(stdout, "Resultado decifracao: %x %x\n", block[0], block[1]);
				for(j = i - 7; j <= i; j++) {
					buf[j] = get_block_byte(block[(j % 8) / 4], j % 4);
				}
				block[0] = 0;
				block[1] = 0;
			}
		}
	} else { //CFB Mode
		uint32_t *pt_block, *aux_block;
		pt_block = (uint32_t *)malloc(2 * (sizeof(uint32_t)));
		pt_block[0] = 0;
		pt_block[1] = 0;
		aux_block = (uint32_t *)malloc(2 * (sizeof(uint32_t)));

		printf("Initialization Vector (IV): ");
		for (i = 0; i < 2; i++) {
			if (scanf("%x", &byte) != 1) {
				fprintf(stderr, "\nError reading initialization vector.\n");
				exit(1);
			}
			block[i] = byte;
		}

		for (i = 0; i < len; i++) {
			pt_block[(i % 8) / 4] += buf[i];
			if (i % 4 < 3) pt_block[(i % 8) / 4] <<= 8;
			fprintf(stdout, "Pos. Buffer Atual: %d Tam. Total Buffer: %d Pos. Bloco: %d\n", i ,len, (i % 8) / 4);
			fprintf(stdout, "Byte do Buffer Atual: %d\n", buf[i]);
			fprintf(stdout, "Bloco: %x\n", pt_block[(i % 8) / 4]);

			if(i % 8 == 7) {
				tea_encrypt(block, tk);
				aux_block[0] = pt_block[0];
				aux_block[1] = pt_block[1];
				pt_block[0] = pt_block[0] ^ block[0];
				pt_block[1] = pt_block[1] ^ block[1];
				fprintf(stdout, "Resultado decifracao: %x %x\n", pt_block[0], pt_block[1]);
				for(j = i - 7; j <= i; j++) {
					buf[j] = get_block_byte(pt_block[(j % 8) / 4], j % 4);
				}
				block[0] = aux_block[0];
				block[1] = aux_block[1];
				pt_block[0] = 0;
				pt_block[1] = 0;
			} else {
				if (i == len - 1) {
					int k = i + 1;
					int rem = i % 8;

					while (k % 8 != 0) {
						if (k % 4 < 3) pt_block[(k % 8) / 4] <<= 8;
						k++;
					}
					fprintf(stdout, "Bloco final: %x %x\n", pt_block[0], pt_block[1]);

					pt_block[0] = pt_block[0] ^ block[0];
					pt_block[1] = pt_block[1] ^ block[1];
					fprintf(stdout, "Resultado decifracao: %x %x\n", pt_block[0], pt_block[1]);
					for(j = i - rem; j <= i; j++) {
						buf[j] = get_block_byte(pt_block[(j % 8) / 4], j % 4);
					}
				}
			}
		}

		free(pt_block);
		free(aux_block);
	}
	free(block);
}

unsigned char get_block_byte(uint32_t i, int pos) {
	return (i >> (8*(3-pos))) & 0xff;
}
