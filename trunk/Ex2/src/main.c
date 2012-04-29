#include "graph.h"
#include "cluster.h"
#include "xgmml.h"

#define MAX_LEN 305
#define FILE_NAME_MAX_LEN 50

/* The function converts the received string to an integer
 * The function assumes the string is a valid integer */
int string_to_integer(char* str) {
	int res=0;
	sscanf(str, "%d", &res);
	return res;
}

/* The function converts the received string to a double
 * The function assumes the string is a valid double */
double string_to_double(char* str) {
	double res=0;
	sscanf(str, "%lf", &res);
	return res;
}

/* The function returns true if the received string represents an integer greater than min, otherwise false */
bool is_integer_greater_than(char* str, int min) {
	int resInt=0, success=0;

	success=sscanf(str, "%d", &resInt);
	if ( (success==0) || (success==EOF) ) {  /* conversion failed. */ 
		return false;
	}

	if ( strchr(str, '.')==NULL ) { /* str is not double. */ 
		if (resInt >= min) {
			return true;
		} else {
			return false; 
		}
	} else { /* double. */
		return false;
	}
}

/* The function returns true if the received string represents a double, otherwise false */
bool is_double(char* str) {
	int success=0;
	double tmp=0;

	success=sscanf(str, "%lf", &tmp);
	if ( (success==0) || (success==EOF) ) {
		return false;
	} else {
		return true;
	}
}

/* The function returns true if the received string contains a letter, otherwise false */
bool contains_letter(char* str) {
	int i=0, len=0;
	len = (int)strlen(str);

	for (i=0; i<len; i++) {
		if (isalpha(str[i])) {
			return true;
		}
	}
	return false;
}

/* The function inserts the next parameter starting from the received index into "parameter"
 * The function returns the last char index or -1 if no parameter found */
int next_parameter(char* command, int idx, char* parameter) { 
	int i=0;
	
	while ( (isspace(command[idx])) && (idx<(int)strlen(command)-1) ) {  
		idx++;
	}

	if (idx==(int)strlen(command)-1) {
		return -1;
	}

	while ( (isspace(command[idx]) == false) && (idx<(int)strlen(command)-1) ) {  
		parameter[i]=command[idx]; 
		idx++;
		i++;
	}

	parameter[i]='\0';
	return (idx-1);
}

/* The function frees the received pointer if it is not null */
void free_pointer_memory (void *ptr) {
   if (ptr != NULL) {
      free(ptr);
   }
}

