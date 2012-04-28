#ifndef GRAPHICS_H
#define GRAPHICS_H

#include <stdio.h>
#include <string.h>
#include <libxml/tree.h>

#include "graph.h"

#define WIDTH "4"
#define TYPE "ELLIPSE"
#define LINE "solid"
#define THRESHOLD 5
#define LONGEST_NUM 100
#define LONGEST_STR 300


typedef struct clst {
	int id;
	/* to deal with equal cluster sizes. */
	int order;
	/* number of vertices. */
	int size;
} Cluster;

void xmlSaveDoc(xmlDocPtr, const char*, const char*);

void xmlFreeDocument(xmlDocPtr);

xmlDocPtr generateXML(graph*);

void paintXML(xmlDocPtr, graph*, Cluster*,int);

void trimXML(xmlDocPtr, graph*, Cluster*, int);

#endif
