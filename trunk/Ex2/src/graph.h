/*Full Name 1: Or Segal; Id No 1: 203993118; User Name 1: orsegal*/
/*Full Name 2: Aviv Mor; Id No 2: 201254059; User Name 2: avivmor*/
#ifndef GRAPH_H
#define GRAPH_H

#include "types.h"

/* Include declaration for graph methods */

void add_vertex(const char* name);

void remove_vertex_by_id(int id);

void remove_vertex_by_name(char* name);

void add_edge_by_name(char* first_vertex_name, char* second_vertex_name, double weight);

void add_edge_by_id(int head_vertex_id, int tail_vertex_id, double weight);

void remove_edge(int id);

void print();

void cluster(int num_of_clusters);

void free_and_quit();

void print_error(char* error);

int next_ver();

int next_ed();

int search_ver(char* name);

bool edge_duplication(int id1, int id2);

bool ver_exist(int id);

bool ed_exist(int id);

bool edges_attached(int id);

#endif
