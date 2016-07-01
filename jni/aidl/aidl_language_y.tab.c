
/*  A Bison parser, made from aidl_language_y.y
    by GNU Bison version 1.28  */

#define YYBISON 1  /* Identify Bison output.  */

#define	IMPORT	257
#define	PACKAGE	258
#define	IDENTIFIER	259
#define	IDVALUE	260
#define	GENERIC	261
#define	ARRAY	262
#define	PARCELABLE	263
#define	INTERFACE	264
#define	FLATTENABLE	265
#define	RPC	266
#define	IN	267
#define	OUT	268
#define	INOUT	269
#define	ONEWAY	270

#line 1 "aidl_language_y.y"

#include "aidl_language.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int yyerror(char* errstr);
int yylex(void);
extern int yylineno;

static int count_brackets(const char*);

#ifndef YYSTYPE
#define YYSTYPE int
#endif
#include <stdio.h>

#ifndef __cplusplus
#ifndef __STDC__
#define const
#endif
#endif



#define	YYFINAL		78
#define	YYFLAG		-32768
#define	YYNTBASE	24

#define YYTRANSLATE(x) ((unsigned)(x) <= 270 ? yytranslate[x] : 40)

static const char yytranslate[] = {     0,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,    20,
    21,     2,     2,    23,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,    17,     2,
    22,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,    18,     2,    19,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
     2,     2,     2,     2,     2,     1,     3,     4,     5,     6,
     7,     8,     9,    10,    11,    12,    13,    14,    15,    16
};

#if YYDEBUG != 0
static const short yyprhs[] = {     0,
     0,     2,     5,     7,     9,    12,    14,    16,    19,    20,
    23,    26,    28,    30,    34,    37,    41,    45,    48,    52,
    54,    57,    59,    61,    63,    69,    75,    79,    80,    83,
    87,    94,   102,   111,   121,   122,   124,   128,   130,   134,
   136,   139,   141,   142,   144,   146
};

static const short yyrhs[] = {    28,
     0,    25,    28,     0,    26,     0,    27,     0,    26,    27,
     0,     4,     0,     3,     0,     3,    27,     0,     0,    28,
    29,     0,    28,     1,     0,    30,     0,    33,     0,     9,
     5,    17,     0,     9,    17,     0,     9,     1,    17,     0,
    11,     5,    17,     0,    11,    17,     0,    11,     1,    17,
     0,    10,     0,    16,    10,     0,    12,     0,    10,     0,
    12,     0,    31,     5,    18,    34,    19,     0,    32,     1,
    18,    34,    19,     0,    32,     1,    19,     0,     0,    34,
    35,     0,    34,     1,    17,     0,    38,     5,    20,    36,
    21,    17,     0,    16,    38,     5,    20,    36,    21,    17,
     0,    38,     5,    20,    36,    21,    22,     6,    17,     0,
    16,    38,     5,    20,    36,    21,    22,     6,    17,     0,
     0,    37,     0,    36,    23,    37,     0,     1,     0,    39,
    38,     5,     0,     5,     0,     5,     8,     0,     7,     0,
     0,    13,     0,    14,     0,    15,     0
};

#endif

#if YYDEBUG != 0
static const short yyrline[] = { 0,
    31,    33,    36,    38,    39,    42,    46,    48,    51,    53,
    70,    77,    79,    82,    94,    99,   104,   115,   120,   128,
   139,   149,   161,   163,   166,   176,   181,   189,   191,   203,
   210,   229,   247,   265,   285,   287,   288,   300,   306,   318,
   324,   329,   336,   338,   339,   340
};
#endif


#if YYDEBUG != 0 || defined (YYERROR_VERBOSE)

static const char * const yytname[] = {   "$","error","$undefined.","IMPORT",
"PACKAGE","IDENTIFIER","IDVALUE","GENERIC","ARRAY","PARCELABLE","INTERFACE",
"FLATTENABLE","RPC","IN","OUT","INOUT","ONEWAY","';'","'{'","'}'","'('","')'",
"'='","','","document","headers","package","imports","document_items","declaration",
"parcelable_decl","interface_header","interface_keywords","interface_decl","interface_items",
"method_decl","arg_list","arg","type","direction", NULL
};
#endif

static const short yyr1[] = {     0,
    24,    24,    25,    25,    25,    26,    27,    27,    28,    28,
    28,    29,    29,    30,    30,    30,    30,    30,    30,    31,
    31,    31,    32,    32,    33,    33,    33,    34,    34,    34,
    35,    35,    35,    35,    36,    36,    36,    36,    37,    38,
    38,    38,    39,    39,    39,    39
};

static const short yyr2[] = {     0,
     1,     2,     1,     1,     2,     1,     1,     2,     0,     2,
     2,     1,     1,     3,     2,     3,     3,     2,     3,     1,
     2,     1,     1,     1,     5,     5,     3,     0,     2,     3,
     6,     7,     8,     9,     0,     1,     3,     1,     3,     1,
     2,     1,     0,     1,     1,     1
};

static const short yydefact[] = {     9,
     7,     6,     9,     3,     4,     0,     8,     0,     5,    11,
     0,    20,     0,    22,     0,    10,    12,     0,     0,    13,
     0,     0,    15,     0,     0,    18,    21,     0,     0,    16,
    14,    19,    17,    28,    28,    27,     0,     0,     0,    40,
    42,     0,    25,    29,     0,    26,    30,    41,     0,     0,
     0,     0,     0,    38,    44,    45,    46,     0,    36,     0,
     0,     0,    43,     0,     0,    31,     0,    37,    39,    32,
     0,     0,     0,    33,    34,     0,     0,     0
};

static const short yydefgoto[] = {    76,
     3,     4,     5,     6,    16,    17,    18,    19,    20,    37,
    44,    58,    59,    45,    60
};

