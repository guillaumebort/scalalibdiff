package scalalibdiff

import scala.io.AnsiColor._
import scala.annotation.tailrec

object Diff {

  sealed trait Difference
  case class Removed(start: Int, end: Int) extends Difference
  case class Added(start: Int, end: Int) extends Difference
  case class Same(leftStart: Int, leftEnd: Int, rightStart: Int, rightEnd: Int)
      extends Difference

  /** List the differences between 2 sequences */
  def apply[A](left: Seq[A], right: Seq[A]): List[Difference] = {
    val left0 = left.toIndexedSeq
    val right0 = right.toIndexedSeq

    @tailrec def go(commonSubsequences: List[LCS],
                    leftIndex: Int = 0,
                    rightIndex: Int = 0,
                    diff: List[Difference] = Nil): List[Difference] =
      commonSubsequences match {
        case Nil =>
          if (leftIndex < left0.size && rightIndex < right0.size)
            (Added(rightIndex, right0.size) :: Removed(leftIndex, left0.size) :: diff).reverse
          else if (leftIndex < left0.size)
            (Removed(leftIndex, left0.size) :: diff).reverse
          else if (rightIndex < right0.size)
            (Added(rightIndex, right0.size) :: diff).reverse
          else
            diff.reverse
        case LCS(leftStart, rightStart, _) :: _
            if leftIndex < leftStart || rightIndex < rightStart =>
          if (leftIndex < leftStart && rightIndex < rightStart)
            go(
              commonSubsequences,
              leftStart,
              rightStart,
              Added(rightIndex, rightStart) :: Removed(leftIndex, leftStart) :: diff)
          else if (leftIndex < leftStart)
            go(commonSubsequences,
               leftStart,
               rightIndex,
               Removed(leftIndex, leftStart) :: diff)
          else
            go(commonSubsequences,
               leftIndex,
               rightStart,
               Added(rightIndex, rightStart) :: diff)
        case LCS(leftStart, rightStart, length) :: rest if length > 0 =>
          go(rest,
             leftStart + length,
             rightStart + length,
             Same(leftStart,
                  leftStart + length,
                  rightStart,
                  rightStart + length) :: diff)
        case LCS(leftStart, rightStart, _) :: rest =>
          go(rest, leftStart, rightStart, diff)
      }

    go(LCS(left0, right0))
  }

  /** Dispaly differences between 2 texts at the line level
    * @param colored Use ANSI color in the output (red & green)
    */
  def text(original: String,
           modified: String,
           colored: Boolean = false): String = {
    val originalLines = original.linesIterator.toSeq
    val modifiedLines = modified.linesIterator.toSeq
    val buff = new StringBuilder
    Diff(originalLines, modifiedLines).foreach {
      case Diff.Same(start, end, _, _) =>
        originalLines.slice(start, end).foreach {
          case line =>
            if (!buff.isEmpty) buff.append("\n")
            if (line.nonEmpty) buff.append("  ")
            buff.append(line)
        }
      case Diff.Added(start, end) =>
        modifiedLines.slice(start, end).foreach {
          case line =>
            if (!buff.isEmpty) buff.append("\n")
            if (colored) buff.append(GREEN)
            buff.append("+")
            if (line.nonEmpty) buff.append(" ")
            buff.append(line)
            if (colored) buff.append(RESET)
        }
      case Diff.Removed(start, end) =>
        originalLines.slice(start, end).foreach {
          case line =>
            if (!buff.isEmpty) buff.append("\n")
            if (colored) buff.append(RED)
            buff.append("-")
            if (line.nonEmpty) buff.append(" ")
            buff.append(line)
            if (colored) buff.append(RESET)
        }
    }
    buff.toString
  }
}