/* The function receives a command, interpret and run the command  */
int run_command(graph* grp, char* command) {
	int len=(int)strlen(command)-1;
	int tmp=0, tmp2=0, nextParamIdx=0;
	double weight=0;
	char commandName[MAX_LEN]="";
	/* Three arguments tops */
	char arguments[3][MAX_LEN];

	if (command[0]=='\n') {
		return ERROR_CMD_NOT_RECOGNIZED;
	}
	if (isspace(command[0])) {
		return ERROR_CMD_NOT_RECOGNIZED;
	}
	if (isspace(command[len-1])) {
		return ERROR_FORMAT_NOT_VALID;
	}

	/* first parameter is the command */
	nextParamIdx=next_parameter(command, 0, commandName);

	if (strcmp(commandName, "add_vertex") == 0) {
		strcpy(arguments[0], "");
		nextParamIdx=next_parameter(command, nextParamIdx+1, arguments[0]);

		if ( (nextParamIdx==-1) || (nextParamIdx != (len-1)) ) {
			return ERROR_FORMAT_NOT_VALID;                                     
		}

		if (contains_letter(arguments[0]) == false) {
			return ERROR_VERTEX_NAME_NOT_VALID;
		}

		return add_vertex(grp, arguments[0]);
	}


	if (strcmp(commandName, "remove_vertex") == 0) {
		strcpy(arguments[0], "");
		nextParamIdx=next_parameter(command, nextParamIdx+1, arguments[0]);

		if ( (nextParamIdx==-1) || (nextParamIdx != (len-1)) ) {
			return ERROR_FORMAT_NOT_VALID;                                      
		}

		if (contains_letter(arguments[0])) { /* the param. is name and not id. */
			tmp=remove_vertex_by_name(grp, arguments[0]);

			if (tmp==ERROR_VERTEX_CANNOT_BE_DELETED) {
				printf("Error: Cannot delete vertex %s since there are edges connected to it\n", arguments[0]);
				return 0;
			}
			return tmp;
		} else {
			if (is_integer_greater_than(arguments[0],0)) {
				tmp=string_to_integer(arguments[0]);
				tmp2=remove_vertex_by_id(grp, tmp);

				if (tmp2==ERROR_VERTEX_CANNOT_BE_DELETED) {
					printf("Error: Cannot delete vertex %s since there are edges connected to it\n", 
						   (*((*grp).vertices+tmp)).name);
					return 0;
				}
				return tmp2;
			} else {
				return ERROR_FORMAT_NOT_VALID;
			}
		}
	}


	if (strcmp(commandName, "add_edge") == 0) {
		strcpy(arguments[0], "");
		strcpy(arguments[1], "");
		strcpy(arguments[2], "");
		nextParamIdx=next_parameter(command, nextParamIdx+1, arguments[0]);

		if (nextParamIdx==-1) {
			return ERROR_FORMAT_NOT_VALID;                                     
		}

		nextParamIdx=next_parameter(command, nextParamIdx+1, arguments[1]);
		if (nextParamIdx==-1) {
			return ERROR_FORMAT_NOT_VALID;                                     
		}

		nextParamIdx=next_parameter(command, nextParamIdx+1, arguments[2]);
		if (nextParamIdx==-1) {
			return ERROR_FORMAT_NOT_VALID;                                     
		}

		if (nextParamIdx != (len-1)) {
			return ERROR_FORMAT_NOT_VALID;                                     
		}

		if (is_double(arguments[2])==false) {
			return ERROR_WEIGHT_NOT_NUMBER;
		}

		weight=string_to_double(arguments[2]);
		if (weight<0) {
			return ERROR_WEIGHT_NOT_POSITIVE;
		}

		if ((contains_letter(arguments[0])) && (contains_letter(arguments[1])==false)) {
			 return ERROR_FORMAT_NOT_VALID;
		}

		if ((is_integer_greater_than(arguments[0],0)) && (is_integer_greater_than(arguments[1],0)==false)) {
			 return ERROR_VERTEX_ID_NOT_NUMBER;
		}

		if ( ((contains_letter(arguments[0])==false) && (is_integer_greater_than(arguments[0],0)==false)) ||
			 ((contains_letter(arguments[1])==false) && (is_integer_greater_than(arguments[1],0)==false)) ) {
				 return ERROR_FORMAT_NOT_VALID;     /* one of them is neither name nor legal id. */
		}

		if (contains_letter(arguments[0])) {
			return add_edge_by_names(grp, arguments[0], arguments[1], weight);
		} else {
			return add_edge_by_ids(grp, string_to_integer(arguments[0]), string_to_integer(arguments[1]), weight);
		}
	}


	if (strcmp(commandName, "remove_edge_by_id") == 0) {
		strcpy(arguments[0], "");
		nextParamIdx=next_parameter(command, nextParamIdx+1, arguments[0]);

		if ( (nextParamIdx==-1) || (nextParamIdx != (len-1)) ) {
			return ERROR_FORMAT_NOT_VALID;                                     
		}

		if (is_integer_greater_than(arguments[0],0)) {
			return remove_edge_by_id(grp, string_to_integer(arguments[0]));
		} else {
			return ERROR_EDGE_ID_NOT_NUMBER;
		}
	}

	if (strcmp(commandName, "print") == 0) {
		if (nextParamIdx != (len-1)) {
			return ERROR_FORMAT_NOT_VALID;                                  
		}
		print(grp, false);
		return 0;
	}

	if (strcmp(commandName, "cluster") == 0) {
		strcpy(arguments[0], "");
		nextParamIdx=next_parameter(command, nextParamIdx+1, arguments[0]);

		if ( (nextParamIdx==-1) || (nextParamIdx != (len-1)) ) {
			return ERROR_FORMAT_NOT_VALID;                                     
		}

		if (is_integer_greater_than(arguments[0],0)==false) {
				return ERROR_CLUSTERS_NUMBER_NOT_VALID; 
		}

		tmp=string_to_integer(arguments[0]);
		if (tmp < 1) {
			return ERROR_CLUSTERS_NUMBER_NOT_VALID;  
		} else {
			cluster(grp, tmp);
			return 0;
		}
	}

	return  ERROR_CMD_NOT_RECOGNIZED;
}


