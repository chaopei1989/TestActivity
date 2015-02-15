#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../su.h"

//int cmd_main(int, char **);


#define TOOL(name) int name##_main(int, char**);
#include "tools.h"
#undef TOOL

static struct 
{
    const char *name;
    int (*func)(int, char**);
} tools[] = {
#define TOOL(name) { #name, name##_main },
#include "tools.h"
#undef TOOL
    { 0, 0 },
};

int cmd_main(int argc, char **argv)
{
    int i;
    char *name = argv[0];

    if((argc > 1) && (argv[1][0] == '@')) {
        name = argv[1] + 1;
        argc--;
        argv++;
    } else {
        char *cmd = strrchr(argv[0], '/');
        if (cmd)
            name = cmd + 1;
    }

    for(i = 0; tools[i].name; i++){
        if(!strcmp(tools[i].name, name)){
			PLOGV("find-------cmd %s",argv[0]);
            return tools[i].func(argc, argv);
        }
    }

    //printf("%s: no such tool\n", argv[0]);
    return -100;
}
