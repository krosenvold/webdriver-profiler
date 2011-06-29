Build using "mvn install"

Add to your project using:

<pre>
       &lt;dependency>
          &lt;groupId>org.seleniumhq.selenium</groupId>
          &lt;artifactId>webdriver-diag-proxy</artifactId>
          &lt;version>0.9-SNAPSHOT</version>
      &lt;/dependency>
</pre>

Or just get the jar file from the target folder if you're using a lesser build system ;)


Using:

        WebDriver webDriver = .. Instantiate your faviourite webdriver ...;
        ProfilerFactory profilerFactory = new ProfilerFactory("perfLog");  // <number>.txt is appended
        webDriver = profilerFactory.createProxy( webDriver);

        ....     run tests ....

        profilerFactory.close();


        Use as normally. Full event is recorded to "perfLog0.txt" in this example. The profiler records the log when the
         close method is called on the factory or 5 seconds after the last browser is called.


The profilerfactory is thread-safe and createProxy can be run multiple times for each WebDriver instance you wish to proxy