static const short yypact[] = {    59,
    15,-32768,-32768,    15,-32768,    23,-32768,    36,-32768,-32768,
    26,     1,    37,    63,    18,-32768,-32768,    46,    64,-32768,
    43,    51,-32768,    52,    53,-32768,-32768,    48,    22,-32768,
-32768,-32768,-32768,-32768,-32768,-32768,     0,    10,    54,    65,
-32768,    50,-32768,-32768,    62,-32768,-32768,-32768,    67,    55,
    56,    -1,    -1,-32768,-32768,-32768,-32768,    35,-32768,    50,
    38,     8,    -5,    69,    27,-32768,    71,-32768,-32768,-32768,
    72,    66,    68,-32768,-32768,    79,    80,-32768
};

static const short yypgoto[] = {-32768,
-32768,-32768,    49,    78,-32768,-32768,-32768,-32768,-32768,    47,
-32768,    31,    24,   -39,-32768
};


#define	YYLAST		87


static const short yytable[] = {    54,
    39,   -23,    49,   -43,    40,   -43,    41,    55,    56,    57,
    39,    55,    56,    57,    40,    42,    41,     1,    43,   -35,
    64,   -35,    -1,    10,    66,    42,    21,    27,    46,    67,
    22,    11,    12,    13,    14,    -2,    10,    24,    15,    35,
    36,    25,    23,    70,    11,    12,    13,    14,    71,     7,
    28,    15,     9,    26,    40,    62,    41,    63,    65,    30,
    63,     1,     2,   -24,    29,    34,    50,    31,    32,    33,
    47,    51,    48,    69,    52,    53,    72,    73,    77,    78,
     8,    38,    74,    61,    75,     0,    68
};

static const short yycheck[] = {     1,
     1,     1,    42,     5,     5,     7,     7,    13,    14,    15,
     1,    13,    14,    15,     5,    16,     7,     3,    19,    21,
    60,    23,     0,     1,    17,    16,     1,    10,    19,    22,
     5,     9,    10,    11,    12,     0,     1,     1,    16,    18,
    19,     5,    17,    17,     9,    10,    11,    12,    22,     1,
     5,    16,     4,    17,     5,    21,     7,    23,    21,    17,
    23,     3,     4,     1,     1,    18,     5,    17,    17,    17,
    17,     5,     8,     5,    20,    20,     6,     6,     0,     0,
     3,    35,    17,    53,    17,    -1,    63
};
/* -*-C-*-  Note some compilers choke on comments on `#line' lines.  */
#line 3 "/usr/local/share/bison.simple"
/* This file comes from bison-1.28.  */

/* Skeleton output parser for bison,
   Copyright (C) 1984, 1989, 1990 Free Software Foundation, Inc.

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2, or (at your option)
   any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place - Suite 330,
   Boston, MA 02111-1307, USA.  */

/* As a special exception, when this file is copied by Bison into a
   Bison output file, you may use that output file without restriction.
   This special exception was added by the Free Software Foundation
   in version 1.24 of Bison.  */

/* This is the parser code that is written into each bison parser
  when the %semantic_parser declaration is not specified in the grammar.
  It was written by Richard Stallman by simplifying the hairy parser
  used when %semantic_parser is specified.  */

#ifndef YYSTACK_USE_ALLOCA
#ifdef alloca
#define YYSTACK_USE_ALLOCA
#else /* alloca not defined */
#ifdef __GNUC__
#define YYSTACK_USE_ALLOCA
#define alloca __builtin_alloca
#else /* not GNU C.  */
#if (!defined (__STDC__) && defined (sparc)) || defined (__sparc__) || defined (__sparc) || defined (__sgi) || (defined (__sun) && defined (__i386))
#define YYSTACK_USE_ALLOCA
#include <alloca.h>
#else /* not sparc */
/* We think this test detects Watcom and Microsoft C.  */
/* This used to test MSDOS, but that is a bad idea
   since that symbol is in the user namespace.  */
#if (defined (_MSDOS) || defined (_MSDOS_)) && !defined (__TURBOC__)
#if 0 /* No need for malloc.h, which pollutes the namespace;
	 instead, just don't use alloca.  */
#include <malloc.h>
#endif
#else /* not MSDOS, or __TURBOC__ */
#if defined(_AIX)
/* I don't know what this was needed for, but it pollutes the namespace.
   So I turned it off.   rms, 2 May 1997.  */
/* #include <malloc.h>  */
 #pragma alloca
#define YYSTACK_USE_ALLOCA
#else /* not MSDOS, or __TURBOC__, or _AIX */
#if 0
#ifdef __hpux /* haible@ilog.fr says this works for HPUX 9.05 and up,
		 and on HPUX 10.  Eventually we can turn this on.  */
#define YYSTACK_USE_ALLOCA
#define alloca __builtin_alloca
#endif /* __hpux */
#endif
#endif /* not _AIX */
#endif /* not MSDOS, or __TURBOC__ */
#endif /* not sparc */
#endif /* not GNU C */
#endif /* alloca not defined */
#endif /* YYSTACK_USE_ALLOCA not defined */

#ifdef YYSTACK_USE_ALLOCA
#define YYSTACK_ALLOC alloca
#else
#define YYSTACK_ALLOC malloc
#endif

/* Note: there must be only one dollar sign in this file.
   It is replaced by the list of actions, each action
   as one case of the switch.  */

#define yyerrok		(yyerrstatus = 0)
#define yyclearin	(yychar = YYEMPTY)
#define YYEMPTY		-2
#define YYEOF		0
#define YYACCEPT	goto yyacceptlab
#define YYABORT 	goto yyabortlab
#define YYERROR		goto yyerrlab1
/* Like YYERROR except do call yyerror.
   This remains here temporarily to ease the
   transition to the new meaning of YYERROR, for GCC.
   Once GCC version 2 has supplanted version 1, this can go.  */
#define YYFAIL		goto yyerrlab
#define YYRECOVERING()  (!!yyerrstatus)
#define YYBACKUP(token, value) \
do								\
  if (yychar == YYEMPTY && yylen == 1)				\
    { yychar = (token), yylval = (value);			\
      yychar1 = YYTRANSLATE (yychar);				\
      YYPOPSTACK;						\
      goto yybackup;						\
    }								\
  else								\
    { yyerror ("syntax error: cannot back up"); YYERROR; }	\
