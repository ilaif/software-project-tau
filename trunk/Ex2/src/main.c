
#include <stdio.h>
#include <string.h>
#include "graph.h"

#define MAX_LENGTH 300

void quit();
void strip_newline(char *str, int size);
void run_command(char* command);
char* get_command_param(char* command, int param_number);

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
	char* command_name = get_command_param(command, 0);

	if(strcmp("add_vertex", command_name) == 0) {
		char* vertex_name = get_command_param(command, 1);
		if(vertex_name == NULL || strlen(vertex_name) < 1) {
			print_error("When adding a vertex name must have at least one letter");
			return;
		} else {
			add_vertex(vertex_name);
		}
	} else if(strcmp("remove_vertex", command_name) == 0) {
		char* vertex = get_command_param(command, 1);
		if(vertex == NULL || strlen(vertex) < 1) {
			print_error("Command format is not valid");
			return;
		} else {
			int vertex_id = atoi(vertex);
			if(vertex_id == 0) {
				remove_vertex_by_name(vertex);
			} else {
				remove_vertex_by_id(vertex_id);
			}
		}
	} else if(strcmp("add_edge", command_name) == 0) {
		char* vertex_a = get_command_param(command, 1);
		if(vertex_a == NULL || strlen(vertex_a) < 1) {
			print_error("Command format is not valid");
			return;
		}
		int vertex_a_id = atoi(vertex_a);

		char* vertex_b = get_command_param(command, 2);
		if(vertex_b == NULL || strlen(vertex_b) < 1) {
			print_error("Command format is not valid");
			return;
		}
		int vertex_b_id = atoi(vertex_b);

		char* weight_str = get_command_param(command, 3);
		if(weight_str == NULL || strlen(weight_str) < 1) {
			print_error("Command format is not valid");
			return;
		}
		int weight = atoi(weight_str);
		if(weight == 0) {
			print_error("When adding an edge weight must be a number");
			return;
		}
		if(weight < 0) {
			print_error("When adding an edge weight must be positive");
			return;
		}

		if(vertex_a_id != 0 && vertex_b_id != 0) {
			add_edge_by_id(vertex_a_id, vertex_b_id, weight);
		} else {
			add_edge_by_name(vertex_a, vertex_b, weight);
		}

	} else if(strcmp("remove_edge", command_name) == 0) {
		char* edge_id_str = get_command_param(command, 1);
		if(edge_id_str == NULL || strlen(edge_id_str) < 1) {
			print_error("Command format is not valid");
			return;
		} else {
			int edge_id = atoi(edge_id_str);
			if(edge_id == 0) {
				print_error("Edge id must be a number");
				return;
			} else {
				remove_edge(edge_id);
			}
		}
	} else if(strcmp("print", command_name) == 0) {
		print();
	} else if(strcmp("cluster", command_name) == 0) {
		char* num_clusters_str = get_command_param(command, 1);
		if(num_clusters_str == NULL || strlen(num_clusters_str) < 1) {
			print_error("Command format is not valid");
			return;
		} else {
			int num_clusters = atoi(num_clusters_str);
			if(num_clusters < 1) {
				print_error("Number of clusters must be a number bigger or equal to 1");
				return;
			} else {
				cluster(num_clusters);
			}
		}
	} else if(strcmp("quit", command_name) == 0) {
		quit();
	} else {
		print_error("Command is not recognized");
		return;
	}
}

// the function retrieves a parameter from the command according to the given param_number
char* get_command_param(char* command, int param_number) {
	int current_param = 0;
	char command_tmp[MAX_LENGTH];
	strcpy(command_tmp, command);

	char* user_input = NULL;
	user_input = strtok(command_tmp, " ");

	while(current_param < param_number) {
		user_input = strtok(NULL, " ");
		current_param++;
	}

	return user_input;
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

