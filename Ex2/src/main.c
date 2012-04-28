#include "graph.h"
#include "cluster.h"
#include "graphics.h"  

#define MAX_LEN 305
#define FILE_NAME_MAX_LEN 50

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

/* converts str to integer, assuming str represents an integer. */
int string_to_integer(char* str) { 
	int res=0;
	sscanf(str, "%d", &res);
	return res;
}

/* returns true iff str represents double. */
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

/* converts str to double, assuming str represents double. */
double string_to_double(char* str) {  
	double res=0;
	sscanf(str, "%lf", &res);
	return res;
}

/* res will get next word in str, starting at index ind (ignoring white-spaces),
   or an empty string, if a word wasn't found. 
   the returned value - index of the last char of the next word in str, or -1 if no word found. */
int next_word(char* str, int ind, char* res) { 
	int i=0;
	
	while ( (isspace(str[ind])) && (ind<(int)strlen(str)-1) ) {  
		/* till non white-space or till char '\n' */ 
		ind++;
	}

	if (ind==(int)strlen(str)-1) {  /* we didn't find a word, */
		return -1;                 /* res is empty string. */
	}

	while ( (isspace(str[ind]) == false) && (ind<(int)strlen(str)-1) ) {  
		/* copying the word to res. */
		res[i]=str[ind]; 
		ind++;
		i++;
	}

	res[i]='\0';

	return (ind-1); /* last index of word in str. */
}


