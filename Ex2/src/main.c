//Full Name 1: Or Segal; Id No 1: 203993118; User Name 1: orsegal
//Full Name 2: Aviv Mor; Id No 2: 201254059; User Name 2: avivmor
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include "graph.h"

#define MAX_LENGTH 300

void quit();
void strip_newline(char *str, int size);
void run_command(char* command);
char* get_command_param(const char* command, int param_number);
bool valid_command_params(const char* command, int num_of_params);
bool isNumeric (const char * s);
bool isInteger (const char * s);
bool isContainLetter(const char *mystring);

bool exitFlag = false;

int main(void) {
	//REMOVE THESE LINES BEFORE SUBMITING - fix for windows
	setvbuf(stdout, NULL, _IONBF, 0);
	setvbuf(stderr, NULL, _IONBF, 0);
	//***********************************

	char command[MAX_LENGTH];

	while(!exitFlag) {
		fgets(command, MAX_LENGTH, stdin);
		strip_newline(command, MAX_LENGTH);
		run_command(command);
	}

	return 0;
}

void quit() {
	exitFlag = true;
}

// the function parse the command string and runs a function accordingly
void run_command(char* command) {
	if(command == NULL || strlen(command) < 1 || isspace(*command)) {
		print_error("Command format is not valid");
		return;
	}
	char* command_name = get_command_param(command, 0);
	if(strcmp("add_vertex", command_name) == 0) {
		if(!valid_command_params(command, 2)) {
			print_error("Command format is not valid");
			return;
		}

		const char* vertex_name = get_command_param(command, 1);
		if(vertex_name == NULL || strlen(vertex_name) < 1) {
			print_error("Command format is not valid");
			return;
		} else if (!isContainLetter(vertex_name)) {
			print_error("When adding a vertex name must have at least one letter");
			return;
		} else {
			add_vertex(vertex_name);
		}
	} else if(strcmp("remove_vertex", command_name) == 0) {
		if(!valid_command_params(command, 2)) {
			print_error("Command format is not valid");
			return;
		}

		char* vertex = get_command_param(command, 1);
		if(vertex == NULL || strlen(vertex) < 1) {
			print_error("Command format is not valid");
			return;
		} else {
			if(!isInteger(vertex)) {
				remove_vertex_by_name(vertex);
			} else {
				int vertex_id;
				sscanf(vertex, "%d", &vertex_id);
				remove_vertex_by_id(vertex_id);
			}
		}
	} else if(strcmp("add_edge", command_name) == 0) {
		if(!valid_command_params(command, 4)) {
			print_error("Command format is not valid");
			return;
		}

		char* vertex_a = get_command_param(command, 1);
		if(vertex_a == NULL || strlen(vertex_a) < 1) {
			print_error("Command format is not valid");
			return;
		}

		char* vertex_b = get_command_param(command, 2);
		if(vertex_b == NULL || strlen(vertex_b) < 1) {
			print_error("Command format is not valid");
			return;
		}

		char* weight_str = get_command_param(command, 3);
		if(weight_str == NULL || strlen(weight_str) < 1) {
			print_error("Command format is not valid");
			return;
		}
		if(!isNumeric(weight_str)) {
			print_error("When adding an edge weight must be a number");
			return;
		}
		double weight;
		sscanf(weight_str, "%lf", &weight);
		if(weight < 0) {
			print_error("When adding an edge weight must be positive");
			return;
		}

		if(isInteger(vertex_a) && isInteger(vertex_b)) {
			int vertex_a_id;
			sscanf(vertex_a, "%d", &vertex_a_id);
			int vertex_b_id;
			sscanf(vertex_b, "%d", &vertex_b_id);
			add_edge_by_id(vertex_a_id, vertex_b_id, weight);
		} else {
			add_edge_by_name(vertex_a, vertex_b, weight);
		}

	} else if(strcmp("remove_edge", command_name) == 0) {
		if(!valid_command_params(command, 2)) {
			print_error("Command format is not valid");
			return;
		}

		char* edge_id_str = get_command_param(command, 1);
		if(edge_id_str == NULL || strlen(edge_id_str) < 1) {
			print_error("Command format is not valid");
			return;
		} else {
			if(!isInteger(edge_id_str)) {
				print_error("Edge id must be a number");
				return;
			} else {
				int edge_id;
				sscanf(edge_id_str, "%d", &edge_id);
				remove_edge(edge_id);
			}
		}
	} else if(strcmp("print", command_name) == 0) {
		if(!valid_command_params(command, 1)) {
			print_error("Command format is not valid");
			return;
		}

		print();
	} else if(strcmp("cluster", command_name) == 0) {
		if(!valid_command_params(command, 2)) {
			print_error("Command format is not valid");
			return;
		}

		char* num_clusters_str = get_command_param(command, 1);
		if(num_clusters_str == NULL || strlen(num_clusters_str) < 1) {
			print_error("Command format is not valid");
			return;
		} else {
			if(!isInteger(num_clusters_str)) {
				print_error("Command format is not valid");
				return;
			}
			int num_clusters;
			sscanf(num_clusters_str, "%d", &num_clusters);
			if(num_clusters < 1) {
				print_error("Number of clusters must be a number bigger or equal to 1");
				return;
			} else {
				cluster(num_clusters);
			}
		}
	} else if(strcmp("quit", command_name) == 0) {
		if(!valid_command_params(command, 1)) {
			print_error("Command format is not valid");
			return;
		}
		free_and_quit();
		quit();
	} else {
		print_error("Command is not recognized");
		return;
	}
}

// the function retrieves a parameter from the command according to the given param_number
char* get_command_param(const char* command, int param_number) {
	int current_param = 0;
	char command_tmp[MAX_LENGTH];
	strcpy(command_tmp, command);

	char* user_input = NULL;
	user_input = strtok(command_tmp, " ");

	while(current_param < param_number) {
		user_input = strtok(NULL, " ");
		current_param++;
	}

	char* param = strdup(user_input);
	return param;
}

// the function check if the number of params in the command is valid
bool valid_command_params(const char* command, int num_of_params) {
	char* extra_command = get_command_param(command, num_of_params);
	if(extra_command == NULL || strlen(extra_command) < 1) {
		return true;
	}
	return false;
}

// the function removes the newline from the end of a string entered using fgets.
void strip_newline(char *str, int size) {
    int i;

    for (  i = 0; i < size; ++i ) {
        if ( str[i] == '\n' ) {
            str[i] = '\0';
            return;
        }
    }
}

// Returns true if character-string parameter represents a signed or unsigned floating-point number. Otherwise returns false.
bool isNumeric (const char * s) {
    if (s == NULL || *s == '\0' || isspace(*s))
      return 0;
    char * p;
    strtod (s, &p);
    return *p == '\0';
}

// Returns true if character-string parameter represents a signed or unsigned integer number. Otherwise returns false.
bool isInteger (const char * s) {
    if (s == NULL || *s == '\0' || isspace(*s))
      return 0;
    char * p;
    strtol (s, &p, 10);
    return *p == '\0';
}

// Returns true if there is a letter in the string. Otherwise returns false.
bool isContainLetter(const char *c) {
   const char *letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
   int i = 0;
   while (c[i] != 0) {
	   if (strchr(letters, c[i]) != NULL) {
		  return true;
	   }
	   i++;
   }
   return false;
}
