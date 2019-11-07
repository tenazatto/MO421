#define DECRYPT

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>

#include "decrypt.h"

int cipnum = 33;

void cribdrag(FILE *cipher1, FILE *cipher2, FILE *dictionary) {
	int i;
	if (debug_mode) fprintf(stdout,"1\n");
	char *strcipher1 = get_cipher(cipher1);
	char *strcipher2 = get_cipher(cipher2);

	if (debug_mode) fprintf(stdout,"2\n");
	char *strxor1 = strxor(strcipher1, strcipher2);

	if (debug_mode) {
		fprintf(stdout,"%s\n", strxor1);
		for (i = 0; i < strlen(strxor1); i++) {
			fprintf(stdout,"%d ", strxor1[i]);
		}
		fprintf(stdout,"\n");

		struct stat st;
		if (stat("debug", &st) == -1) {
			mkdir("debug", 0700);
		}

		FILE *debug_file = fopen("debug/texts.txt", "wb+");
		fprintf(debug_file,"CIPHER 1:\n");
		fprintf(debug_file,"%s\n", strcipher1);
		for (i = 0; i < strlen(strcipher1); i++) {
			fprintf(debug_file,"%d ", strcipher1[i]);
		}
		fprintf(debug_file,"\n");
		fprintf(debug_file,"CIPHER 2:\n");
		fprintf(debug_file,"%s\n", strcipher2);
		for (i = 0; i < strlen(strcipher2); i++) {
			fprintf(debug_file,"%d ", strcipher2[i]);
		}
		fprintf(debug_file,"\n");
		fprintf(debug_file,"XOR:\n");
		fprintf(debug_file,"%s\n", strxor1);
		for (i = 0; i < strlen(strxor1); i++) {
			fprintf(debug_file,"%d ", strxor1[i]);
		}
		fprintf(debug_file,"\n");
		fclose(debug_file);
	}

	if (debug_mode) fprintf(stdout,"3\n");
	decrypt(strxor1, dictionary);
}

char *get_cipher(FILE *cipher) {
	int c, i = 0;
	char ch[1];
	char *str = (char *) malloc(BUFFER_SIZE);

	if (debug_mode) fprintf(stdout,"x\n");
	while ((c = fgetc(cipher)) != EOF) {
		if (debug_mode) fprintf(stdout,"%d ", c);
		ch[0] = c;
		strncat(str, ch, 1);
        i++;
	}

    if (debug_mode){
        fprintf(stdout,"y\n");
        cipnum += 32;
        char filename[15];
        char cha[1];
        cha[0] = cipnum;
        strcpy(filename, "getcipher");
        strncat(filename, cha, 1);
        strcat(filename, ".txt");
        FILE *cipher = fopen(filename, "wb+");
        fprintf(cipher,"%d\n", i);
        fprintf(cipher,"%s\n", str);
        fprintf(cipher,"%d\n", strlen(str));
        fclose(cipher);
    }

	if (debug_mode) fprintf(stdout,"\n");

	return str;
}

char *strxor(char *str1, char *str2) {
	int i;
	char c[1];
	char *str = (char *) malloc(BUFFER_SIZE);

	int str1len = strlen(str1);
	int str2len = strlen(str2);

	if (debug_mode) {
		fprintf(stdout,"CIPHER 1 STRING LENGTH: %d ", str1len);
		fprintf(stdout,"CIPHER 2 STRING LENGTH: %d\n", str2len);
	}
	int strxorlen;

	if (str1len < str2len) {
		strxorlen = str1len;
	} else {
		strxorlen = str2len;
	}

	for (i = 0; i < strxorlen; i++) {
		c[0] = str1[i] ^ str2[i];
		strncat(str,c,1);
	}

	return str;
}

void decrypt(char *str, FILE *dictionary) {
	int i, dic_size = 0;
	char **dic;

	dic = get_dictionary(dictionary, &dic_size);

	for (i = 0; i < dic_size; i++) {
		get_plain(str, dic[i]);
	}
}

