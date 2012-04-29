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

xmlDocPtr generate_xml(graph*);
void paint_xml(xmlDocPtr, graph*, clusterProperties*,int);
void trim_xml(xmlDocPtr, graph*, clusterProperties*, int);
void xml_save_doc(xmlDocPtr, const char*, const char*);
void xml_free_doc(xmlDocPtr);

#endif
