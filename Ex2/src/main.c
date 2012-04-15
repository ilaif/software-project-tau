#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include <assert.h>
#include "graph.h"
#include "cluster.h"


#define MAX_LENGTH 305

/* The function returns true if the received string represents a non-negative/positive integer. */
/* mode = 0 - for non-negative */
/* mode = 1 - for positive */
bool is_str_non_negative_int(char* str, int mode) {
	int result = 0;
	int parsingCheck = 0;

	parsingCheck = sscanf(str, "%d", &result);

	if (parsingCheck == 0) {
		return false;
	}
	/* string is not double */
	if ( strchr(str, '.')==NULL ) {
		if (result >= mode) {
			return true;
		} else {
			return false; 
		}
	/* double */
	} else {
		return false;
	}
}

/* The function returns true if the received string represents double, otherwise false. */
bool is_str_double(char* str) {
	int parsingCheck = 0;
	double tmp = 0;

	parsingCheck=sscanf(str, "%lf", &tmp);
	/* parsing failed */
	if (parsingCheck == 0) {
		return false;
	} else {
		return true;
	}
}

/* The function returns true if str contains a letter, otherwise false. */
bool contains_letter(char* str) {
	int i = 0;
	int length = 0;

	length = (int)strlen(str);

	for (i = 0; i < length; i++) {
		if (isalpha(str[i])) {
			return true;
		}
	}
	return false;
}

/* The function converts the received string to an integer */
/* The function assumes the string represents a valid integer. */
int str_to_int(char* str) {
	int result = 0;
	sscanf(str, "%d", &result);
	return result;
}

/* The function converts the received string to a double. */
/* The function assumes the string represents a valid double. */
double str_to_double(char* str) {
	double result = 0;
	sscanf(str, "%lf", &result);
	return result;
}


/* The function inserts into result the next word in the string (ignoring white-spaces and beginning at idx). */
/* If no word was found, it will insert an empty string. */
/* return: If a word was found - The index of the last char. */
/* If no word was found - -1 */
int get_next_word(char* str, int idx, char* result) {
	int i = 0;
	
	/* Loops until found '\n' char or non white-space char */
	while ( (isspace(str[idx])) && (idx<(int)strlen(str)-1) ) {
		idx++;
	}
	/* No word was found */
	if (idx==(int)strlen(str)-1) {
		return -1;
	}
	/* Copy word to result */
	while ( (isspace(str[idx]) == false) && (idx < (int)strlen(str)-1) ) {
		result[i]=str[idx];
		idx++;
		i++;
	}
	result[i]='\0';
	return (idx-1);
}


