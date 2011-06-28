Using:

Build using "mvn install"

Add to your project using:

<pre>
       <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>webdriver-diag-proxy</artifactId>
        <version>0.9-SNAPSHOT</version>
      </dependency>
</pre>

Or just get the jar file from the target folder if you're using a lesser build system.


Using:

        WebDriver webDriver = getApproproateWebDriverImpl();
        WebDriverProxyFactory webDriverProxyFactory = new WebDriverProxyFactory("perfLog");  // <number>.txt is appended
        webDriver = webDriverProxyFactory.createProxy( webDriver);


        Use as normally. Full event is recorded to "fud.txt" in this example.
