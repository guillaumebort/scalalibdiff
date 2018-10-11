# scalalibdiff

: a very simple diff library

The main feature is to display text diff in a human readable format, such as:

```
    in_flight = true;

-   JsonObjectRequest req = new JsonObjectRequest(Request.POST,</span>
+   JsonObjectRequest req = new JsonObjectRequest(Request.GET,</span>
      new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
```

But you can also use the low level API to customize the way diff are displayed.

There are certainly better and more advanced text diff library for the JVM but this one is compiled to ScalaJS. I use it mainly for good reporting in my tests (and I like to be able to run my test suite fully in Javascript for ScalaJS libraries).


## Add the dependency

Add the following to your SBT config:

```
libraryDependencies += "com.github.guillaumebort" %% "scalalibdiff" % "0.1.0"
```

For ScalaJS applications, use this dependencies instead:

```
libraryDependencies += "com.github.guillaumebort" %%% "scalalibdiff" % "0.1.0"
```