/* input string parsing. */
int graph_input_handle(graph* grp, char* input) {
	int len=(int)strlen(input)-1;  /* without '\n' char at the end. */
	int tmp=0, tmp2=0, nextWordInd=0;
	double weight=0;
	char command[MAX_LEN]="";
	char params[3][MAX_LEN];  /* we have at most 3 parameters. */

	if (input[0]=='\n') {   /* empty input line. */
		return ERROR_CMD_NOT_RECOGNIZED;
	}

	if (isspace(input[0])) { /* input starts with white-space. */
		return ERROR_CMD_NOT_RECOGNIZED;
	}

	if (isspace(input[len-1])) {  /* input ends with white-space. */
		return ERROR_FORMAT_NOT_VALID;
	}

	nextWordInd=next_word(input, 0, command); /* getting the first word -> command name. */

	if (strcmp(command, "add_vertex") == 0) { 
		strcpy(params[0], "");   
		nextWordInd=next_word(input, nextWordInd+1, params[0]);   
		/* getting the first (and only) parameter. */
		if ( (nextWordInd==-1) || (nextWordInd != (len-1)) ) {     
			return ERROR_FORMAT_NOT_VALID;                                     
		} /* there isn't first param., or there are more input beyond the first param. */

		if (contains_letter(params[0]) == false) { /* name of vertex doesn't have a letter. */
			return ERROR_VERTEX_NAME_NOT_VALID;
		}

		return add_vertex(grp, params[0]);  /* calling the proc. */
	}


	if (strcmp(command, "remove_vertex") == 0) { 
		strcpy(params[0], "");
		nextWordInd=next_word(input, nextWordInd+1, params[0]);      
		/* getting the first (and only) parameter/ */
		if ( (nextWordInd==-1) || (nextWordInd != (len-1)) ) {      
			return ERROR_FORMAT_NOT_VALID;                                      
		}  /* there isn't first param., or there are more input beyond the first param. */

		if (contains_letter(params[0])) { /* the param. is name and not id. */
			tmp=remove_vertex_by_name(grp, params[0]);
			if (tmp==ERROR_VERTEX_CANNOT_BE_DELETED) {
				printf("Error: Cannot delete vertex %s since there are edges connected to it\n", params[0]);
				return 0;
			}
			return tmp;
		} else { /* the param. is id, or illegal. */
			if (is_integer_greater_than(params[0],0)) {
				tmp=string_to_integer(params[0]); /* the id. */
				tmp2=remove_vertex_by_id(grp, tmp); /* the error. */
				if (tmp2==ERROR_VERTEX_CANNOT_BE_DELETED) {
					printf("Error: Cannot delete vertex %s since there are edges connected to it\n", 
						   (*((*grp).vertices+tmp)).name);
					return 0;
				}
				return tmp2;
			} else {  /* not name and not id. */
				return ERROR_FORMAT_NOT_VALID;
			}
		}
	}


	if (strcmp(command, "add_edge") == 0) { 
		strcpy(params[0], "");
		strcpy(params[1], "");
		strcpy(params[2], "");
		nextWordInd=next_word(input, nextWordInd+1, params[0]);     
		if (nextWordInd==-1) {  /* there isn't first param. */
			return ERROR_FORMAT_NOT_VALID;                                     
		}
		nextWordInd=next_word(input, nextWordInd+1, params[1]);     
		if (nextWordInd==-1) {  /* there isn't second param. */
			return ERROR_FORMAT_NOT_VALID;                                     
		}
		nextWordInd=next_word(input, nextWordInd+1, params[2]);     
		if (nextWordInd==-1) {  /* there isn't third param. */
			return ERROR_FORMAT_NOT_VALID;                                     
		}

		if (nextWordInd != (len-1)) { /* there are more input beyond the last param. */
			return ERROR_FORMAT_NOT_VALID;                                     
		}

		/* checking the weight. */
		if (is_double(params[2])==false) {
			return ERROR_WEIGHT_NOT_NUMBER;
		}
		weight=string_to_double(params[2]);
		if (weight<0) {
			return ERROR_WEIGHT_NOT_POSITIVE;
		}

		/* checking first two params. */
		if ((contains_letter(params[0])) && (contains_letter(params[1])==false)) {  
			/* 1st is legal name, but not the 2nd. */
			 return ERROR_FORMAT_NOT_VALID;
		}

		if ((is_integer_greater_than(params[0],0)) && (is_integer_greater_than(params[1],0)==false)) { 
			/* 1st is legal id, but not the 2nd. */
			 return ERROR_VERTEX_ID_NOT_NUMBER;
		}

		if ( ((contains_letter(params[0])==false) && (is_integer_greater_than(params[0],0)==false)) ||
			 ((contains_letter(params[1])==false) && (is_integer_greater_than(params[1],0)==false)) ) {
				 return ERROR_FORMAT_NOT_VALID;     /* one of them is neither name nor legal id. */
		}

		if (contains_letter(params[0])) { /* they both are names, from prev. conditions. */
			return add_edge_by_names(grp, params[0], params[1], weight);
		} else {      /* they both are ids, from prev. conditions. */
			return add_edge_by_ids(grp, string_to_integer(params[0]), string_to_integer(params[1]), weight);
		}
	}


	if (strcmp(command, "remove_edge_by_id") == 0) { 
		strcpy(params[0], "");
		nextWordInd=next_word(input, nextWordInd+1, params[0]);         
		/* getting the first (and only) parameter. */
		if ( (nextWordInd==-1) || (nextWordInd != (len-1)) ) {         
			return ERROR_FORMAT_NOT_VALID;                                     
		} /* there isn't first param., or there are more input beyond the first param. */

		if (is_integer_greater_than(params[0],0)) {
			return remove_edge_by_id(grp, string_to_integer(params[0]));
		} else {
			return ERROR_EDGE_ID_NOT_NUMBER;
		}
	}


	if (strcmp(command, "print") == 0) {       
		if (nextWordInd != (len-1)) {  /* there are more input beyond the command. */
			return ERROR_FORMAT_NOT_VALID;                                  
		}
		print(grp, false);
		return 0;
	}


	if (strcmp(command, "cluster") == 0) { 
		strcpy(params[0], "");
		nextWordInd=next_word(input, nextWordInd+1, params[0]);       
		/* getting the first (and only) parameter */
		if ( (nextWordInd==-1) || (nextWordInd != (len-1)) ) {      
			return ERROR_FORMAT_NOT_VALID;                                     
		} /* there isn't first param., or there are more input beyond the first param. */

		if (is_integer_greater_than(params[0],0)==false) {
				return ERROR_CLUSTERS_NUMBER_NOT_VALID; 
		}
		tmp=string_to_integer(params[0]);
		if (tmp < 1) {
			return ERROR_CLUSTERS_NUMBER_NOT_VALID;  
		} else {
			cluster(grp, tmp);
			return 0;
		}
	}

	return  ERROR_CMD_NOT_RECOGNIZED;  /* non of the commands above. */ 
}