char **get_dictionary(FILE *dictionary, int *size) {
	int c, i = 0;
	char ch[1];
	char *word = (char *) malloc(BUFFER_SIZE);
	char *result[BUFFER_SIZE];

	while ((c = fgetc(dictionary)) != EOF) {
		ch[0] = c;
		if(ch[0] != '\n') {
			strncat(word, ch, 1);
		} else {
			if (debug_mode){
				int j;
				fprintf(stdout,"%s\n", word);
				for (j = 0; j < strlen(word); j++) {
					fprintf(stdout,"%d ", word[j]);
				}
				fprintf(stdout,"\n");
			}
			result[i] = word;
			i++;
			word = (char *) malloc(BUFFER_SIZE);
		}
	}

    if (debug_mode) {
        int j;
        fprintf(stdout,"\n");
        for (j = 0; j < i; j++) {
            fprintf(stdout,"%s\n", result[j]);
        }
        fprintf(stdout,"\n");
    }

	*size = i;

	return result;
}

void get_plain(char *str, char *word) {
	int i, j;
	int str_len = strlen(str);
	int word_len = strlen(word);
	struct stat st;

	char *aux_str;
	char filename[BUFFER_SIZE + 15];

	if (stat("cribs", &st) == -1) {
		mkdir("cribs", 0700);
	}

	strcpy(filename, "cribs/");
	strcat(filename, word);
	strcat(filename, ".txt");

	FILE *crib = fopen(filename, "wb+");

	for (i = 0; i < str_len - word_len + 1; i++) {
		aux_str = (char *) malloc(word_len);

		for (j = 0; j < word_len; j++) {
			aux_str[j] = str[i + j];
		}
		if (debug_mode) {
			int aux_len = strlen(aux_str);
			fprintf(stdout,"AUX LENGTH: %d ", aux_len);
			fprintf(stdout,"WORD LENGTH: %d\n", word_len);
		}
		char *cribplain = strxor(aux_str,word);

		insert_possible_texts(crib, cribplain, i, word_len);
	}

	fclose(crib);
}

void insert_possible_texts(FILE *crib, char *cribplain, int i, int word_len) {
	if (verify_text(cribplain, word_len)) {
		fprintf(crib, "%d: %s", i, cribplain);
		if (debug_mode) {
			fprintf(crib, " - ");
			int k;
			for (k = 0; k < word_len; k++) {
				fprintf(crib, "%d ", cribplain[k]);
			}
		}
		fprintf(crib, "\n");
	}
}

int verify_text(char *cribplain, int word_len) {
	int i, v = 1;
	for (i = 0; i < word_len; i++) {
		//if(!(cribplain[i] >= 32 && cribplain[i] <= 126)){
		if(!(cribplain[i] == 32
		  || cribplain[i] == 33 || cribplain[i] == 46 || cribplain[i] == 32
		  || cribplain[i] == 58 || cribplain[i] == 59 || cribplain[i] == 63
		  || (cribplain[i] >= 65 && cribplain[i] <= 90)
		  || (cribplain[i] >= 97 && cribplain[i] <= 122)
		  || (cribplain[i] >= 48 && cribplain[i] <= 57))){
			v = 0;
			break;
		}
	}

	return v;
}

void discover_key(FILE *plaintext, FILE *cipher, char *key_filename) {
	char filename[BUFFER_SIZE + 5];
	struct stat st;

	char *strplain = get_cipher(plaintext);
	char *strcipher = get_cipher(cipher);

	char *strxor1 = strxor(strplain, strcipher);

	if (stat("keys", &st) == -1) {
		mkdir("keys", 0700);
	}

	if (strcmp(key_filename, "") == 0) {
		strcpy(key_filename, "key.txt");
	}

	if(debug_mode) {
		fprintf(stdout,"KEY FILE: %s ", key_filename);
		fprintf(stdout,"COMPARATION WITH EMPTY: %d\n", strcmp(key_filename, ""));
	}

	strcpy(filename, "keys/");
	strcat(filename, key_filename);

	FILE *key_file = fopen(filename, "wb+");

	fprintf(key_file, "%s", strxor1);
}
