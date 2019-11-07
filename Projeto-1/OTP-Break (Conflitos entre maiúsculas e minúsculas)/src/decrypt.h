#ifndef BUFFER_SIZE
  #define BUFFER_SIZE 1024
#endif

#ifdef DECRYPT
  int debug_mode = 0;
#else
  extern int debug_mode;
#endif

extern void cribdrag(FILE *cipher1, FILE *cipher2, FILE *dictionary);
char *get_cipher(FILE *cipher);
char *strxor(char *str1, char *str2);
void decrypt(char *str, FILE *dictionary);
char **get_dictionary(FILE *dictionary, int *size);
void get_plain(char *str, char *word);
void insert_possible_texts(FILE *crib, char *cribplain, int i, int word_len);
int verify_text(char *cribplain, int word_len);
void discover_key(FILE *plaintext, FILE *cipher, char *key_filename);