/* will read from nodes/edges input files and fill the graph
   VorE - which file is it - nodes (0) or edges (1) */
int fill_graph(FILE* fil, graph *grp, int VorE) {
	char line[MAX_LEN];
	char cmd[MAX_LEN+10];
	int error;

	while(1) {
		fgets(line, MAX_LEN, fil);
		if (feof(fil)) {   /* the last, 'empty line', was read */
			break;
		}
	
		if (VorE==0) {
			strcpy(cmd, "add_vertex ");
		} else {
			strcpy(cmd, "add_edge ");
		}
		strcat(cmd, line);   /* the full command. */
		error=graph_input_handle(grp, cmd);
		if (error != 0) {   /* input correctness */

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
			
			return error; /* 'critical' error */        
		}
	}

	return 0; /* no errors. */
}

/* will write to the results file the clustered graph for k=U */
void graphToFile(graph *grp, FILE* res) {
	int i=0;
	char* name1;
	char* name2;

	if (grp->numVer > 0) {
		fprintf(res, "%d vertices:\n", grp->numVer);
	}

	for(i=0; i<=grp->lastVerIdx; i++) {
		fprintf(res, "%d: %s %d\n", i, (grp->vertices+i)->name, (grp->vertices+i)->cluster);
	}


	if (grp->numEdg > 0) {
		fprintf(res, "%d edges:\n", grp->numEdg);
	}

	for(i=0; i<=grp->lastEdgIdx; i++) {
		name1=(grp->vertices+(grp->edges+i)->v1_id)->name;
        name2=(grp->vertices+(grp->edges+i)->v2_id)->name;
		fprintf(res, "%d: %s-%s %.3f\n", i, name1, name2, (grp->edges+i)->weight);
	}
}

/* simple routine that frees pointer ptr, if it isn't null. */
void free_memory (void *ptr) {
   if (ptr != NULL) {
      free(ptr);
   }
} 


