#ifndef CIPHERMODE
	#define CIPHERMODE 1 
#endif

#ifndef KEYMODE
	#define KEYMODE 2 
#endif

#ifndef HELPMODE
	#define HELPMODE 3
#endif

#ifndef HELPKEYMODE
	#define HELPKEYMODE 4
#endif

#ifndef HELPCIPHERMODE
	#define HELPCIPHERMODE 5
#endif

#ifndef DEBUGMODE
	#define DEBUGMODE 10
#endif

#ifndef DEFAULTERR
	#define DEFAULTERR 100
#endif

#ifndef NUMARGERR
	#define NUMARGERR 101
#endif

extern void define_mode(int argc, char const *argv[], int *mode);
int validate_num_arguments(int argc, int num_args, int selected_mode);
extern void define_execution(char const *argv[], int mode);
void define_parameters(int argc, char const *argv[], int mode);
