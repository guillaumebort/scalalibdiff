package scalalibdiff

import scala.annotation.tailrec

/** The longest common subsequence (LCS) problem is the problem of finding the longest subsequence
  * common to all sequences in a set of sequences (often just two sequences). It differs from the
  * longest common substring problem: unlike substrings, subsequences are not required to occupy
  * consecutive positions within the original sequences.
  *
  * See https://en.wikipedia.org/wiki/Longest_common_subsequence_problem
  */
object LCS {

  /** Find Longest Common Subsequences
    * between left & right.
    *
    * Empty result means that both inputs are identical.
    */
  def apply[A](left: Seq[A], right: Seq[A]): List[LCS] = {
    val left0 = left.toIndexedSeq
    val right0 = right.toIndexedSeq

    // Check easy cases first
    if (left0.isEmpty || right0.isEmpty) Nil
    else if (left0 == right0) List(LCS(0, 0, left0.size))
    else if (left0.startsWith(right0)) List(LCS(0, 0, right0.size))
    else if (right0.startsWith(left0)) List(LCS(0, 0, left0.size))

    // left & right are not substrings of each other, let's dig further
    else {
      // First search for common prefix & suffix
      val maybePrefix =
        Option(left0.zip(right0).takeWhile { case (l, r) => l == r }.map(_._1))
          .filter(_.size > 0)
      val maybeSuffix = Option(
        left0.reverse
          .zip(right0.reverse)
          .takeWhile { case (l, r) => l == r }
          .map(_._1)
          .reverse).filter(_.size > 0)
      val prefixSize = maybePrefix.map(_.size).getOrElse(0)
      val suffixSize = maybeSuffix.map(_.size).getOrElse(0)

      // Now focus on what is remaining
      val remainingOnLeft = left0.slice(prefixSize, left0.size - suffixSize)
      val remainingOnRight = right0.slice(prefixSize, left0.size - suffixSize)

      val lastCommonSubsequence = maybeSuffix
        .map(s => LCS(left0.size - s.size, right0.size - s.size, s.size))
        .toList

      maybePrefix.map(s => LCS(0, 0, s.size)).toList ++ (
        // If one side has nothing left this is easy
        if (remainingOnLeft.isEmpty || remainingOnRight.isEmpty)
          lastCommonSubsequence

        // Otherwise use the classical LCS algorithm to find further common subsequences
        // See: https://en.wikipedia.org/wiki/Longest_common_subsequence_problem#Code_for_the_dynamic_programming_solution
        else {
          val C = Array
            .ofDim[Int](remainingOnLeft.size + 1, remainingOnRight.size + 1)
          for {
            i <- 0 until remainingOnLeft.size
            j <- 0 until remainingOnRight.size
          } if (remainingOnLeft(i) == remainingOnRight(j))
            C(i + 1)(j + 1) = C(i)(j) + 1
          else
            C(i + 1)(j + 1) = math.max(C(i + 1)(j), C(i)(j + 1))
          @tailrec def backtrack(i: Int, j: Int, result: List[LCS]): List[LCS] =
            if (i == 0 || j == 0) {
              result.reverse
            } else if (C(i)(j) == C(i - 1)(j)) {
              backtrack(i - 1, j, result)
            } else if (C(i)(j) == C(i)(j - 1)) {
              backtrack(i, j - 1, result)
            } else {
              backtrack(
                i - 1,
                j - 1,
                result match {
                  case LCS(il, ir, s) :: rest
                      if prefixSize + i == il && prefixSize + j == ir =>
                    LCS(prefixSize + i - 1, prefixSize + j - 1, s + 1) :: rest
                  case LCS(il, ir, s) :: rest
                      if il + s == prefixSize + i - 1 && ir + s == prefixSize + j - 1 =>
                    LCS(il, ir, s + 1) :: rest
                  case LCS(il, ir, s) :: rest
                      if il < prefixSize + i - 1 && ir < prefixSize + j - 1 && il + s > prefixSize + i - 1 && ir + s > prefixSize + j - 1 =>
                    result
                  case r =>
                    LCS(prefixSize + i - 1, prefixSize + j - 1, 1) :: r
                }
              )
            }
          backtrack(remainingOnLeft.size, remainingOnRight.size, Nil)
            .foldLeft(lastCommonSubsequence) {
              case (s, lcs) => lcs :: s
            }
        }
      )
    }
  }

}

case class LCS(leftIndex: Int, rightIndex: Int, length: Int)
