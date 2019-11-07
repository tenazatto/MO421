#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "options.h"
#include "decrypt.h"
#include "help.h"

char *arg1 = "", *arg2 = "", *arg3 = "";

void define_mode(int argc, char const *argv[], int *mode) {
	if (argc > 1) {
		if (strcmp(argv[1],"-d") == 0) {
			if (argc > 2) {
				if (strcmp(argv[2],"-cipher") == 0) {
					*mode = validate_num_arguments(argc, 6, DEBUGMODE + CIPHERMODE);
				} else if (strcmp(argv[2],"-key") == 0) {
					*mode = DEBUGMODE + KEYMODE;
				} else {
					*mode = validate_num_arguments(argc, 5, DEBUGMODE + CIPHERMODE);
				}
			} else {
				*mode = NUMARGERR;
			}
		} else if (strcmp(argv[1],"-cipher") == 0) {
			*mode = validate_num_arguments(argc, 5, CIPHERMODE);
		} else if (strcmp(argv[1],"-key") == 0) {
			*mode = KEYMODE;
		} else if (strcmp(argv[1],"-help") == 0) {
			if (argc == 3) {
				if (strcmp(argv[2],"-cipher") == 0) {
					*mode = HELPCIPHERMODE;
				} else if (strcmp(argv[2],"-key") == 0) {
					*mode = HELPKEYMODE;
				} else if (strcmp(argv[2],"-all") == 0) {
					*mode = HELPMODE;
				} else {
					*mode = DEFAULTERR;
				}
			} else if (argc == 2) {
				*mode = HELPMODE;
			} else {
				*mode = NUMARGERR;
			}
		} else {
			*mode = validate_num_arguments(argc, 4, CIPHERMODE);
		}
	} else {
		*mode = NUMARGERR;
	}

	define_parameters(argc, argv, *mode);
}

int validate_num_arguments(int argc, int num_args, int selected_mode) {
	if (argc == num_args) {
		return selected_mode;
	} else {
		return NUMARGERR;
	}
}

void define_execution(char const *argv[], int mode) {
	switch (mode) {
		case CIPHERMODE:
			//fprintf(stdout, "Opção Selecionada: %d - CIPHERMODE\n", CIPHERMODE);
			cribdrag(fopen(arg1,"rb"), fopen(arg2,"rb"), fopen(arg3,"rb"));
			break;
		case KEYMODE:
			//fprintf(stdout, "Opção Selecionada: %d - KEYMODE\n", KEYMODE);
			discover_key(fopen(arg1,"rb"), fopen(arg2,"rb"), arg3);
			break;
		case HELPMODE:
			//fprintf(stdout, "Opção Selecionada: %d - HELPMODE\n", HELPMODE);
			help_all();
			break;
		case HELPKEYMODE:
			//fprintf(stdout, "Opção Selecionada: %d - HELPKEYMODE\n", HELPKEYMODE);
			help_key();
			break;
		case HELPCIPHERMODE:
			//fprintf(stdout, "Opção Selecionada: %d - HELPCIPHERMODE\n", HELPCIPHERMODE);
			help_cipher();
			break;
		case DEBUGMODE + CIPHERMODE:
			//fprintf(stdout, "Opção Selecionada: %d - DEBUGMODE + CIPHERMODE\n", DEBUGMODE + CIPHERMODE);
			debug_mode = 1;
			cribdrag(fopen(arg1,"rb"), fopen(arg2,"rb"), fopen(arg3,"rb"));
			break;
		case DEBUGMODE + KEYMODE:
			//fprintf(stdout, "Opção Selecionada: %d - DEBUGMODE + KEYMODE\n", DEBUGMODE + KEYMODE);
			debug_mode = 1;
			discover_key(fopen(arg1,"rb"), fopen(arg2,"rb"), arg3);
			break;
		case NUMARGERR:
			fprintf(stderr, "ERRO - número de parâmetros inválido!\n");
			break;
		default:
			fprintf(stderr, "ERRO - Opção Inválida selecionada!\n");
	}
}

void define_parameters(int argc, char const *argv[], int mode) {
	switch (mode) {
		case CIPHERMODE:
		case DEBUGMODE + CIPHERMODE:
			arg1 = argv[argc - 3];
			arg2 = argv[argc - 2];
			arg3 = argv[argc - 1];
			break;
		case KEYMODE:
			if (argc == 5) {
				arg1 = argv[argc - 3];
				arg2 = argv[argc - 2];
				arg3 = argv[argc - 1];
			} else {
				arg1 = argv[argc - 2];
				arg2 = argv[argc - 1];
				arg3 = (char*)calloc(1, sizeof(char));
			}
			break;
		case DEBUGMODE + KEYMODE:
			if (argc == 6) {
				arg1 = argv[argc - 3];
				arg2 = argv[argc - 2];
				arg3 = argv[argc - 1];
			} else {
				arg1 = argv[argc - 2];
				arg2 = argv[argc - 1];
				arg3 = (char*)calloc(1, sizeof(char));
			}
			break;
	}
}
