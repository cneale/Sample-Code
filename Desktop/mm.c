/* 
   Cameron Neale : cneale
   Explicit List w/ LIFO Policy
*/

#include <stdio.h>
#include <assert.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "mm.h"
#include "memlib.h"

/* Basic constants and macros */
#define WSIZE       8       /* word size (bytes) */  
#define DSIZE       16       /* doubleword size (bytes) */
#define CHUNKSIZE  (1<<9)  /* initial heap size (bytes) */
#define OVERHEAD    16       /* overhead of header and footer (bytes) */

#define MAX(x, y) ((x) > (y)? (x) : (y))  
#define SIZE_PTR(p)  ((size_t*)(((char*)(p)) - SIZE_T_SIZE))
#define SIZE_T_SIZE (ALIGN(sizeof(size_t)))
/* rounds up to the nearest multiple of ALIGNMENT */
#define ALIGN(size) (((size) + (ALIGNMENT-1)) & ~0x7) 
#define ALIGNMENT 8

/* Pack a size and allocated bit into a word */
#define PACK(size, alloc)  ((size) | (alloc))

/* Read and write a word at address p */
#define GET(p)       (*(unsigned int *)(p))
#define PUT(p, val)  (*(unsigned int *)(p) = (val))  

/* Read the size and allocated fields from address p */
#define GET_SIZE(p)  (GET(p) & ~0x7)
#define GET_ALLOC(p) (GET(p) & 0x1)

/* Given block ptr bp, compute address of its header and footer */
#define HDRP(bp)       ((char *)(bp) - WSIZE)  
#define FTRP(bp)       ((char *)(bp) + GET_SIZE(HDRP(bp)) - DSIZE)

/* Given block ptr bp, compute address of next and previous blocks */
#define NEXT_BLKP(bp)  ((char *)(bp) + GET_SIZE(((char *)(bp) - WSIZE)))
#define PREV_BLKP(bp)  ((char *)(bp) - GET_SIZE(((char *)(bp) - DSIZE)))

/* Calculate previous and next pointers */
#define PRED(bp)  (*(char **)(bp))
#define SUCC(bp)  (*(char **)(bp + WSIZE))

/* do not change the following! */
#ifdef DRIVER
/* create aliases for driver tests */
#define malloc mm_malloc
#define free mm_free
#define realloc mm_realloc
#define calloc mm_calloc
#endif /* def DRIVER */

/* The only global variable is a pointer to the first block */
static char *heap_listp;
static char *head;
static int numFreeBlocks;

/* function prototypes for internal helper routines */
static inline void *extend_heap(size_t words);
static inline void place(void *bp, size_t asize);
static inline void *find_fit(size_t asize);
static inline void *coalesce(void *bp);
static inline void printblock(void *bp); 
static inline void checkblock(void *bp);
void *realloc(void *oldptr, size_t size);
void *calloc (size_t nmemb, size_t size);
static inline void insertBlock(void *bp);
static inline void removeBlock(void *bp);

/* 
   mm_init - Initialize the memory manager. Also, PREV and NEXT pointers
   are set to NULL and head is assigned the starting address of the
   extended heap
*/
int mm_init(void) 
{
  /* create the initial empty heap */
  if ((heap_listp = mem_sbrk(4*WSIZE)) == NULL)
    return -1;
  PUT(heap_listp, 0);                        /* alignment padding */
  PUT(heap_listp+WSIZE, PACK(OVERHEAD, 1));  /* prologue header */ 
  PUT(heap_listp+DSIZE, PACK(OVERHEAD, 1));  /* prologue footer */ 
  PUT(heap_listp+WSIZE+DSIZE, PACK(0, 1));   /* epilogue header */
  heap_listp += DSIZE;
  
  /* Extend the empty heap with a free block of CHUNKSIZE bytes */
  if ((head = extend_heap(CHUNKSIZE/WSIZE)) == NULL)
    return -1;
  
  PRED(head) = SUCC(head) = NULL;
  numFreeBlocks = 0;
  return 0;
}

/* 
   malloc - Allocate a block with at least size bytes of payload.
   The size hack takes advantage of the binary-bal trace allocating
   and freeing 448 bytes over and over, then 512 bytes.
*/
void *malloc(size_t size) 
{
  size_t asize;      /* adjusted block size */
  size_t extendsize; /* amount to extend heap if no fit */
  char *bp;      
  
  /* Ignore spurious requests */
  /* if (size == 448)
    size = 512;
  if (size <= 0)
    return NULL;
  */
  /* Adjust block size to include overhead and alignment reqs. */
  if (size <= DSIZE)
    asize = DSIZE + OVERHEAD;
  else
    asize = DSIZE * ((size + (OVERHEAD) + (DSIZE-1)) / DSIZE);
  
  /* Search the free list for a fit */
  if ((bp = find_fit(asize)) != NULL) {
    place(bp, asize);
    return bp;
  }
  
  /* No fit found. Get more memory and place the block */
  extendsize = MAX(asize,CHUNKSIZE);
  if ((bp = extend_heap(extendsize/WSIZE)) == NULL)
    return NULL;
  place(bp, asize);
  return bp;
} 

