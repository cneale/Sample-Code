use "lib.sml";

(* ---------------------------------------------------------------------- *)
(* Section 2 - Write and Prove *)
(* Task 2.1 *)
(* Purpose : concat all elements of a list with elements of another list to
 * create one big list
 * Examples:
 * concat [] ==> []
 * concat [[]] ==> []
 * concat [[1]] ==> [1]
 * concat [[],["a","b"],[],["z"]] ==> ["a","b","z"]
*)
fun concat (l : 'a list list) : 'a list =
    case l of
      [] => []
    | x :: xs => case x of
                        [] => concat(xs)
                      | y::ys => y::concat(ys::xs)

val [] = concat []
val [] = concat [[]]
val [1] = concat[[1]]
val ["a","b","c"] = concat[[],["a","b"],[],["c"]]

(* ---------------------------------------------------------------------- *)
(* Section 3 - Polymorphism, HOFs, Options *)
(* Task 3.1 *)
(* Purpose : allPairs(l1,l2) returns all the possible pairings of
 * elements from l1 with elements from l2
 * Examples:
 * allPairs([],[1,2,3]) ==> []
 * allPairs([1,2,3],[]) ==> [[],[],[]]
 * allPairs([1,2],["a","b"]) ==> [[(1,"a"),(1,"b")],[(2,"a"),(2,"b")]]
*)
fun allpairs (l1 : 'a list, l2 : 'b list) : ('a * 'b) list list =
    map (fn(y) => (map (fn(x) => (y,x)) l2)) l1

val [] = allpairs([],[1,2,3])
val [[],[],[]] = allpairs([1,2,3],[])
val [[(1,"a"),(1,"b")],[(2,"a"),(2,"b")]] = allpairs([1,2],["a","b"])

(* Task 3.2 *)
(* Purpose : transpose l interchanges the rows and columns of l
 * Examples:
 * transpose [[1,2]] ==> [[1],[2]]
 * transpose [[1,2],[3,4]] ==> [[1,3],[2,4]]
*)

fun transpose (l : 'a list list) : 'a list list =
    case l of
      [] => []
    | x::[] => map (fn y => [y]) x
    | x::xs => ListPair.map (fn(i,s) => i::s) (x,transpose xs)

val [[1],[2]] = transpose [[1,2]]
val [[1,3],[2,4]] = transpose [[1,2],[3,4]]

(* Task 3.3 *)
(* Purpose extract (p,l) returns SOME(x,xs) where x was the first element
 * that p(x) evaluates to true for, and xs, l unchanged aside
 * from it missing x. Otherwise it returns NONE if no elements
 * when applied to p evaluated to true
 * Examples:
 * extract(evenP , [2,3,4]) ==> SOME (2,[3,4])
 * extract(evenP , [1,3,5]) ==> NONE
 * extract(fn x => String.size x < 2 , ["aaa","b","bca"])
                        ==> SOME ("b", ["aaa", "bca"])
*)
fun extract (p : 'a -> bool, l : 'a list) : ('a * 'a list) option =
    case l of
      [] => NONE
    | x::xs => (case p x of
                  true => SOME(x,xs)
                | false => (case extract(p,xs) of
                              NONE => NONE
                            | SOME(y,ys) => SOME(y,x::ys)))

fun evenP (n : int) : bool =
    (case n
     of 0 => true
      | 1 => false
      | _ => evenP(n-2))

val SOME(2,[3,4]) = extract(evenP , [2,3,4])
val NONE = extract(evenP , [1,3,5])
val SOME("b", ["aaa", "bca"]) = extract(fn x => String.size x < 2 , ["aaa","b","bca"])


(* ---------------------------------------------------------------------- *)
(* Section 4 - Polynomials as SML functions *)
(* Task 4.1 *)
(* Purpose differentiate takes a poly-f(x)- and returns a poly that is
 * equivalent to its differentiation-f'(x)
 * Examples:
 * differentiate (fn 0 => 5//1 | 1 => 2//1 | 2 => 3//1 | _ => 0//1)
                 ==> fn 0 => 2//1 | 1 => 6//1 | _ => 0//1
 * differentiate (fn 1 => 2//1 | 3 => 1//1 | _ => 0)
                 ==> fn 0 => 2//1 | 2 => 3//1 | _ => 0//1
*)
fun differentiate (p : poly) : poly =
    (fn y => ((y+1)//1)**p(y+1))

val true = polyEqual((fn 0 => 2//1 | 1 => 6//1 | _ => 0//1):poly,differentiate (fn 0 => 5//1 | 1 => 2//1 | 2 => 3//1 | _ => 0//1),4)
val true = polyEqual((fn 0 => 2//1 | 2 => 3//1 | _ => 0//1):poly,differentiate (fn 1 => 2//1 | 3 => 1//1 | _ => 0//1),4)

(* Task 4.2 *)
(* Purpose: integrate(p) takes a polynomial and returns a function that takes a rational and returns
 * a polynomial
 * Examples:
 * integrate (fn 0 => 2//1 | 1 => 6//1 | _ => 0//1) 5//1
             ==> (fn 0 => 5//1 | 1 => 2//1 | 2 => 3//1 | _ => 0//1)
 * integrate (fn 0 => 2//1 | 2 => 3//1 | _ => 0//1) 0//1
             ==> (fn 3 => 1//1 | 1 => 2//1 | _ => 0//1)
*)

fun integrate (p : poly) : rat -> poly =
    fn c => (fn y => case y of 0 => c | _ => divide (p(y-1),y//1))

val true = polyEqual((fn 3 => 1//1 | 1 => 2//1 | _ => 0//1),(integrate (fn 0 => 2//1 | 2 => 3//1 | _ => 0//1) (0//1)),4)
val true = polyEqual((fn 0 => 5//1 | 1 => 2//1 | 2 => 3//1 | _ => 0//1),(integrate (fn 0 => 2//1 | 1 => 6//1 | _ => 0//1) (5//1)),4)

(* ---------------------------------------------------------------------- *)
(* Section 5 - Matrices *)
(* Task 5.1 *)
(* Purpose: plus(m1,m2) adds two matrices of the same dimension together*)
fun plus (m1 : matrix, m2 : matrix) : matrix =
    ListPair.map (fn(x,y) => (ListPair.map (fn(i,s) => i++s) (x,y))) (m1,m2)


(* summat won't work until you complete Task 5.1 *)
(* Purpose: summat adds all matrices in a list (of equivalent dimensions) together *)
fun summat (ms : matrix list) : matrix =
    case ms of
        nil => zed (0, 0)
      | m::ms => List.foldr plus m ms

(* Task 5.2 *)
(* Purpose: outerprod (v1,v2) returns the outerproduct of two column matrices*)
fun outerprod (v1 : rat list, v2 : rat list) : matrix =
    map (fn y => (map (fn(i,s) => i**s) y)) (allpairs(v1,v2))

(* Task 5.3 *)
(* Purpose: times(m1,m2) returns the product of two matrices*)
fun times (m1 : matrix, m2 : matrix) : matrix =
    summat(ListPair.map (fn(i,s) => outerprod(i,s)) ((transpose m1),m2))


(* ---------------------------------------------------------------------- *)
(* Section 6 - Block World *)
(* Task 6.1 *)
(* Purpose: extractMany (p,to,from) extracts all elements of from that return true when p is applied  to
 * that element and an element from from, the resulting list remains unchanged
*)
fun extractMany (eq : 'a * 'a -> bool,
                 toExtract : 'a list, from : 'a list) : ('a list) option =
    case toExtract of
      [] => SOME(from)
    | x::xs => case extract(fn y => eq(x,y),from) of
                 NONE => NONE
               | SOME(y,ys) => extractMany(eq,xs,ys)


(* Task 6.2 *)
datatype block = A | B | C

datatype move = Pickup of (block * block) | Put of (block * block)
              | PickupFromTable of (block) | PutOnTable of (block)

datatype fact = Free of block | OnTop of (block * block) | OnTable of block
              | EmptyHand | Holding of block

type state = fact list

(* Task 6.3 *)
val initial : state = [Free(A),Free(B),Free(C),EmptyHand,OnTable(A),OnTable(B),OnTable(C)]

(* instantiates extractMany with equality for your fact datatype *)
fun extractManyFacts (toConsume : fact list, s : state) : state option =
    extractMany (fn (x : fact, y : fact) => x = y, toConsume, s)

(* Task 6.4 *)
fun consumeAndAdd (s : state, bef : fact list, aft : fact list) : state option =
    case extractManyFacts(bef,s) of
      NONE => NONE
    | SOME(xs) => SOME(xs@aft)


(* Task 6.5 *)
fun step (m : move, s : state) : state option =
    case m of
      Pickup(b,c) => consumeAndAdd(s,[Free(b),OnTop(b,c),EmptyHand],[Holding(b),Free(c)])
    | Put(b,c) => consumeAndAdd(s,[Free(c),Holding(b)],[OnTop(b,c),Free(b)])
    | PickupFromTable(c) => consumeAndAdd(s,[EmptyHand,Free(c),OnTable(c)],[Holding(c)])
    | PutOnTable(c) => consumeAndAdd(s,[Holding(c)],[OnTable(c),Free(c),EmptyHand])
