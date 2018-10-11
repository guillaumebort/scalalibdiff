package scalalibdiff

import utest._

object LCSTests extends TestSuite {
  val tests = Tests {

    "Empty" - {
      * - { LCS("", "") ==> Nil }
      * - { LCS("A", "") ==> Nil }
      * - { LCS("", "A") ==> Nil }
    }
    "Same" - {
      LCS("AB", "AB") ==> List(LCS(0, 0, 2))
    }
    "Contains" - {
      * - { LCS("A", "AB") ==> List(LCS(0, 0, 1)) }
      * - { LCS("B", "AB") ==> List(LCS(0, 1, 1)) }
      * - { LCS("AB", "B") ==> List(LCS(1, 0, 1)) }
    }
    "Common prefix" - {
      * - { LCS("AB", "AC") ==> List(LCS(0, 0, 1)) }
      * - { LCS("ABC", "ABCX") ==> List(LCS(0, 0, 3)) }
    }
    "Common suffix" - {
      * - { LCS("AXB", "AB") ==> List(LCS(0, 0, 1), LCS(2, 1, 1)) }
      * - { LCS("ABXC", "AXC") ==> List(LCS(0, 0, 1), LCS(2, 1, 2)) }
    }
    "Differents" - {
      * - { LCS("ABRV", "BV") ==> List(LCS(1, 0, 1), LCS(3, 1, 1)) }
      * - { LCS("ABCDEFGH", "XYZ") ==> Nil }
      * - {
        LCS("A0B0C0D0E0F", "00000") ==> List(LCS(1, 0, 1),
                                             LCS(3, 1, 1),
                                             LCS(5, 2, 1),
                                             LCS(7, 3, 1),
                                             LCS(9, 4, 1))
      }
      * - { LCS("A0B0C0D0E0F", "00") ==> List(LCS(1, 0, 1), LCS(3, 1, 1)) }
      * - {
        LCS("abcdefghij", "adekfg") ==> List(LCS(0, 0, 1),
                                             LCS(3, 1, 2),
                                             LCS(5, 4, 2))
      }
      * - {
        LCS("1234567890", "145x67") ==> List(LCS(0, 0, 1),
                                             LCS(3, 1, 2),
                                             LCS(5, 4, 2))
      }
    }
  }
}
