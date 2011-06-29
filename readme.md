Using:

Build using "mvn install"

Add to your project using:

<pre>
       &lt;dependency>
        &lt;groupId>org.seleniumhq.selenium</groupId>
        &lt;artifactId>webdriver-diag-proxy</artifactId>
        &lt;version>0.9-SNAPSHOT</version>
      &lt;/dependency>
</pre>

Or just get the jar file from the target folder if you're using a lesser build system.


Using:

        WebDriver webDriver = getApproproateWebDriverImpl();
        WebDriverProxyFactory profilerFactory = new ProfilerFactory("perfLog");  // <number>.txt is appended
        webDriver = profilerFactory.createProxy( webDriver);


        Use as normally. Full event is recorded to "fud.txt" in this example.


The profilerfactory is thread-safe and createProxy can be run multiple times for each WebDriver instance you wish to proxy
