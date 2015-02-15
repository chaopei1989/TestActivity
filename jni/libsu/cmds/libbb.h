/* vi: set sw=4 ts=4: */
/*
 * Busybox main internal header file
 *
 * Based in part on code from sash, Copyright (c) 1999 by David I. Bell
 * Permission has been granted to redistribute this code under GPL.
 *
 * Licensed under GPLv2, see file LICENSE in this source tree.
 */
#ifndef LIBBB_H
#define LIBBB_H 1

//#include "platform.h"

#include <ctype.h>
#include <dirent.h>
#include <errno.h>
#include <fcntl.h>
#include <inttypes.h>
#include <netdb.h>
#include <setjmp.h>
#include <signal.h>
#if defined __UCLIBC__ /* TODO: and glibc? */
/* use inlined versions of these: */
# define sigfillset(s)    __sigfillset(s)
# define sigemptyset(s)   __sigemptyset(s)
# define sigisemptyset(s) __sigisemptyset(s)
#endif
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <stddef.h>
#include <string.h>
/* There are two incompatible basename's, let not use them! */
/* See the dirname/basename man page for details */
#include <libgen.h> /* dirname,basename */
#undef basename
#define basename dont_use_basename
#include <sys/poll.h>
#include <sys/ioctl.h>
#include <sys/mman.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <sys/time.h>
#include <sys/types.h>
#ifndef major
# include <sys/sysmacros.h>
#endif
#include <sys/wait.h>
#include <termios.h>
#include <time.h>
#include <sys/param.h>
#include <pwd.h>
#include <grp.h>
#if ENABLE_FEATURE_SHADOWPASSWDS
# if !ENABLE_USE_BB_SHADOW
/* If using busybox's shadow implementation, do not include the shadow.h
 * header as the toolchain may not provide it at all.
 */
#  include <shadow.h>
# endif
#endif
#if defined(ANDROID) || defined(__ANDROID__)
# define endpwent() ((void)0)
# define endgrent() ((void)0)
#endif
#ifdef HAVE_MNTENT_H
# include <mntent.h>
#endif
#ifdef HAVE_SYS_STATFS_H
# include <sys/statfs.h>
#endif
/* Don't do this here:
 * #include <sys/sysinfo.h>
 * Some linux/ includes pull in conflicting definition
 * of struct sysinfo (only in some toolchanins), which breaks build.
 * Include sys/sysinfo.h only in those files which need it.
 */
#if ENABLE_SELINUX
# include <selinux/selinux.h>
# include <selinux/context.h>
# include <selinux/flask.h>
# include <selinux/av_permissions.h>
#endif
#if ENABLE_FEATURE_UTMP
# include <utmp.h>
#endif
#if ENABLE_LOCALE_SUPPORT
# include <locale.h>
#else
# define setlocale(x,y) ((void)0)
#endif
#ifdef DMALLOC
# include <dmalloc.h>
#endif
/* Just in case libc doesn't define some of these... */
#ifndef _PATH_PASSWD
#define _PATH_PASSWD  "/etc/passwd"
#endif
#ifndef _PATH_GROUP
#define _PATH_GROUP   "/etc/group"
#endif
#ifndef _PATH_SHADOW
#define _PATH_SHADOW  "/etc/shadow"
#endif
#ifndef _PATH_GSHADOW
#define _PATH_GSHADOW "/etc/gshadow"
#endif
#if defined __FreeBSD__ || defined __OpenBSD__
# include <netinet/in.h>
# include <arpa/inet.h>
#elif defined __APPLE__
# include <netinet/in.h>
#else
# include <arpa/inet.h>
# if !defined(__socklen_t_defined) && !defined(_SOCKLEN_T_DECLARED)
/* We #define socklen_t *after* includes, otherwise we get
 * typedef redefinition errors from system headers
 * (in case "is it defined already" detection above failed)
 */
#  define socklen_t bb_socklen_t
   typedef unsigned socklen_t;
# endif
#endif
#ifndef HAVE_CLEARENV
# define clearenv() do { if (environ) environ[0] = NULL; } while (0)
#endif
#ifndef HAVE_FDATASYNC
# define fdatasync fsync
#endif
#ifndef HAVE_XTABS
# define XTABS TAB3
#endif


/* Some libc's forget to declare these, do it ourself */

extern char **environ;
extern uint32_t option_mask32;
#define bb_perror_msg printf
#define bb_show_usage() ((void*)0)
#define bb_error_msg_and_die printf
#define getopt32 getopt
#define bb_putchar putchar
#define DOT_OR_DOTDOT(s) ((s)[0] == '.' && (!(s)[1] || ((s)[1] == '.' && !(s)[2])))
#endif
