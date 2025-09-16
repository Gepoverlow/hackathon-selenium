import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class HackathonTest {

    private WebDriver driver;
    private WebDriverWait webDriverWait;
    private Actions actions;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
        driver = new ChromeDriver();
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        actions = new Actions(driver);
    }

    @Test
    public void testHackathon() throws InterruptedException {
        driver.get("https://hackthefuture.bignited.be/home");

        // Home Page
        driver.findElement(By.className("center-button")).click();

        // Character Select Page
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("male")));
        driver.findElement(By.id("male")).click();
        driver.findElement(By.xpath("//button[contains(text(), 'Yes')]")).click();
        driver.findElement(By.cssSelector("input[placeholder='Enter your name']")).sendKeys("Bob");
        driver.findElement(By.cssSelector("input[placeholder='Enter your age']")).sendKeys("33");
        WebElement countrySelect = driver.findElement(By.tagName("select"));
        Select select = new Select(countrySelect);
        select.selectByValue("Belgium");
        driver.findElement(By.className("center-button")).click();

        // Office Page
        driver.findElement(By.id("the-crystal")).click();
        driver.findElement(By.id("take-crystal")).click();
        driver.findElement(By.id("the-map")).click();
        driver.findElement(By.id("popup-image")).click();
        driver.findElement(By.className("door")).click();

        // Docking Bay Page
        List<WebElement> values = driver.findElements(By.className("randomValue"));
        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i).getText().trim();
            String switchId = "switch-" + i;
            WebElement switchElement = driver.findElement(By.id(switchId));
            
            if(value.equals("1")) {
                switchElement.click();
            } else if (value.equals("0")) {
                actions.doubleClick(switchElement).perform();
            }
        }
        driver.findElement(By.id("button")).click();
        driver.findElement(By.id("submarine")).click();

        // Submarine Page
        for (int i = 0; i < 10; i++) {
            WebElement arrow = driver.findElement(By.className("arrow"));
            String arrowSrc = arrow.getAttribute("src");
            String arrowDirection = arrowSrc.substring(arrowSrc.lastIndexOf("/") + 1);

            switch (arrowDirection) {
                case "right.png":
                    actions.sendKeys(Keys.ARROW_RIGHT).perform();
                    break;
                case "left.png":
                    actions.sendKeys(Keys.ARROW_LEFT).perform();
                    break;
                case "up.png":
                    actions.sendKeys(Keys.ARROW_UP).perform();
                    break;
                case "down.png":
                    actions.sendKeys(Keys.ARROW_DOWN).perform();
                    break;
                default:
                    System.out.println("Unknown direction: " + arrowDirection);
            }

            Thread.sleep(300);
        }

        // Crash Page
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.className("square")));
        actions.doubleClick(driver.findElement(By.className("square"))).perform();

        // Escape Page
        for(int i = 0; i < 4; i++) {
            WebElement door = driver.findElement(By.id("square-" + i));
            actions.moveToElement(door).perform();
            Thread.sleep(200);

            String hoveredDoor = door.getAttribute("class");

            if(hoveredDoor.contains("active")) {
                actions.doubleClick(door).perform();
                break;
            }
        }

        // Cave Page
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("word-target")));
        List<String> letters = Arrays.asList("A", "T", "L", "N", "I", "S");
        for (String letter: letters) {
            List<WebElement> sources = driver.findElements(By.cssSelector(String.format("[data-letter='%s'][draggable='true']", letter)));
            List<WebElement> targets = driver.findElements(By.cssSelector(String.format("[data-letter='%s']:not([draggable])", letter)));

            int count = Math.min(sources.size(), targets.size());
            System.out.println(count);

            for (int i = 0; i < count; i++) {
                WebElement source = sources.get(i);
                WebElement target = targets.get(i);

                actions.dragAndDrop(source, target).perform();

                Thread.sleep(300);
            }
        }
        driver.findElement(By.xpath("//button[contains(text(), 'dev-button 2')]")).click();

        // Boss Page
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("instructions-content")));
        actions.sendKeys(Keys.SPACE).perform();
        WebElement boss = driver.findElement(By.className("boss"));
        WebElement player = driver.findElement((By.id("character")));
        int bossX = boss.getLocation().getX();
        int playerX = player.getLocation().getX();

        // Do a while health of boss > 0 and adjust the movement of the players X based on the bosses X
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