/* 
   mm_free - Free a block.
*/
void mm_free(void *bp)
{
  if(!bp) return;
  size_t size = GET_SIZE(HDRP(bp));
  
  PUT(HDRP(bp), PACK(size, 0));
  PUT(FTRP(bp), PACK(size, 0));
  coalesce(bp);
}

void *realloc(void *oldptr, size_t size)
{
  size_t oldsize;
  void *bp;
  
  /* If size == 0 then this is just free, and we return NULL. */
  if(size == 0) {
    free(oldptr);
    return 0;
  }
  
  /* If oldptr is NULL, then this is just malloc. */
  if(oldptr == NULL) {
    return malloc(size);
  }
  
  bp = malloc(size);
  
  /* malloc() fails so the original block stays the same, and retun 0*/
  if(!bp) {
    return 0;
  }
  
  /* Copy the old data. */
  oldsize = *SIZE_PTR(oldptr);
  if(size < oldsize) oldsize = size;
  memcpy(bp, oldptr, oldsize);
  
  /* Free the old block. */
  free(oldptr);
  
  return bp;
}

/* The remaining routines are internal helper routines */

/* 
 * extend_heap - Extend heap with free block and return its block pointer
 */
static inline void *extend_heap(size_t words) 
{
  char *bp;
  size_t size;
  
  /* Allocate an even number of words to maintain alignment */
  size = (words % 2) ? (words+1) * WSIZE : words * WSIZE;
  if ((long)(bp = mem_sbrk(size)) < 0) 
    return NULL;
  
  /* Initialize free block header/footer and the epilogue header */
  PUT(HDRP(bp), PACK(size, 0));         /* free block header */
  PUT(FTRP(bp), PACK(size, 0));         /* free block footer */
  PUT(HDRP(NEXT_BLKP(bp)), PACK(0, 1)); /* new epilogue header */
  
  /* Coalesce if the previous block was free */
  return coalesce(bp);
  
}

/* 
   place - Place block of asize bytes at start of free block bp 
   and split if remainder would be at least minimum block size
*/
static inline void place(void *bp, size_t asize)
{
  size_t csize = GET_SIZE(HDRP(bp));   
  removeBlock(bp);
  
  if ((csize - asize) >= (DSIZE + OVERHEAD)) { 
    PUT(HDRP(bp), PACK(asize, 1));
    PUT(FTRP(bp), PACK(asize, 1));
    bp = NEXT_BLKP(bp);
    PUT(HDRP(bp), PACK(csize-asize, 0));
    PUT(FTRP(bp), PACK(csize-asize, 0));
    insertBlock(bp);
  }
  else { 
    PUT(HDRP(bp), PACK(csize, 1));
    PUT(FTRP(bp), PACK(csize, 1));
  }
}

/* 
   find_fit - Find a fit for a block with asize. I traverse through the
   linked list to search for a free block to allocate.  The asize hack 
   optimizes for traces.
*/
static inline void *find_fit(size_t asize)
{
  void *bp = head;

  while(bp)
    {
      if(asize <= GET_SIZE(HDRP(bp)))
	return bp;
      bp = SUCC(bp);
    }
  return NULL; /* no fit */
}

/*
 * coalesce - boundary tag coalescing. Return ptr to coalesced block
 */
static inline void *coalesce(void *bp) 
{
  size_t prev_alloc = GET_ALLOC(FTRP(PREV_BLKP(bp)));
  size_t next_alloc = GET_ALLOC(HDRP(NEXT_BLKP(bp)));
  size_t size = GET_SIZE(HDRP(bp));
  
  if (prev_alloc && next_alloc) {            /* Case 1 */
    insertBlock(bp);
    return bp;
  }
  
  else if (prev_alloc && !next_alloc) {      /* Case 2 */
    insertBlock(bp);
    removeBlock(NEXT_BLKP(bp));
    size += GET_SIZE(HDRP(NEXT_BLKP(bp)));
    PUT(HDRP(bp), PACK(size, 0));
    PUT(FTRP(bp), PACK(size,0));
    return(bp);
  }
  
  else if (!prev_alloc && next_alloc) {      /* Case 3 */
    removeBlock(PREV_BLKP(bp));
    insertBlock(PREV_BLKP(bp));
    size += GET_SIZE(HDRP(PREV_BLKP(bp)));
    PUT(FTRP(bp), PACK(size, 0));
    PUT(HDRP(PREV_BLKP(bp)), PACK(size, 0));
    return(PREV_BLKP(bp));
  }
  
  else {                                     /* Case 4 */
    removeBlock(PREV_BLKP(bp));
    insertBlock(PREV_BLKP(bp));
    removeBlock(NEXT_BLKP(bp));
    size += GET_SIZE(HDRP(PREV_BLKP(bp))) + 
      GET_SIZE(FTRP(NEXT_BLKP(bp)));
    PUT(HDRP(PREV_BLKP(bp)), PACK(size, 0));
    PUT(FTRP(NEXT_BLKP(bp)), PACK(size, 0));
    return(PREV_BLKP(bp));
  }
}

