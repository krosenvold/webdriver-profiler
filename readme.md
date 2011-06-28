Using:

Build using "mvn install"

Add to yout project using:
       <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>webdriver-diag-proxy</artifactId>
        <version>0.9-SNAPSHOT</version>
      </dependency>

      Or just get the jar file from the target folder if you're using a lesser build system.


Using:

        WebDriver webDriver = getApproproateWebDriverImpl();
        WebDriverProxyFactory webDriverProxyFactory = new WebDriverProxyFactory("fud.txt");
        webDriver = webDriverProxyFactory.createProxy( webDriver);


        Use as normally. Full event is recorded to "fud.txt" in this example.