/* The function runs the received command from the input */
int run_command(graph* grp, char* command) {
	int len = (int)strlen(command)-1;
	int tmp = 0, tmp2 = 0, nextWordIdx = 0;
	double weight=0;
	char operation[MAX_LENGTH]="";
	/* The commands have maximum 3 parameters */
	char params[3][MAX_LENGTH];

	/* Empty command */
	if (command[0]=='\n') {
		return ERROR_CMD_NAME;
	}
	/* The command starts with a white space */
	if (isspace(command[0])) {
		return ERROR_CMD_NAME;
	}
	/* The command ends with a white space */
	if (isspace(command[len-1])) {
		return ERROR_CMD_FORMAT;
	}

	nextWordIdx=get_next_word(command, 0, operation); /* getting the first word -> command name. */

	if (strcmp(operation, "add_vertex") == 0) { 
		strcpy(params[0], "");   
		nextWordIdx=get_next_word(command, nextWordIdx+1, params[0]);

		if ( (nextWordIdx==-1) || (nextWordIdx != (len-1)) ) {
			return ERROR_CMD_FORMAT;
		}

		if (contains_letter(params[0]) == false) {
			return ERROR_VER_ONE_LETTER;
		}

		return add_vertex(grp, params[0]);
	}

	if (strcmp(operation, "remove_vertex") == 0) { 
		strcpy(params[0], "");
		nextWordIdx=get_next_word(command, nextWordIdx+1, params[0]);

		if ( (nextWordIdx==-1) || (nextWordIdx != (len-1)) ) {
			return ERROR_CMD_FORMAT;
		}

		if (contains_letter(params[0])) {
			tmp=remove_vertex_by_name(grp, params[0]);
			if (tmp==ERROR_VER_DEGREE_NOT_ZERO) {
				printf("Error: Cannot delete vertex %s since there are edges connected to it\n", params[0]);
				return 0;
			}
			return tmp;
		} else {
			if (is_str_non_negative_int(params[0],0)) {
				tmp=strToInt(params[0]);
				tmp2=remove_vertex_by_id(grp, tmp);
				if (tmp2==ERROR_VER_DEGREE_NOT_ZERO) {
					printf("Error: Cannot delete vertex %s since there are edges connected to it\n", 
						   (*((*grp).vertices+tmp)).name);
					return 0;
				}
				return tmp2;
			} else {
				return ERROR_CMD_FORMAT;
			}
		}
	}

	if (strcmp(operation, "add_edge") == 0) { 
		strcpy(params[0], "");
		strcpy(params[1], "");
		strcpy(params[2], "");
		nextWordIdx=get_next_word(command, nextWordIdx+1, params[0]);
		if (nextWordIdx==-1) {
			return ERROR_CMD_FORMAT;
		}
		nextWordIdx=get_next_word(command, nextWordIdx+1, params[1]);
		if (nextWordIdx==-1) {
			return ERROR_CMD_FORMAT;
		}
		nextWordIdx=get_next_word(command, nextWordIdx+1, params[2]);
		if (nextWordIdx==-1) {
			return ERROR_CMD_FORMAT;
		}
		if (nextWordIdx != (len-1)) {
			return ERROR_CMD_FORMAT;
		}
		if (is_str_double(params[2])==false) {
			return ERROR_WEIGHT_NUMBER;
		}
		weight=str_to_double(params[2]);
		if (weight<0) {
			return ERROR_WEIGHT_POSITIVE;
		}
		if ((contains_letter(params[0])) && (contains_letter(params[1])==false)) {  
			 return ERROR_CMD_FORMAT;
		}
		if ((is_str_non_negative_int(params[0],0)) && (is_str_non_negative_int(params[1],0)==false)) { 
			 return ERROR_VER_ID_NUMBER;
		}
		if ( ((contains_letter(params[0])==false) && (is_str_non_negative_int(params[0],0)==false)) ||
			 ((contains_letter(params[1])==false) && (is_str_non_negative_int(params[1],0)==false)) ) {
				 return ERROR_CMD_FORMAT;
		}
		if (contains_letter(params[0])) {
			return add_edge_by_names(grp, params[0], params[1], weight);
		} else {
			return add_edge_by_ids(grp, strToInt(params[0]), strToInt(params[1]), weight);
		}
	}

	if (strcmp(operation, "remove_edge_by_id") == 0) {
		strcpy(params[0], "");
		nextWordIdx=get_next_word(command, nextWordIdx+1, params[0]);

		if ( (nextWordIdx==-1) || (nextWordIdx != (len-1)) ) {
			return ERROR_CMD_FORMAT;
		}

		if (is_str_non_negative_int(params[0],0)) {
			return remove_edge_by_id(grp, strToInt(params[0]));
		} else {
			return ERROR_EDGE_ID_NUMBER;
		}
	}

	if (strcmp(operation, "print") == 0) {       
		if (nextWordIdx != (len-1)) {
			return ERROR_CMD_FORMAT;
		}
		print(grp, false);
		return 0;
	}

	if (strcmp(operation, "cluster") == 0) { 
		strcpy(params[0], "");
		nextWordIdx=get_next_word(command, nextWordIdx+1, params[0]);

		if ( (nextWordIdx==-1) || (nextWordIdx != (len-1)) ) {
			return ERROR_CMD_FORMAT;
		}

		if (is_str_non_negative_int(params[0],0)==false) {
				return ERROR_NUMBER_OF_CLUSTERS;
		}
		tmp=strToInt(params[0]);
		if (tmp < 1) {
			return ERROR_NUMBER_OF_CLUSTERS;
		} else {
			cluster(grp, tmp);
			return 0;
		}
	}

	/* No valid command matched the received command */
	return  ERROR_CMD_NAME;
}