while (0)

#define YYTERROR	1
#define YYERRCODE	256

#ifndef YYPURE
#define YYLEX		yylex()
#endif

#ifdef YYPURE
#ifdef YYLSP_NEEDED
#ifdef YYLEX_PARAM
#define YYLEX		yylex(&yylval, &yylloc, YYLEX_PARAM)
#else
#define YYLEX		yylex(&yylval, &yylloc)
#endif
#else /* not YYLSP_NEEDED */
#ifdef YYLEX_PARAM
#define YYLEX		yylex(&yylval, YYLEX_PARAM)
#else
#define YYLEX		yylex(&yylval)
#endif
#endif /* not YYLSP_NEEDED */
#endif

/* If nonreentrant, generate the variables here */

#ifndef YYPURE

int	yychar;			/*  the lookahead symbol		*/
YYSTYPE	yylval;			/*  the semantic value of the		*/
				/*  lookahead symbol			*/

#ifdef YYLSP_NEEDED
YYLTYPE yylloc;			/*  location data for the lookahead	*/
				/*  symbol				*/
#endif

int yynerrs;			/*  number of parse errors so far       */
#endif  /* not YYPURE */

#if YYDEBUG != 0
int yydebug;			/*  nonzero means print parse trace	*/
/* Since this is uninitialized, it does not stop multiple parsers
   from coexisting.  */
#endif

/*  YYINITDEPTH indicates the initial size of the parser's stacks	*/

#ifndef	YYINITDEPTH
#define YYINITDEPTH 200
#endif

/*  YYMAXDEPTH is the maximum size the stacks can grow to
    (effective only if the built-in stack extension method is used).  */

#if YYMAXDEPTH == 0
#undef YYMAXDEPTH
#endif

#ifndef YYMAXDEPTH
#define YYMAXDEPTH 10000
#endif

/* Define __yy_memcpy.  Note that the size argument
   should be passed with type unsigned int, because that is what the non-GCC
   definitions require.  With GCC, __builtin_memcpy takes an arg
   of type size_t, but it can handle unsigned int.  */

#if __GNUC__ > 1		/* GNU C and GNU C++ define this.  */
#define __yy_memcpy(TO,FROM,COUNT)	__builtin_memcpy(TO,FROM,COUNT)
#else				/* not GNU C or C++ */
#ifndef __cplusplus

/* This is the most reliable way to avoid incompatibilities
   in available built-in functions on various systems.  */
static void
__yy_memcpy (to, from, count)
     char *to;
     char *from;
     unsigned int count;
{
  register char *f = from;
  register char *t = to;
  register int i = count;

  while (i-- > 0)
    *t++ = *f++;
}

#else /* __cplusplus */

/* This is the most reliable way to avoid incompatibilities
   in available built-in functions on various systems.  */
static void
__yy_memcpy (char *to, char *from, unsigned int count)
{
  register char *t = to;
  register char *f = from;
  register int i = count;

  while (i-- > 0)
    *t++ = *f++;
}

#endif
#endif

#line 217 "/usr/local/share/bison.simple"

/* The user can define YYPARSE_PARAM as the name of an argument to be passed
   into yyparse.  The argument should have type void *.
   It should actually point to an object.
   Grammar actions can access the variable by casting it
   to the proper pointer type.  */

#ifdef YYPARSE_PARAM
#ifdef __cplusplus
#define YYPARSE_PARAM_ARG void *YYPARSE_PARAM
#define YYPARSE_PARAM_DECL
#else /* not __cplusplus */
#define YYPARSE_PARAM_ARG YYPARSE_PARAM
#define YYPARSE_PARAM_DECL void *YYPARSE_PARAM;
#endif /* not __cplusplus */
#else /* not YYPARSE_PARAM */
#define YYPARSE_PARAM_ARG
#define YYPARSE_PARAM_DECL
#endif /* not YYPARSE_PARAM */

/* Prevent warning if -Wstrict-prototypes.  */
#ifdef __GNUC__
#ifdef YYPARSE_PARAM
int yyparse (void *);
#else
int yyparse (void);
#endif
#endif

