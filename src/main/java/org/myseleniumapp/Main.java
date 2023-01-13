package org.myseleniumapp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.*;

import java.util.Random;


public class Main {
    public static void main(String[] args) {

        String URL = "https://apps.run-29.slot-35.tanzu-gss-labs.vmware.com";

        //when called from command line java -jar JarExample.jar "arg 1" "arg 2"..etc
        //first arg is how long the broswer should stay open
        //second arg is delay between browswer respawns
        //third arg is number of browswers
        //fourth arg is location of chromedriver
        long browswerStayOpenDurationInMillis = Long.parseLong(args[0]);
        long delayBetweenBrowserSpawns = Long.parseLong(args[1]);
        int numberOfBrowsersToSpawn = Integer.parseInt(args[2]);
        String chromeDriverLocation = args[3];



        for(int i = 0; i < numberOfBrowsersToSpawn; i++){

            new Thread(new Runnable() {
                WebDriver webDriver = null;

                @Override
                public void run() {

                    try{

                        // chrome didn't work easily on ubuntu like it did mac - leaving here for reference though
//                        System.setProperty("webdriver.chrome.driver",chromeDriverLocation);
//                        ChromeOptions chromeOptions = new ChromeOptions();
//                        chromeOptions.addArguments("ignore-certificate-errors");
//                        webDriver = new ChromeDriver(chromeOptions);

                        DesiredCapabilities capabilities = new DesiredCapabilities();
                        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                        System.setProperty("webdriver.gecko.driver",chromeDriverLocation);
                        FirefoxOptions firefoxOptions = new FirefoxOptions(capabilities);
                        firefoxOptions.setProfile(new FirefoxProfile());
                        webDriver = new FirefoxDriver(firefoxOptions);

                        webDriver.navigate().to(URL);

                        sleep(10000);

                        int randUserNumber = getRandomNumForUserLogin();
                        String USERNAME = "my-username-" + randUserNumber;
                        String PASSWORD = "my-password-" + randUserNumber;

                        webDriver.findElement(By.name("username")).sendKeys(USERNAME);
                        webDriver.findElement(By.name("password")).sendKeys(PASSWORD);
                        webDriver.findElement(By.className("island-button")).click();

                        sleep(10000);

                        webDriver.findElement(By.cssSelector("tr:nth-of-type(1) > th > .apps-manager-anchor > .anchor-text")).click();

                        sleep(10000);

                        if(shouldNavigateToSpace()){
                            webDriver.findElement(By.cssSelector("tr:nth-of-type(1) > th > .apps-manager-anchor > .anchor-text")).click();
                            sleep(10000);
                            webDriver.findElement(By.cssSelector("li:nth-of-type(4) > .apps-manager-anchor.mlxl.plxl.pvl > .anchor-text.type-ellipsis")).click();
                        }else{
                            webDriver.findElement(By.cssSelector("li:nth-of-type(4) > .apps-manager-anchor.mlxl.plxl.pvl > .anchor-text.type-ellipsis")).click();
                        }

                        sleep(browswerStayOpenDurationInMillis);

                    }catch (Exception e){
                        System.out.println("Exception hit: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }finally {
                        webDriver.close();
                    }

                }
            }).start();

            sleep(delayBetweenBrowserSpawns);
        }

    }

    static int getRandomNumForUserLogin(){
        return new Random().nextInt(299980) + 15;
    }

    static boolean shouldNavigateToSpace(){
        int num = new Random().nextInt(10);
        if (num < 5){
            return false;
        }else{
            return true;
        }
    }

    static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}