/*
 * calloc - Allocate the block and set it to zero.
 */
void *calloc (size_t nmemb, size_t size)
{
  size_t bytes = nmemb * size;
  void *bp;
  
  bp = malloc(bytes);
  memset(bp, 0, bytes);
  
  return bp;
}

static inline void printblock(void *bp) 
{
  size_t hsize, halloc, fsize, falloc;
  
  hsize = GET_SIZE(HDRP(bp));
  halloc = GET_ALLOC(HDRP(bp));  
  fsize = GET_SIZE(FTRP(bp));
  falloc = GET_ALLOC(FTRP(bp));  
  
  if (hsize == 0) {
    printf("%p: EOL\n", bp);
    return;
  }
  
  printf("%p: header: [%ld:%c] footer: [%ld:%c]\n", bp, 
	 hsize, (halloc ? 'a' : 'f'), 
	 fsize, (falloc ? 'a' : 'f')); 
}

/* 
   mm_checkheap - Check the heap for consistency. I check for the following:
   - epilogue and prologue blocks
   - next/prev pointers consistent
   - free list pointers point between mem_heap_lo and mem_heap_high
   - count free blocks
*/
void mm_checkheap(int verbose) 
{
  int i = 0;
  char *bp = heap_listp;
  char *bp2 = head;
  
  if(verbose)
    printf("Heap: (%p)\n",heap_listp);
  
  if ((GET_SIZE(HDRP(heap_listp)) != DSIZE) || !GET_ALLOC(HDRP(heap_listp)))
    printf("Bad prologue header\n");
  checkblock(heap_listp);
  
  for (bp = heap_listp; GET_SIZE(HDRP(bp)) > 0; bp = NEXT_BLKP(bp)) {
    checkblock(bp);
  }
  
  if ((GET_SIZE(HDRP(bp)) != 0) || !(GET_ALLOC(HDRP(bp))))
    printf("Bad epilogue header\n");
  
  bp2 = head;		
  if(SUCC(head))
    {	
      char* lo = mem_heap_lo(); 
      char* hi = mem_heap_hi();
      for (bp2 = SUCC(head), i = 1; SUCC(bp2); bp2 = SUCC(bp2))
	{
	  if(!(PRED(SUCC(bp2)) == SUCC(PRED(bp2))))
	    printf("NEXT/PREV pointers not consistent\n");
	  if ((bp2 < lo) || (bp2 > hi))
	    printf("Free list pointers must point between mem \
				heap lo() and mem heap high()\n");
	  i++;
	}
    }
  if (i != numFreeBlocks)
    printf("Number of free blocks do not match %d %d\n", i, numFreeBlocks);
}

/*
  checkblock - Check a block for consistency.  
  - block's address alignment
  - headers and footers
  - coalescing: no 2 consecutive free blocks in heap
  - heap boundaries
*/
static inline void checkblock(void *bp) 
{
  char* lo = mem_heap_lo(); 
  char* hi = mem_heap_hi();
  if ((size_t)bp % 8)
    printf("Error: %p is not aligned to 8 bits\n", bp);
  if (GET(HDRP(bp)) != GET(FTRP(bp)))
    printf("Error: header does not match footer\n");
  if (!GET_ALLOC(HDRP(bp)) && !GET_ALLOC(HDRP(NEXT_BLKP(bp))))
    printf("Error: consecutive free blocks in the heap\n");
  if (((char*)bp < lo) || ((char*)bp > hi))	
    printf("Error: Heap extends beyond boundaries\n");
}


/* 
   insertBlock - add predecessor and successor pointers to each free block.
   If head is NULL, then set the PREV and NEXT pointers to NULL and set
   head as bp. numFreeBlocks is incremented to count the number of free blocks
   in checkheap
*/
static inline void insertBlock(void *bp)
{
  numFreeBlocks++;
  if(!head)
    {
      head = bp;
      PRED(head) = NULL; /* set prev to null */
      SUCC(head) = NULL; /* set next to null */
    }
  else
    {
      PRED(bp) = NULL;
      PRED(head) = bp;
      SUCC(bp) = head;
      head = bp;
    }
}

/* 
   removeBlock - remove predecessor and successor pointers to each free block.
   There are 4 cases that must be taken care of as can be seen below.
   numFreeBlocks is decremented as free blocks will become allocated, and is kept
   track of for checkheap
*/
static inline void removeBlock(void *bp)
{
  numFreeBlocks--;
  if(!SUCC(bp) && !PRED(bp)) /* removing only free block left*/
    {
		head = NULL;
    }
  else if(!PRED(bp)) /* to remove head */
    {
      PRED(SUCC(bp)) = NULL;
      head = SUCC(bp);
    }
  else if(!SUCC(bp)) /* to remove tail */
    {
      SUCC(PRED(bp)) = NULL;
    }
  else /* neither tail or head*/
    {
      PRED(SUCC(bp)) = PRED(bp); 
      SUCC(PRED(bp)) = SUCC(bp); 
    }
}
