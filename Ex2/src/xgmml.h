#ifndef GRAPHICS_H
#define GRAPHICS_H

#include <stdio.h>
#include <string.h>
#include <libxml/tree.h>

#include "graph.h"

#define GRAPHICS_WIDTH "4"
#define GRAPHICS_TYPE "ELLIPSE"
#define GRAPHICS_BORDER "solid"
#define THRESHOLD 5
#define LONGEST_NUM 100
#define LONGEST_STR 300

typedef struct clst {
	int idNum;
	int orderNum;
	int numOfVertices;
} clusterProperties;

xmlDocPtr create_xml(graph*);
void color_xml_nodes(xmlDocPtr, graph*, clusterProperties*,int);
void remove_xml_small_clusters(xmlDocPtr, graph*, clusterProperties*, int);
void save_xml_doc(xmlDocPtr, const char*, const char*);
void free_xml_doc(xmlDocPtr);

#endif