/* The function go over the text in the file and run the commands */
int get_graph_data_from_file(FILE* fil, graph *grp, int verticesOrEdgesFile) {
	char line[MAX_LEN];
	char cmd[MAX_LEN+10];
	int error;

	while(1) {
		fgets(line, MAX_LEN, fil);
		if (feof(fil)) {
			break;
		}
	
		if (verticesOrEdgesFile==0) {
			strcpy(cmd, "add_vertex ");
		} else {
			strcpy(cmd, "add_edge ");
		}

		strcat(cmd, line);
		error=run_command(grp, cmd);

		if (error != 0) {
			if (error==ERROR_DUPLICATED_EDGES) {
				printf("Error: Duplicate edge was found, and won't be added. ");
				printf("Yet, the graph fill will resume\n");
				continue; 
			}
			if (error==ERROR_INVALID_LOOP) {
				printf("Error: Self loop was found, and won't be added. ");
				printf("Yet, the graph fill will resume\n");
				continue; 
			}
			
			return error;
		}
	}

	return 0;
}

/* The function writes the clustered graph for k=U to the received file */
void write_graph_to_file(graph *grp, FILE* res) {
	int i=0;
	char* name1;
	char* name2;

	if (grp->numOfVertices > 0) {
		fprintf(res, "%d vertices:\n", grp->numOfVertices);
	}

	for(i=0; i<=grp->lastVerIdx; i++) {
		fprintf(res, "%d: %s %d\n", i, (grp->vertices+i)->name, (grp->vertices+i)->cluster);
	}


	if (grp->numOfEdges > 0) {
		fprintf(res, "%d edges:\n", grp->numOfEdges);
	}

	for(i=0; i<=grp->lastEdgIdx; i++) {
		name1=(grp->vertices+(grp->edges+i)->v1_id)->name;
        name2=(grp->vertices+(grp->edges+i)->v2_id)->name;
		fprintf(res, "%d: %s-%s %.3f\n", i, name1, name2, (grp->edges+i)->weight);
	}
}

