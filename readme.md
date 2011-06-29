Build using "mvn install"

Add to your project using:

<pre>
       &lt;dependency>
          &lt;groupId>org.seleniumhq.selenium&lt;/groupId>
          &lt;artifactId>webdriver-diag-proxy&lt;/artifactId>
          &lt;version>0.9&lt;/version>
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
close method is called on the factory or 5 seconds after the last browser is called (assuming the process lives that long, which
I'd normally only guarantee on selenium-server !).


The profilerfactory is thread-safe and createProxy can be run multiple times for each WebDriver instance you wish to proxy


If you are running se2.0 release version you can also embed this:
java -cp webdriver-profiler-0.9.jar:server-standalone.jar org.openqa.selenium.server.SeleniumServer