/* The function reads fill the graph from the input files */
/* The parameter verticesOrEdges determines whether the input file is for vertices or edges */
int fill_graph(FILE* fil, graph *grp, int verticesOrEdges) {
	char line[MAX_LENGTH];
	char cmd[MAX_LENGTH+10];
	int error;

	while(1) {
		fgets(line, MAX_LENGTH, fil);
		if (feof(fil)) {
			break;
		}
	
		if (verticesOrEdges==0) {
			strcpy(cmd, "add_vertex ");
		} else {
			strcpy(cmd, "add_edge ");
		}

		strcat(cmd, line);
		error=run_command(grp, cmd);
		/* Checks the command ran successfully */
		if (error != 0) {
			return error;         
		}
	}

	return 0;
}

/* The function writes the clustered graph for k=U to the results file */
void graph_to_file(graph *grp, FILE* result) {
	int i = 0;
	char* verNameA;
	char* verNameB;

	/* Writes number of vertices */
	if (grp->numVertices > 0) {
		fprintf(result, "%d vertices:\n", grp->numVertices);
	}
	/* Writes vertices */
	for(i = 0; i <= grp->lastVerIdx; i++) {
		fprintf(result, "%d: %s %d\n", i, (grp->vertices+i)->name, (grp->vertices+i)->cluster);
	}
	/* Writes number of edges */
	if (grp->numEdges > 0) {
		fprintf(result, "%d edges:\n", grp->numEdges);
	}
	/* Writes edges */
	for(i = 0; i <= grp->lastEdgeIdx; i++) {
		verNameA = (grp->vertices+(grp->edges+i)->v1_id)->name;
        verNameB = (grp->vertices+(grp->edges+i)->v2_id)->name;

        fprintf(result, "%d: %s-%s %.3f\n", i, verNameA, verNameB, (grp->edges+i)->weight);
	}
}  


void print_error(int error_number) {
	switch(error_number) {
		case ERROR_CMD_NAME: printf("Error: Command is not recognized\n");
				break;
		case ERROR_CMD_FORMAT: printf("Error: Command format is not valid\n");
				break;
		case ERROR_WEIGHT_NUMBER: printf("Error: When adding an edge weight must be a number\n");
				break;
		case ERROR_WEIGHT_POSITIVE: printf("Error: When adding an edge weight must be positive\n");
				break;
		case ERROR_VER_ONE_LETTER: printf("Error: When adding a vertex name must have at least one letter\n");
				break;
		case ERROR_EDGE_ID_NUMBER: printf("Error: Edge id must be a number\n");
				break;
		case ERROR_VER_ID_NUMBER: printf("Error: Vertex id must be a number\n");
				break;
		case ERROR_NUMBER_OF_CLUSTERS: printf("Error: Number of clusters must be a number bigger or equal to 1\n");
				break;
		case ERROR_MALLOC: printf("Error: Memory allocation failed\n");
				break;
		case ERROR_VER_NOT_EXIST: printf("Error: Requested vertex does not exist\n");
				 break;
		case ERROR_VER_SAME_NAME: printf("Error: More than one vertex with the requested name exists, please remove using id\n");
				 break;
		case ERROR_VER_NOT_FOUND: printf("Error: First/second vertex was not found\n");
				 break;
		case ERROR_SELF_LOOPS: printf("Error: No self loops are allowed in the graph\n");
				 break;
		case ERROR_EDGE_DUPLICATE: printf("Error: No duplicated edges are allowed in the graph\n");
				 break;
		case ERROR_EDGE_NOT_EXIST: printf("Error: Requested edge does not exist\n");
				 break;
		default: printf("Error not recognized!\n");
				 break;
	}
}