/* The function prints the recieved error message */
void print_error(int error) {
	switch(error) {
		case ERROR_CMD_NOT_RECOGNIZED: printf("Error: Command is not recognized\n");
			break;
		case ERROR_FORMAT_NOT_VALID: printf("Error: Command format is not valid\n");
			break;
		case ERROR_WEIGHT_NOT_NUMBER: printf("Error: When adding an edge weight must be a number\n");
			break;
		case ERROR_WEIGHT_NOT_POSITIVE: printf("Error: When adding an edge weight must be positive\n");
			break;
		case ERROR_VERTEX_NAME_NOT_VALID: printf("Error: When adding a vertex name must have at least one letter\n");
			break;
		case ERROR_EDGE_ID_NOT_NUMBER: printf("Error: Edge id must be a number\n");
			break;
		case ERROR_VERTEX_ID_NOT_NUMBER: printf("Error: Vertex id must be a number\n");
			break;
		case ERROR_CLUSTERS_NUMBER_NOT_VALID: printf("Error: Number of clusters must be a number bigger or equal to 1\n");
			break;
		case ERROR_MALLOC_FAILED: printf("Error: Memory allocation failed\n");
			break;
		case ERROR_VERTEX_NOT_EXIST: printf("Error: Requested vertex does not exist\n");
		 	break;
		case ERROR_MULTIPLE_VERTICES_NAME: printf("Error: More than one vertex with the requested name exists, please remove using id\n");
			break;
		case ERROR_VERTEX_NOT_FOUND: printf("Error: First/second vertex was not found\n");
			break;
		case ERROR_INVALID_LOOP: printf("Error: No self loops are allowed in the graph\n");
			break;
		case ERROR_DUPLICATED_EDGES: printf("Error: No duplicated edges are allowed in the graph\n");
			break;
		case ERROR_EDGE_NOT_EXIST: printf("Error: Requested edge does not exist\n");
			break;
		default: printf("Error not recognized!\n");
			break;
	}
}

