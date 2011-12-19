#ifndef TYPES_H
#define TYPES_H

/* Include declaration for boolean types */
#define false 0
#define true (!false)
#define bool int

/* Include declaration for struct types */
typedef struct vertex_t
{
	char*	name;
	int		cluster;
	bool	deleted;
}vertex;

typedef struct edge_t
{
	int		v1_id;
	int		v2_id;
	double	weight;
	bool	deleted;
}edge;

#endif