int main(int argc, char *argv[]) {

	int i = 0, error = 0, L, U;
	/* for CPLEX */
	double objval=0;
	char *vertices_file_name, *edges_file_name, *results_file_name;
	FILE *vertices_file, *edges_file, *results_file;

	graph *ptr;
	graph grp;

	/* Graph properties initialization */
	grp.lastVerIdx=-1;
	grp.lastEdgeIdx=-1;
	grp.verLength=0;
	grp.edgeLength=0;
	grp.numVertices=0;
	grp.numEdges=0;
	grp.vertices=NULL;
	grp.edges=NULL;

	ptr=&grp;

	/* Invalid number of arguments */
	if (argc != 5) {
		printf("Error: Wrong number of input arguments\n");
		return 0;
	}

	vertices_file_name=(char*)malloc((strlen(argv[1])+10)*sizeof(char));
	if (vertices_file_name == NULL) {   
		print_error(ERROR_MALLOC);
		return 0;
	}
	strcpy(vertices_file_name, argv[1]);
	strcat(vertices_file_name, "/nodes");
	vertices_file = fopen(vertices_file_name,"r");
	if (vertices_file_name == NULL) {   
		printf("Error: Nodes file could not be opened or the path/file doesn't exist\n");
		return 0;
	}


	edges_file_name=(char*)malloc((strlen(argv[1])+10)*sizeof(char));
	if (edges_file_name == NULL) {   
		print_error(ERROR_MALLOC);
		return 0;
	}
	strcpy(edges_file_name, argv[1]);
	strcat(edges_file_name, "/edges");
	edges_file = fopen(edges_file_name,"r");
	if (edges_file_name == NULL) {   
		printf("Error: Edges file could not be opened or the path/file doesn't exist\n");
		return 0;
	}


	results_file_name=(char*)malloc((strlen(argv[2])+10)*sizeof(char));
	if (results_file_name == NULL) {   
		print_error(ERROR_MALLOC);
		return 0;
	}
	strcpy(results_file_name, argv[2]);
	strcat(results_file_name, "/results");
	results_file = fopen(results_file_name,"w");
	if (results_file_name == NULL) {   
		printf("Error: Results file could not be opened or the path doesn't exist\n");
		return 0;
	}


	if (is_str_non_negative_int(argv[3],1) && is_str_non_negative_int(argv[4],1)) { 
		L=strToInt(argv[3]);
	    U=strToInt(argv[4]);
		if (U<L) {  
			printf("Error: Lower bound for the number of clusters is bigger than the upper bound\n");
			return 0;
		}
	} else {
		printf("Error: Lower or upper bound for the number of clusters isn't a positive integer\n");
		return 0;
	}

	/* Inserting the vertices from the input file to the graph */
	error=fill_graph(vertices_file, ptr, 0);
	if (error != 0) {   
		print_error(error);  
		return 0;
	}

	/* Inserting the edges from the input file to the graph */
	error=fill_graph(edges_file, ptr, 1);
	if (error != 0) {   
		print_error(error);  
		return 0;
	}

	/* Running the cluster algorithm */
	for (i=L; i<=U; i++) {
		error=k_cluster(ptr, i, &objval);
		if (error != 0) {
			if (error == ERROR_MALLOC) {
				print_error(ERROR_MALLOC);
			}
			return 0;
		}
		fprintf(results_file,"%d: %.3f\n", i, objval); /* clustering scores */
	}

	fprintf(results_file,"\n"); 

	/* Write the clustered graph for k=U */
	fprintf(results_file,"The clustered graph for %d:\n", U);
	graph_to_file(ptr, results_file);


	free(vertices_file_name);
	free(edges_file_name);
	free(results_file_name);

	fclose(vertices_file);
	fclose(edges_file);  
	fclose(results_file); 

	/* Free memory */
	for(i=0; i<=grp.lastVerIdx; i++) {
		if ( (*(grp.vertices+i)).deleted == false ) {
			free( (*(grp.vertices+i)).name );
		}
	}

	free( grp.vertices );
	free( grp.edges );

	return 0;
}