int main(int argc, char *argv[]) {

	int k=0, error=0;
	int numClustersLowerBound, numClustersUpperBound;

	char *nodes_filename=NULL;
	char *edges_filename=NULL;
	char *results_filename=NULL;
	char *xml_filename=NULL;

	FILE *nodes_file=NULL;
	FILE *edges_file=NULL;
	FILE *results_file=NULL;
	/* for CPLEX */
	double objval=0;
	
	xmlDocPtr pXMLDom;
	clusterProperties *clusters = NULL;
	char tmp[LONGEST_STR];

	graph *ptr=NULL;
	graph grp;

	grp.lastVerIdx=-1;
	grp.lastEdgIdx=-1;
	grp.numOfVerticesGroups=0;
	grp.numOfEdgesGroups=0;
	grp.numOfVertices=0;
	grp.numOfEdges=0;
	grp.vertices=NULL;
	grp.edges=NULL;

	ptr=&grp;

	/* Check the number of input arguments. */
	if (argc != 5) {
		printf("Error: Wrong number of input arguments\n");
		goto TERMINATE;
	}

	nodes_filename=(char*)malloc((strlen(argv[1])+FILE_NAME_MAX_LEN)*sizeof(char));
	if (nodes_filename == NULL) {   
		print_error(ERROR_MALLOC_FAILED);  
		goto TERMINATE;
	}
	/* input folder */
	strcpy(nodes_filename, argv[1]);
	strcat(nodes_filename, "/nodes");
	nodes_file = fopen(nodes_filename,"r");
	if (nodes_filename == NULL) {   
		printf("Error: Nodes file could not be opened or the path/file doesn't exist\n");
		goto TERMINATE;
	}


	edges_filename=(char*)malloc((strlen(argv[1])+FILE_NAME_MAX_LEN)*sizeof(char));
	if (edges_filename == NULL) {   
		print_error(ERROR_MALLOC_FAILED);  
		goto TERMINATE;
	}
	/* input folder */
	strcpy(edges_filename, argv[1]);
	strcat(edges_filename, "/edges");
	edges_file = fopen(edges_filename,"r");
	if (edges_filename == NULL) {   
		printf("Error: Edges file could not be opened or the path/file doesn't exist\n");
		goto TERMINATE;
	}


	results_filename=(char*)malloc((strlen(argv[2])+FILE_NAME_MAX_LEN)*sizeof(char));
	if (results_filename == NULL) {   
		print_error(ERROR_MALLOC_FAILED);  
		goto TERMINATE;
	}

	/* output folder */
	strcpy(results_filename, argv[2]);
	strcat(results_filename, "/results");
	results_file = fopen(results_filename,"w");
	if (results_filename == NULL) {   
		printf("Error: Results file could not be opened or the path doesn't exist\n");
		goto TERMINATE;
	}

	/* Check lower and upper bounds */
	if (is_integer_greater_than(argv[3],1) && is_integer_greater_than(argv[4],1)) { 
		numClustersLowerBound=string_to_integer(argv[3]);
	    numClustersUpperBound=string_to_integer(argv[4]);
		if (numClustersUpperBound<numClustersLowerBound) {
			printf("Error: Lower bound for the number of clusters is bigger than the upper bound\n");
			goto TERMINATE;
		}
	} else {
		printf("Error: Lower or upper bound for the number of clusters isn't a positive integer\n");
		goto TERMINATE;
	}

	/* fill graph with vertices */
	error=get_graph_data_from_file(nodes_file, ptr, 0);
	if (error != 0) {   
		print_error(error);  
		goto TERMINATE;
	}

	/* fill graph with edges */
	error=get_graph_data_from_file(edges_file, ptr, 1);
	if (error != 0) {   
		print_error(error);  
		goto TERMINATE;
	}

	/* creating cluster, writing to results file and xml */
	pXMLDom = create_xml(ptr);

	xml_filename=(char*)malloc((strlen(argv[2])+FILE_NAME_MAX_LEN)*sizeof(char));
	if (xml_filename == NULL) {   
		print_error(ERROR_MALLOC_FAILED);  
		goto TERMINATE;
	}

	for (k=numClustersLowerBound; k<=numClustersUpperBound; k++) {
		error=k_cluster(ptr, k, &objval, argv[2]);
		if (error != 0) {
			if (error == ERROR_MALLOC_FAILED) {
				print_error(ERROR_MALLOC_FAILED);
			}
			goto TERMINATE;
		}
		fprintf(results_file,"%d: %.3f\n", k, objval);

		clusters=(clusterProperties*)malloc(k*sizeof(clusterProperties));
		if (clusters==NULL) {
			print_error(ERROR_MALLOC_FAILED);
			goto TERMINATE;
		}

		color_xml_nodes(pXMLDom,ptr,clusters,k);

		/* output folder */
		strcpy(xml_filename, argv[2]);
		sprintf(tmp,"/%d_clustering_solution.xgmml",k);
		strcat(xml_filename,tmp);
		save_xml_doc(pXMLDom,xml_filename,"UTF-8");

		free(clusters);

	}
	

	fprintf(results_file,"\n");

	fprintf(results_file,"The clustered graph for %d:\n", numClustersUpperBound);
	write_graph_to_file(ptr, results_file);

	clusters=(clusterProperties*)malloc(numClustersUpperBound*sizeof(clusterProperties));
	if (clusters==NULL) {
		print_error(ERROR_MALLOC_FAILED);
		goto TERMINATE;
	}

	remove_xml_small_clusters(pXMLDom,ptr,clusters,numClustersUpperBound);
	free(clusters);
	

	strcpy(xml_filename, argv[2]); /* output folder */
	strcat(xml_filename,"/best_clusters.xgmml");
	save_xml_doc(pXMLDom,xml_filename,"UTF-8");

	free_xml_doc(pXMLDom);


TERMINATE:

	/* close open files and free the memory */

	free_pointer_memory(nodes_filename);
	free_pointer_memory(edges_filename);
	free_pointer_memory(results_filename);
	free_pointer_memory(xml_filename);

	if (nodes_file!=NULL) {
		fclose(nodes_file);   
	}
	if (edges_file!=NULL) {
		fclose(edges_file);  
	}
	if (results_file!=NULL) {
		fclose(results_file);
	}

	/* free the graph memory */
	for(k=0; k<=grp.lastVerIdx; k++) {
		if ( (grp.vertices+k)->deleted == false ) {
			free_pointer_memory( (grp.vertices+k)->name );
		}
	}
	
	free_pointer_memory( grp.vertices );
	free_pointer_memory( grp.edges );

	return 0;
}
