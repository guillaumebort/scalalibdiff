package scalalibdiff

import utest._

import scala.io.AnsiColor._

object DiffTests extends TestSuite {
  val tests = Tests {

    "Diff" - {
      def diff(a: String, b: String) =
        Diff(a, b)
          .map {
            case Diff.Same(start, end, _, _) =>
              s"${a.slice(start, end)}"
            case Diff.Added(start, end) =>
              s"+${b.slice(start, end)}"
            case Diff.Removed(start, end) =>
              s"-${a.slice(start, end)}"
          }
          .mkString(" ")

      diff("", "") ==> ""
      diff("A", "") ==> "-A"
      diff("", "A") ==> "+A"
      diff("_C_D_EFGH_", "_CD_E_F_G_H_") ==> "_C -_ D_E +_ F -G +_G_ H_"
      diff("OBAMA", "BUSH") ==> "-O B -AMA +USH"
      diff("_C_D_E", "_CD_E_") ==> "_C -_ D_E +_"
      diff("xCxDxE", "xCDxEx") ==> "xC -x DxE +x"
      diff("AGCDEFG", "FGFDXF") ==> "-A +F G -C +F D -E +X F -G"
    }

    "Text diff" - {
      * - {
        Diff.text(
          s"""  in_flight = true;
             |
             |  JsonObjectRequest req = new JsonObjectRequest(Request.POST,
             |    new Response.Listener<JSONObject>() {
             |    @Override
             |    public void onResponse(JSONObject response) {""".stripMargin,
          s"""  in_flight = true;
             |
             |  JsonObjectRequest req = new JsonObjectRequest(Request.GET,
             |    new Response.Listener<JSONObject>() {
             |    @Override
             |    public void onResponse(JSONObject response) {""".stripMargin
        ) ==>
          s"""    in_flight = true;
             |
             |-   JsonObjectRequest req = new JsonObjectRequest(Request.POST,
             |+   JsonObjectRequest req = new JsonObjectRequest(Request.GET,
             |      new Response.Listener<JSONObject>() {
             |      @Override
             |      public void onResponse(JSONObject response) {""".stripMargin
      }
    }

    "Colored text diff" - {
      * - {
        Diff.text(
          s"""  in_flight = true;
             |
             |  JsonObjectRequest req = new JsonObjectRequest(Request.POST,
             |    new Response.Listener<JSONObject>() {
             |    @Override
             |    public void onResponse(JSONObject response) {""".stripMargin,
          s"""  in_flight = true;
             |
             |  JsonObjectRequest req = new JsonObjectRequest(Request.GET,
             |    new Response.Listener<JSONObject>() {
             |    @Override
             |    public void onResponse(JSONObject response) {""".stripMargin,
          colored = true
        ) ==>
          s"""    in_flight = true;
             |
             |${RED}-   JsonObjectRequest req = new JsonObjectRequest(Request.POST,${RESET}
             |${GREEN}+   JsonObjectRequest req = new JsonObjectRequest(Request.GET,${RESET}
             |      new Response.Listener<JSONObject>() {
             |      @Override
             |      public void onResponse(JSONObject response) {""".stripMargin
      }
    }
  }
}