int
yyparse(YYPARSE_PARAM_ARG)
     YYPARSE_PARAM_DECL
{
  register int yystate;
  register int yyn;
  register short *yyssp;
  register YYSTYPE *yyvsp;
  int yyerrstatus;	/*  number of tokens to shift before error messages enabled */
  int yychar1 = 0;		/*  lookahead token as an internal (translated) token number */

  short	yyssa[YYINITDEPTH];	/*  the state stack			*/
  YYSTYPE yyvsa[YYINITDEPTH];	/*  the semantic value stack		*/

  short *yyss = yyssa;		/*  refer to the stacks thru separate pointers */
  YYSTYPE *yyvs = yyvsa;	/*  to allow yyoverflow to reallocate them elsewhere */

#ifdef YYLSP_NEEDED
  YYLTYPE yylsa[YYINITDEPTH];	/*  the location stack			*/
  YYLTYPE *yyls = yylsa;
  YYLTYPE *yylsp;

#define YYPOPSTACK   (yyvsp--, yyssp--, yylsp--)
#else
#define YYPOPSTACK   (yyvsp--, yyssp--)
#endif

  int yystacksize = YYINITDEPTH;
  int yyfree_stacks = 0;

#ifdef YYPURE
  int yychar;
  YYSTYPE yylval;
  int yynerrs;
#ifdef YYLSP_NEEDED
  YYLTYPE yylloc;
#endif
#endif

  YYSTYPE yyval;		/*  the variable used to return		*/
				/*  semantic values from the action	*/
				/*  routines				*/

  int yylen;

#if YYDEBUG != 0
  if (yydebug)
    fprintf(stderr, "Starting parse\n");
#endif

  yystate = 0;
  yyerrstatus = 0;
  yynerrs = 0;
  yychar = YYEMPTY;		/* Cause a token to be read.  */

  /* Initialize stack pointers.
     Waste one element of value and location stack
     so that they stay on the same level as the state stack.
     The wasted elements are never initialized.  */

  yyssp = yyss - 1;
  yyvsp = yyvs;
#ifdef YYLSP_NEEDED
  yylsp = yyls;
#endif

/* Push a new state, which is found in  yystate  .  */
/* In all cases, when you get here, the value and location stacks
   have just been pushed. so pushing a state here evens the stacks.  */
yynewstate:

  *++yyssp = yystate;

  if (yyssp >= yyss + yystacksize - 1)
    {
      /* Give user a chance to reallocate the stack */
      /* Use copies of these so that the &'s don't force the real ones into memory. */
      YYSTYPE *yyvs1 = yyvs;
      short *yyss1 = yyss;
#ifdef YYLSP_NEEDED
      YYLTYPE *yyls1 = yyls;
#endif

      /* Get the current used size of the three stacks, in elements.  */
      int size = yyssp - yyss + 1;

#ifdef yyoverflow
      /* Each stack pointer address is followed by the size of
	 the data in use in that stack, in bytes.  */
#ifdef YYLSP_NEEDED
      /* This used to be a conditional around just the two extra args,
	 but that might be undefined if yyoverflow is a macro.  */
      yyoverflow("parser stack overflow",
		 &yyss1, size * sizeof (*yyssp),
		 &yyvs1, size * sizeof (*yyvsp),
		 &yyls1, size * sizeof (*yylsp),
		 &yystacksize);
#else
      yyoverflow("parser stack overflow",
		 &yyss1, size * sizeof (*yyssp),
		 &yyvs1, size * sizeof (*yyvsp),
		 &yystacksize);
#endif

      yyss = yyss1; yyvs = yyvs1;
#ifdef YYLSP_NEEDED
      yyls = yyls1;
#endif
#else /* no yyoverflow */
      /* Extend the stack our own way.  */
      if (yystacksize >= YYMAXDEPTH)
	{
	  yyerror("parser stack overflow");
	  if (yyfree_stacks)
	    {
	      free (yyss);
	      free (yyvs);
#ifdef YYLSP_NEEDED
	      free (yyls);
#endif
	    }
	  return 2;
	}
      yystacksize *= 2;
      if (yystacksize > YYMAXDEPTH)
	yystacksize = YYMAXDEPTH;
#ifndef YYSTACK_USE_ALLOCA
      yyfree_stacks = 1;
#endif
      yyss = (short *) YYSTACK_ALLOC (yystacksize * sizeof (*yyssp));
      __yy_memcpy ((char *)yyss, (char *)yyss1,
		   size * (unsigned int) sizeof (*yyssp));
      yyvs = (YYSTYPE *) YYSTACK_ALLOC (yystacksize * sizeof (*yyvsp));
      __yy_memcpy ((char *)yyvs, (char *)yyvs1,
		   size * (unsigned int) sizeof (*yyvsp));
#ifdef YYLSP_NEEDED
      yyls = (YYLTYPE *) YYSTACK_ALLOC (yystacksize * sizeof (*yylsp));
      __yy_memcpy ((char *)yyls, (char *)yyls1,
		   size * (unsigned int) sizeof (*yylsp));
#endif
#endif /* no yyoverflow */

      yyssp = yyss + size - 1;
      yyvsp = yyvs + size - 1;
#ifdef YYLSP_NEEDED
      yylsp = yyls + size - 1;
#endif

#if YYDEBUG != 0
      if (yydebug)
	fprintf(stderr, "Stack size increased to %d\n", yystacksize);
#endif

      if (yyssp >= yyss + yystacksize - 1)
	YYABORT;
    }

#if YYDEBUG != 0
  if (yydebug)
    fprintf(stderr, "Entering state %d\n", yystate);
#endif

  goto yybackup;
 yybackup:

/* Do appropriate processing given the current state.  */
/* Read a lookahead token if we need one and don't already have one.  */
/* yyresume: */

  /* First try to decide what to do without reference to lookahead token.  */

  yyn = yypact[yystate];
  if (yyn == YYFLAG)
    goto yydefault;

  /* Not known => get a lookahead token if don't already have one.  */

  /* yychar is either YYEMPTY or YYEOF
     or a valid token in external form.  */

  if (yychar == YYEMPTY)
    {
#if YYDEBUG != 0
      if (yydebug)
	fprintf(stderr, "Reading a token: ");
#endif
      yychar = YYLEX;
    }

  /* Convert token to internal form (in yychar1) for indexing tables with */

  if (yychar <= 0)		/* This means end of input. */
    {
      yychar1 = 0;
      yychar = YYEOF;		/* Don't call YYLEX any more */

#if YYDEBUG != 0
      if (yydebug)
	fprintf(stderr, "Now at end of input.\n");
#endif
    }
  else
    {
      yychar1 = YYTRANSLATE(yychar);

#if YYDEBUG != 0
      if (yydebug)
	{
	  fprintf (stderr, "Next token is %d (%s", yychar, yytname[yychar1]);
	  /* Give the individual parser a way to print the precise meaning
	     of a token, for further debugging info.  */
#ifdef YYPRINT
	  YYPRINT (stderr, yychar, yylval);
#endif
	  fprintf (stderr, ")\n");
	}
#endif
    }

  yyn += yychar1;
  if (yyn < 0 || yyn > YYLAST || yycheck[yyn] != yychar1)
    goto yydefault;

  yyn = yytable[yyn];

  /* yyn is what to do for this token type in this state.
     Negative => reduce, -yyn is rule number.
     Positive => shift, yyn is new state.
       New state is final state => don't bother to shift,
       just return success.
     0, or most negative number => error.  */

  if (yyn < 0)
    {
      if (yyn == YYFLAG)
	goto yyerrlab;
      yyn = -yyn;
      goto yyreduce;
    }
  else if (yyn == 0)
    goto yyerrlab;

  if (yyn == YYFINAL)
    YYACCEPT;

  /* Shift the lookahead token.  */

#if YYDEBUG != 0
  if (yydebug)
    fprintf(stderr, "Shifting token %d (%s), ", yychar, yytname[yychar1]);
#endif

  /* Discard the token being shifted unless it is eof.  */
  if (yychar != YYEOF)
    yychar = YYEMPTY;

  *++yyvsp = yylval;
#ifdef YYLSP_NEEDED
  *++yylsp = yylloc;
#endif

  /* count tokens shifted since error; after three, turn off error status.  */
  if (yyerrstatus) yyerrstatus--;

  yystate = yyn;
  goto yynewstate;

/* Do the default action for the current state.  */
yydefault:

  yyn = yydefact[yystate];
  if (yyn == 0)
    goto yyerrlab;

/* Do a reduction.  yyn is the number of a rule to reduce with.  */
yyreduce:
  yylen = yyr2[yyn];
  if (yylen > 0)
    yyval = yyvsp[1-yylen]; /* implement default value of the action */

#if YYDEBUG != 0
  if (yydebug)
    {
      int i;

      fprintf (stderr, "Reducing via rule %d (line %d), ",
	       yyn, yyrline[yyn]);

      /* Print the symbols being reduced, and their result.  */
      for (i = yyprhs[yyn]; yyrhs[i] > 0; i++)
	fprintf (stderr, "%s ", yytname[yyrhs[i]]);
      fprintf (stderr, " -> %s\n", yytname[yyr1[yyn]]);
    }
#endif


  switch (yyn) {

case 1:
#line 32 "aidl_language_y.y"
{ g_callbacks->document(yyvsp[0].document_item); ;
    break;}
case 2:
#line 33 "aidl_language_y.y"
{ g_callbacks->document(yyvsp[0].document_item); ;
    break;}
case 3:
#line 37 "aidl_language_y.y"
{ ;
    break;}
case 4:
#line 38 "aidl_language_y.y"
{ ;
    break;}
case 5:
#line 39 "aidl_language_y.y"
{ ;
    break;}
case 6:
#line 43 "aidl_language_y.y"
{ ;
    break;}
case 7:
#line 47 "aidl_language_y.y"
{ g_callbacks->import(&(yyvsp[0].buffer)); ;
    break;}
case 8:
#line 48 "aidl_language_y.y"
{ g_callbacks->import(&(yyvsp[-1].buffer)); ;
    break;}
case 9:
#line 52 "aidl_language_y.y"
{ yyval.document_item = NULL; ;
    break;}
case 10:
#line 53 "aidl_language_y.y"
{
                                                    if (yyvsp[0].document_item == NULL) {
                                                        // error cases only
                                                        yyval = yyvsp[-1];
                                                    } else {
                                                        document_item_type* p = yyvsp[-1].document_item;
                                                        while (p && p->next) {
                                                            p=p->next;
                                                        }
                                                        if (p) {
                                                            p->next = (document_item_type*)yyvsp[0].document_item;
                                                            yyval = yyvsp[-1];
                                                        } else {
                                                            yyval.document_item = (document_item_type*)yyvsp[0].document_item;
                                                        }
                                                    }
                                                ;
    break;}
case 11:
#line 70 "aidl_language_y.y"
{
                                                    fprintf(stderr, "%s:%d: syntax error don't know what to do with \"%s\"\n", g_currentFilename,
                                                            yyvsp[0].buffer.lineno, yyvsp[0].buffer.data);
                                                    yyval = yyvsp[-1];
                                                ;
    break;}
case 12:
#line 78 "aidl_language_y.y"
{ yyval.document_item = (document_item_type*)yyvsp[0].user_data; ;
    break;}
case 13:
#line 79 "aidl_language_y.y"
{ yyval.document_item = (document_item_type*)yyvsp[0].interface_item; ;
    break;}
case 14:
#line 83 "aidl_language_y.y"
{
                                                        user_data_type* b = (user_data_type*)malloc(sizeof(user_data_type));
                                                        b->document_item.item_type = USER_DATA_TYPE;
                                                        b->document_item.next = NULL;
                                                        b->keyword_token = yyvsp[-2].buffer;
                                                        b->name = yyvsp[-1].buffer;
                                                        b->package = g_currentPackage ? strdup(g_currentPackage) : NULL;
                                                        b->semicolon_token = yyvsp[0].buffer;
                                                        b->flattening_methods = PARCELABLE_DATA;
                                                        yyval.user_data = b;
                                                    ;
    break;}
case 15:
#line 94 "aidl_language_y.y"
{
                                                        fprintf(stderr, "%s:%d syntax error in parcelable declaration. Expected type name.\n",
                                                                     g_currentFilename, yyvsp[-1].buffer.lineno);
                                                        yyval.user_data = NULL;
                                                    ;
    break;}
case 16:
#line 99 "aidl_language_y.y"
{
                                                        fprintf(stderr, "%s:%d syntax error in parcelable declaration. Expected type name, saw \"%s\".\n",
                                                                     g_currentFilename, yyvsp[-1].buffer.lineno, yyvsp[-1].buffer.data);
                                                        yyval.user_data = NULL;
                                                    ;
    break;}
case 17:
#line 104 "aidl_language_y.y"
{
                                                        user_data_type* b = (user_data_type*)malloc(sizeof(user_data_type));
                                                        b->document_item.item_type = USER_DATA_TYPE;
                                                        b->document_item.next = NULL;
                                                        b->keyword_token = yyvsp[-2].buffer;
                                                        b->name = yyvsp[-1].buffer;
                                                        b->package = g_currentPackage ? strdup(g_currentPackage) : NULL;
                                                        b->semicolon_token = yyvsp[0].buffer;
                                                        b->flattening_methods = PARCELABLE_DATA | RPC_DATA;
                                                        yyval.user_data = b;
                                                    ;
    break;}
case 18:
#line 115 "aidl_language_y.y"
{
                                                        fprintf(stderr, "%s:%d syntax error in flattenable declaration. Expected type name.\n",
                                                                     g_currentFilename, yyvsp[-1].buffer.lineno);
                                                        yyval.user_data = NULL;
                                                    ;
    break;}
case 19:
#line 120 "aidl_language_y.y"
{
                                                        fprintf(stderr, "%s:%d syntax error in flattenable declaration. Expected type name, saw \"%s\".\n",
                                                                     g_currentFilename, yyvsp[-1].buffer.lineno, yyvsp[-1].buffer.data);
                                                        yyval.user_data = NULL;
                                                    ;
    break;}
case 20:
#line 129 "aidl_language_y.y"
{
                                                        interface_type* c = (interface_type*)malloc(sizeof(interface_type));
                                                        c->document_item.item_type = INTERFACE_TYPE_BINDER;
                                                        c->document_item.next = NULL;
                                                        c->interface_token = yyvsp[0].buffer;
                                                        c->oneway = false;
                                                        memset(&c->oneway_token, 0, sizeof(buffer_type));
                                                        c->comments_token = &c->interface_token;
                                                        yyval.interface_obj = c;
                                                   ;
    break;}
case 21:
#line 139 "aidl_language_y.y"
{
                                                        interface_type* c = (interface_type*)malloc(sizeof(interface_type));
                                                        c->document_item.item_type = INTERFACE_TYPE_BINDER;
                                                        c->document_item.next = NULL;
                                                        c->interface_token = yyvsp[0].buffer;
                                                        c->oneway = true;
                                                        c->oneway_token = yyvsp[-1].buffer;
                                                        c->comments_token = &c->oneway_token;
                                                        yyval.interface_obj = c;
                                                   ;
    break;}
case 22:
#line 149 "aidl_language_y.y"
{
                                                        interface_type* c = (interface_type*)malloc(sizeof(interface_type));
                                                        c->document_item.item_type = INTERFACE_TYPE_RPC;
                                                        c->document_item.next = NULL;
                                                        c->interface_token = yyvsp[0].buffer;
                                                        c->oneway = false;
                                                        memset(&c->oneway_token, 0, sizeof(buffer_type));
                                                        c->comments_token = &c->interface_token;
                                                        yyval.interface_obj = c;
                                                   ;
    break;}
case 25:
#line 167 "aidl_language_y.y"
{ 
                                                        interface_type* c = yyvsp[-4].interface_obj;
                                                        c->name = yyvsp[-3].buffer;
                                                        c->package = g_currentPackage ? strdup(g_currentPackage) : NULL;
                                                        c->open_brace_token = yyvsp[-2].buffer;
                                                        c->interface_items = yyvsp[-1].interface_item;
                                                        c->close_brace_token = yyvsp[0].buffer;
                                                        yyval.interface_obj = c;
                                                    ;
    break;}
case 26:
#line 176 "aidl_language_y.y"
{
                                                        fprintf(stderr, "%s:%d: syntax error in interface declaration.  Expected type name, saw \"%s\"\n",
                                                                    g_currentFilename, yyvsp[-3].buffer.lineno, yyvsp[-3].buffer.data);
                                                        yyval.document_item = NULL;
                                                    ;
    break;}
case 27:
#line 181 "aidl_language_y.y"
{
                                                        fprintf(stderr, "%s:%d: syntax error in interface declaration.  Expected type name, saw \"%s\"\n",
                                                                    g_currentFilename, yyvsp[-1].buffer.lineno, yyvsp[-1].buffer.data);
                                                        yyval.document_item = NULL;
                                                    ;
    break;}
case 28:
#line 190 "aidl_language_y.y"
{ yyval.interface_item = NULL; ;
    break;}
case 29:
#line 191 "aidl_language_y.y"
{
                                                        interface_item_type* p=yyvsp[-1].interface_item;
                                                        while (p && p->next) {
                                                            p=p->next;
                                                        }
                                                        if (p) {
                                                            p->next = (interface_item_type*)yyvsp[0].method;
                                                            yyval = yyvsp[-1];
                                                        } else {
                                                            yyval.interface_item = (interface_item_type*)yyvsp[0].method;
                                                        }
                                                    ;
    break;}
case 30:
#line 203 "aidl_language_y.y"
{
                                                        fprintf(stderr, "%s:%d: syntax error before ';' (expected method declaration)\n",
                                                                    g_currentFilename, yyvsp[0].buffer.lineno);
                                                        yyval = yyvsp[-2];
                                                    ;
    break;}
case 31:
#line 211 "aidl_language_y.y"
{
                                                        method_type *method = (method_type*)malloc(sizeof(method_type));
                                                        method->interface_item.item_type = METHOD_TYPE;
                                                        method->interface_item.next = NULL;
                                                        method->oneway = false;
                                                        method->type = yyvsp[-5].type;
                                                        memset(&method->oneway_token, 0, sizeof(buffer_type));
                                                        method->name = yyvsp[-4].buffer;
                                                        method->open_paren_token = yyvsp[-3].buffer;
                                                        method->args = yyvsp[-2].arg;
                                                        method->close_paren_token = yyvsp[-1].buffer;
                                                        method->hasId = false;
                                                        memset(&method->equals_token, 0, sizeof(buffer_type));
                                                        memset(&method->id, 0, sizeof(buffer_type));
                                                        method->semicolon_token = yyvsp[0].buffer;
                                                        method->comments_token = &method->type.type;
                                                        yyval.method = method;
                                                    ;
    break;}
case 32:
#line 229 "aidl_language_y.y"
{
                                                        method_type *method = (method_type*)malloc(sizeof(method_type));
                                                        method->interface_item.item_type = METHOD_TYPE;
                                                        method->interface_item.next = NULL;
                                                        method->oneway = true;
                                                        method->oneway_token = yyvsp[-6].buffer;
                                                        method->type = yyvsp[-5].type;
                                                        method->name = yyvsp[-4].buffer;
                                                        method->open_paren_token = yyvsp[-3].buffer;
                                                        method->args = yyvsp[-2].arg;
                                                        method->close_paren_token = yyvsp[-1].buffer;
                                                        method->hasId = false;
                                                        memset(&method->equals_token, 0, sizeof(buffer_type));
                                                        memset(&method->id, 0, sizeof(buffer_type));
                                                        method->semicolon_token = yyvsp[0].buffer;
                                                        method->comments_token = &method->oneway_token;
                                                        yyval.method = method;
                                                    ;
    break;}
case 33:
#line 247 "aidl_language_y.y"
{
                                                        method_type *method = (method_type*)malloc(sizeof(method_type));
                                                        method->interface_item.item_type = METHOD_TYPE;
                                                        method->interface_item.next = NULL;
                                                        method->oneway = false;
                                                        memset(&method->oneway_token, 0, sizeof(buffer_type));
                                                        method->type = yyvsp[-7].type;
                                                        method->name = yyvsp[-6].buffer;
                                                        method->open_paren_token = yyvsp[-5].buffer;
                                                        method->args = yyvsp[-4].arg;
                                                        method->close_paren_token = yyvsp[-3].buffer;
                                                        method->hasId = true;
                                                        method->equals_token = yyvsp[-2].buffer;
                                                        method->id = yyvsp[-1].buffer;
                                                        method->semicolon_token = yyvsp[0].buffer;
                                                        method->comments_token = &method->type.type;
                                                        yyval.method = method;
                                                    ;
    break;}
case 34:
#line 265 "aidl_language_y.y"
{
                                                        method_type *method = (method_type*)malloc(sizeof(method_type));
                                                        method->interface_item.item_type = METHOD_TYPE;
                                                        method->interface_item.next = NULL;
                                                        method->oneway = true;
                                                        method->oneway_token = yyvsp[-8].buffer;
                                                        method->type = yyvsp[-7].type;
                                                        method->name = yyvsp[-6].buffer;
                                                        method->open_paren_token = yyvsp[-5].buffer;
                                                        method->args = yyvsp[-4].arg;
                                                        method->close_paren_token = yyvsp[-3].buffer;
                                                        method->hasId = true;
                                                        method->equals_token = yyvsp[-2].buffer;
                                                        method->id = yyvsp[-1].buffer;
                                                        method->semicolon_token = yyvsp[0].buffer;
                                                        method->comments_token = &method->oneway_token;
                                                        yyval.method = method;
                                                    ;
    break;}
case 35:
#line 286 "aidl_language_y.y"
{ yyval.arg = NULL; ;
    break;}
case 36:
#line 287 "aidl_language_y.y"
{ yyval = yyvsp[0]; ;
    break;}
case 37:
#line 288 "aidl_language_y.y"
{
                                    if (yyval.arg != NULL) {
                                        // only NULL on error
                                        yyval = yyvsp[-2];
                                        arg_type *p = yyvsp[-2].arg;
                                        while (p && p->next) {
                                            p=p->next;
                                        }
                                        yyvsp[0].arg->comma_token = yyvsp[-1].buffer;
                                        p->next = yyvsp[0].arg;
                                    }
                                ;
    break;}
case 38:
#line 300 "aidl_language_y.y"
{
                                    fprintf(stderr, "%s:%d: syntax error in parameter list\n", g_currentFilename, yyvsp[0].buffer.lineno);
                                    yyval.arg = NULL;
                                ;
    break;}
case 39:
#line 307 "aidl_language_y.y"
{
                                                arg_type* arg = (arg_type*)malloc(sizeof(arg_type));
                                                memset(&arg->comma_token, 0, sizeof(buffer_type));
                                                arg->direction = yyvsp[-2].buffer;
                                                arg->type = yyvsp[-1].type;
                                                arg->name = yyvsp[0].buffer;
                                                arg->next = NULL;
                                                yyval.arg = arg;
                                      ;
    break;}
case 40:
#line 319 "aidl_language_y.y"
{
                                    yyval.type.type = yyvsp[0].buffer;
                                    init_buffer_type(&yyval.type.array_token, yylineno);
                                    yyval.type.dimension = 0;
                                ;
    break;}
case 41:
#line 324 "aidl_language_y.y"
{
                                    yyval.type.type = yyvsp[-1].buffer;
                                    yyval.type.array_token = yyvsp[0].buffer;
                                    yyval.type.dimension = count_brackets(yyvsp[0].buffer.data);
                                ;
    break;}
case 42:
#line 329 "aidl_language_y.y"
{
                                    yyval.type.type = yyvsp[0].buffer;
                                    init_buffer_type(&yyval.type.array_token, yylineno);
                                    yyval.type.dimension = 0;
                                ;
    break;}
case 43:
#line 337 "aidl_language_y.y"
{ init_buffer_type(&yyval.buffer, yylineno); ;
    break;}
case 44:
#line 338 "aidl_language_y.y"
{ yyval.buffer = yyvsp[0].buffer; ;
    break;}
case 45:
#line 339 "aidl_language_y.y"
{ yyval.buffer = yyvsp[0].buffer; ;
    break;}
case 46:
#line 340 "aidl_language_y.y"
{ yyval.buffer = yyvsp[0].buffer; ;
    break;}
}
   /* the action file gets copied in in place of this dollarsign */
#line 543 "/usr/local/share/bison.simple"

  yyvsp -= yylen;
  yyssp -= yylen;
#ifdef YYLSP_NEEDED
  yylsp -= yylen;
#endif

#if YYDEBUG != 0
  if (yydebug)
    {
      short *ssp1 = yyss - 1;
      fprintf (stderr, "state stack now");
      while (ssp1 != yyssp)
	fprintf (stderr, " %d", *++ssp1);
      fprintf (stderr, "\n");
    }
#endif

  *++yyvsp = yyval;

#ifdef YYLSP_NEEDED
  yylsp++;
  if (yylen == 0)
    {
      yylsp->first_line = yylloc.first_line;
      yylsp->first_column = yylloc.first_column;
      yylsp->last_line = (yylsp-1)->last_line;
      yylsp->last_column = (yylsp-1)->last_column;
      yylsp->text = 0;
    }
  else
    {
      yylsp->last_line = (yylsp+yylen-1)->last_line;
      yylsp->last_column = (yylsp+yylen-1)->last_column;
    }
#endif

  /* Now "shift" the result of the reduction.
     Determine what state that goes to,
     based on the state we popped back to
     and the rule number reduced by.  */

  yyn = yyr1[yyn];

  yystate = yypgoto[yyn - YYNTBASE] + *yyssp;
  if (yystate >= 0 && yystate <= YYLAST && yycheck[yystate] == *yyssp)
    yystate = yytable[yystate];
  else
    yystate = yydefgoto[yyn - YYNTBASE];

  goto yynewstate;

yyerrlab:   /* here on detecting error */

  if (! yyerrstatus)
    /* If not already recovering from an error, report this error.  */
    {
      ++yynerrs;

#ifdef YYERROR_VERBOSE
      yyn = yypact[yystate];

      if (yyn > YYFLAG && yyn < YYLAST)
	{
	  int size = 0;
	  char *msg;
	  int x, count;

	  count = 0;
	  /* Start X at -yyn if nec to avoid negative indexes in yycheck.  */
	  for (x = (yyn < 0 ? -yyn : 0);
	       x < (sizeof(yytname) / sizeof(char *)); x++)
	    if (yycheck[x + yyn] == x)
	      size += strlen(yytname[x]) + 15, count++;
	  msg = (char *) malloc(size + 15);
	  if (msg != 0)
	    {
	      strcpy(msg, "parse error");

	      if (count < 5)
		{
		  count = 0;
		  for (x = (yyn < 0 ? -yyn : 0);
		       x < (sizeof(yytname) / sizeof(char *)); x++)
		    if (yycheck[x + yyn] == x)
		      {
			strcat(msg, count == 0 ? ", expecting `" : " or `");
			strcat(msg, yytname[x]);
			strcat(msg, "'");
			count++;
		      }
		}
	      yyerror(msg);
	      free(msg);
	    }
	  else
	    yyerror ("parse error; also virtual memory exceeded");
	}
      else
#endif /* YYERROR_VERBOSE */
	yyerror("parse error");
    }

  goto yyerrlab1;
yyerrlab1:   /* here on error raised explicitly by an action */

  if (yyerrstatus == 3)
    {
      /* if just tried and failed to reuse lookahead token after an error, discard it.  */

      /* return failure if at end of input */
      if (yychar == YYEOF)
	YYABORT;

#if YYDEBUG != 0
      if (yydebug)
	fprintf(stderr, "Discarding token %d (%s).\n", yychar, yytname[yychar1]);
#endif

      yychar = YYEMPTY;
    }

  /* Else will try to reuse lookahead token
     after shifting the error token.  */

  yyerrstatus = 3;		/* Each real token shifted decrements this */

  goto yyerrhandle;

yyerrdefault:  /* current state does not do anything special for the error token. */

#if 0
  /* This is wrong; only states that explicitly want error tokens
     should shift them.  */
  yyn = yydefact[yystate];  /* If its default is to accept any token, ok.  Otherwise pop it.*/
  if (yyn) goto yydefault;
#endif

yyerrpop:   /* pop the current state because it cannot handle the error token */

  if (yyssp == yyss) YYABORT;
  yyvsp--;
  yystate = *--yyssp;
#ifdef YYLSP_NEEDED
  yylsp--;
#endif

#if YYDEBUG != 0
  if (yydebug)
    {
      short *ssp1 = yyss - 1;
      fprintf (stderr, "Error: state stack now");
      while (ssp1 != yyssp)
	fprintf (stderr, " %d", *++ssp1);
      fprintf (stderr, "\n");
    }
#endif

yyerrhandle:

  yyn = yypact[yystate];
  if (yyn == YYFLAG)
    goto yyerrdefault;

  yyn += YYTERROR;
  if (yyn < 0 || yyn > YYLAST || yycheck[yyn] != YYTERROR)
    goto yyerrdefault;

  yyn = yytable[yyn];
  if (yyn < 0)
    {
      if (yyn == YYFLAG)
	goto yyerrpop;
      yyn = -yyn;
      goto yyreduce;
    }
  else if (yyn == 0)
    goto yyerrpop;

  if (yyn == YYFINAL)
    YYACCEPT;

#if YYDEBUG != 0
  if (yydebug)
    fprintf(stderr, "Shifting error token, ");
#endif

  *++yyvsp = yylval;
#ifdef YYLSP_NEEDED
  *++yylsp = yylloc;
#endif

  yystate = yyn;
  goto yynewstate;

 yyacceptlab:
  /* YYACCEPT comes here.  */
  if (yyfree_stacks)
    {
      free (yyss);
      free (yyvs);
#ifdef YYLSP_NEEDED
      free (yyls);
#endif
    }
  return 0;

 yyabortlab:
  /* YYABORT comes here.  */
  if (yyfree_stacks)
    {
      free (yyss);
      free (yyvs);
#ifdef YYLSP_NEEDED
      free (yyls);
#endif
    }
  return 1;
}
#line 343 "aidl_language_y.y"


#include <ctype.h>
#include <stdio.h>

int g_error = 0;

int yyerror(char* errstr)
{
    fprintf(stderr, "%s:%d: %s\n", g_currentFilename, yylineno, errstr);
    g_error = 1;
    return 1;
}

void init_buffer_type(buffer_type* buf, int lineno)
{
    buf->lineno = lineno;
    buf->token = 0;
    buf->data = NULL;
    buf->extra = NULL;
}

static int count_brackets(const char* s)
{
    int n=0;
    while (*s) {
        if (*s == '[') n++;
        s++;
    }
    return n;
}
