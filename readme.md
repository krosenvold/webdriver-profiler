Using:

        WebDriver webDriver = getApproproateWebDriverImpl();
        WebDriverProxyFactory webDriverProxyFactory = new WebDriverProxyFactory("fud.txt");
        webDriver = webDriverProxyFactory.createProxy( webDriver);


        Use as normally.
