(* Task 5.1 *)
functor MemoedFibo (D : DICT where type Key.t = IntInf.int) : FIBO =
struct
  val dictionary : D.Key.t D.dict ref = ref D.empty
  (* Purpose: fib n computes the nth fibonacci number
   * Examples fib 11 == 89, fib 4 == 3, fib 0 == 0
   *)
  fun fib (n : IntInf.int) : IntInf.int =
      case n of
        0 => 0
      | 1 => 1
      | _ => case (D.lookup (!dictionary) n) of
               NONE => let
                 val res = fib (n-1) + fib(n-2)
               in (dictionary := (D.insert (!dictionary) (n, res)) ; res) end
             | SOME(v) => v
(*Tests*)

val 0 = fib 0
val 1 = fib 1
val 1 = fib 2
val 354224848179261915075 = fib 100
val 89 = fib 11
end

(* Task 5.3 *)
functor Memoizer (D : DICT) : MEMOIZER =
struct
  structure D = D
  fun memo (f : ((D.Key.t -> 'a) -> (D.Key.t -> 'a))) : (D.Key.t -> 'a) =
      let
        val dictionary : 'a D.dict ref = ref D.empty
        fun memoized  (n : D.Key.t) : 'a =
            case (D.lookup (!dictionary) n ) of
              SOME(v) => v
            | NONE => let
                val res = f memoized n
              in (dictionary := (D.insert (!dictionary) (n, res)) ; res) end
      in
        f memoized
      end

end


structure AutoMemoedFibo : FIBO =
struct
  structure TreeIntInfMemoizer = Memoizer(TreeDict(IntInfLt))
  structure TreeMemoFibo = MemoedFibo(TreeDict(IntInfLt))

  fun fib2 (g : IntInf.int -> IntInf.int) (n : IntInf.int) =
      case n of
        0 => 0
      | 1 => 1
      | _ => (g (n-2)) + (g (n-1))

  val fib = TreeIntInfMemoizer.memo fib2
end

(* Task 5.6 *)
structure AutoMemoedLCS : LCS =
struct
  structure TreeIntInfMemoizer = Memoizer(TreeDict(DnaPairOrder))

  fun lcs2 (g : (Base.t list * Base.t list) -> Base.t list)
          (s1 : Base.t list, s2 :  Base.t list) =
      case (s1,s2) of
        ([],_) => []
      | (_,[]) => []
      | (x::xs, y::ys) =>
        let
          val res = g(xs,ys)
        in
          case Base.eq(x,y) of
            true => x :: res
          | false => Base.longerDnaOf(lcs2 g (s1,ys),lcs2 g (xs,s2))
        end
  val lcs = TreeIntInfMemoizer.memo lcs2
end

(* Task 5.7 *)
structure SpeedExampleLCS =
struct
  val speedExample : (Base.t list * Base.t list) =
      ([Base.A,Base.C,Base.T,Base.G,Base.A,Base.T,Base.G,Base.A,Base.T,Base.G,Base.A,
        Base.C,Base.A,Base.T,Base.T,Base.A,Base.T,Base.G,Base.A,Base.C,Base.A,Base.T,
        Base.T,Base.A,Base.T,Base.G,Base.A,Base.C,Base.A,Base.T,Base.T,Base.A,Base.T,
        Base.G,Base.A,Base.C,Base.A,Base.T,Base.T],
       [Base.A,Base.T,Base.G,Base.A,Base.C,Base.A,Base.T,Base.T,Base.A,Base.T,Base.G,
        Base.A,Base.C,Base.A,Base.T,Base.T,Base.A,Base.T,Base.G,Base.A,Base.C,Base.A,Base.T,Base.T])
end