int main(int argc, char *argv[]) {

	int k=0, error=0, L, U;
	char *nodes_filename=NULL, *edges_filename=NULL, *results_filename=NULL;
	char *xml_filename=NULL;
	double objval=0;  /* for CPLEX */
	FILE *nodes_file=NULL, *edges_file=NULL, *results_file=NULL;
	
	/* for xml */
	xmlDocPtr pXMLDom;
	Cluster *clusters = NULL;
	char tmp[LONGEST_STR];

	graph *ptr=NULL;
	graph grp;

	grp.lastVerIdx=-1;    /* init. */
	grp.lastEdgIdx=-1;
	grp.verLen=0;
	grp.edgLen=0;
	grp.numVer=0;
	grp.numEdg=0;
	grp.vertices=NULL;
	grp.edges=NULL;

	ptr=&grp;

	if (argc != 5) { /* wrong amount of input args. */
		printf("Error: Wrong number of input arguments\n");
		goto TERMINATE;
	}

	nodes_filename=(char*)malloc((strlen(argv[1])+FILE_NAME_MAX_LEN)*sizeof(char));
	if (nodes_filename == NULL) {   
		print_error(ERROR_MALLOC_FAILED);  
		goto TERMINATE;
	}
	strcpy(nodes_filename, argv[1]); /* input folder */
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
	strcpy(edges_filename, argv[1]); /* input folder */
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
	strcpy(results_filename, argv[2]); /* output folder */
	strcat(results_filename, "/results");
	results_file = fopen(results_filename,"w");
	if (results_filename == NULL) {   
		printf("Error: Results file could not be opened or the path doesn't exist\n");
		goto TERMINATE;
	}


	if (is_integer_greater_than(argv[3],1) && is_integer_greater_than(argv[4],1)) { 
		/* lower and upper bounds are both positive integers */
		L=string_to_integer(argv[3]);
	    U=string_to_integer(argv[4]);
		if (U<L) {  
			printf("Error: Lower bound for the number of clusters is bigger than the upper bound\n");
			goto TERMINATE;
		}
	} else {
		printf("Error: Lower or upper bound for the number of clusters isn't a positive integer\n");
		goto TERMINATE;
	}


	error=fill_graph(nodes_file, ptr, 0);  /* reading nodes to graph. */
	if (error != 0) {   
		print_error(error);  
		goto TERMINATE;
	}

	error=fill_graph(edges_file, ptr, 1);  /* reading edges to graph. */
	if (error != 0) {   
		print_error(error);  
		goto TERMINATE;
	}


	/* calculating the clustering and writing to results file,
	   generating xml doc. (tree) and making xml files. */

	pXMLDom = generateXML(ptr);

	xml_filename=(char*)malloc((strlen(argv[2])+FILE_NAME_MAX_LEN)*sizeof(char));
	if (xml_filename == NULL) {   
		print_error(ERROR_MALLOC_FAILED);  
		goto TERMINATE;
	}

	for (k=L; k<=U; k++) {
		error=k_cluster(ptr, k, &objval, argv[2]);
		if (error != 0) {
			if (error == ERROR_MALLOC_FAILED) { /* other errors printed in cluster.c */
				print_error(ERROR_MALLOC_FAILED);
			}
			goto TERMINATE;
		}
		fprintf(results_file,"%d: %.3f\n", k, objval); /* clustering scores */

		/* making xml file for current k */
		clusters=(Cluster*)malloc(k*sizeof(Cluster));
		if (clusters==NULL) {
			print_error(ERROR_MALLOC_FAILED);
			goto TERMINATE;
		}

		paintXML(pXMLDom,ptr,clusters,k);

		strcpy(xml_filename, argv[2]); /* output folder */
		sprintf(tmp,"/%d_clustering_solution.xgmml",k);
		strcat(xml_filename,tmp);
		xmlSaveDoc(pXMLDom,xml_filename,"UTF-8");

		free(clusters);

	} /* end of for. */
	

	fprintf(results_file,"\n");

	/* clustered graph for k=U */
	fprintf(results_file,"The clustered graph for %d:\n", U);
	graphToFile(ptr, results_file);

	/* xml best clusters for k=U */

	clusters=(Cluster*)malloc(U*sizeof(Cluster));
	if (clusters==NULL) {
		print_error(ERROR_MALLOC_FAILED);
		goto TERMINATE;
	}

	trimXML(pXMLDom,ptr,clusters,U);
	free(clusters);
	

	strcpy(xml_filename, argv[2]); /* output folder */
	strcat(xml_filename,"/best_clusters.xgmml");
	xmlSaveDoc(pXMLDom,xml_filename,"UTF-8");

	xmlFreeDocument(pXMLDom);


TERMINATE:

	/* freeing memory and closing files */

	free_memory(nodes_filename);
	free_memory(edges_filename);
	free_memory(results_filename);
	free_memory(xml_filename);

	if (nodes_file!=NULL) {
		fclose(nodes_file);   
	}
	if (edges_file!=NULL) {
		fclose(edges_file);  
	}
	if (results_file!=NULL) {
		fclose(results_file);
	}

	/* freeing the graph */

	for(k=0; k<=grp.lastVerIdx; k++) {
		if ( (grp.vertices+k)->deleted == false ) {
			free_memory( (grp.vertices+k)->name );
		}
	}
	
	free_memory( grp.vertices );
	free_memory( grp.edges );

	return 0;

}
