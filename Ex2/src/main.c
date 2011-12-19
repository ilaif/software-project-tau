
#include <stdio.h>
#include <string.h>
#include "graph.h"

#define MAX_LENGTH 300

void quit();
void strip_newline(char *str, int size);
void run_command(char* command);
char* get_command_param(char* command, int param_number);
void print_error(char* error);

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

void run_command(char* command) {
	char* command_name = get_command_param(command, 0);

	if(strcmp("add_vertex", command_name) == 0) {
		add_vertex("name");
	} else if(strcmp("remove_vertex", command_name) == 0) {
		add_vertex("name");
	} else if(strcmp("add_edge", command_name) == 0) {
		add_vertex("name");
	} else if(strcmp("remove_edge", command_name) == 0) {
		add_vertex("name");
	} else if(strcmp("print", command_name) == 0) {
		add_vertex("name");
	} else if(strcmp("cluster", command_name) == 0) {
		add_vertex("name");
	} else if(strcmp("quit", command_name) == 0) {
		quit();
	} else {
		print_error("Command is not recognized");
	}
}

// this function retrieve a parameter from the command according to the given param_number
char* get_command_param(char* command, int param_number) {
	int current_param = 0;
	char* user_input = NULL;
	user_input = strtok(command, " ");

	while(current_param < param_number) {
		user_input = strtok(NULL, " ");
	}

	return user_input;
}

// this function remove the newline from the end of a string entered using fgets.
void strip_newline(char *str, int size) {
    int i;

    for (  i = 0; i < size; ++i ) {
        if ( str[i] == '\n' ) {
            str[i] = '\0';
            return;
        }
    }
}

// this function prints an error message
void print_error(char* error) {
	printf("Error: %s \n", error);
}


