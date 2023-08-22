import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginValidationTest {
   /* 1. Open https://sharelane.com/cgi-bin/register.py
    2. Enter 12345 as ZIP
    3. Click Continue
    4. Click Register
    5. Check Validation    */

    private WebDriver driver;

    @BeforeClass
    public void startBrowser(){
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--incognito");
        chromeOptions.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();
    }

    @Test
    public void verifyLoginValidation() {
        driver.get("https://sharelane.com/cgi-bin/register.py");
        driver.findElement(By.xpath("//input[@name='zip_code']")).sendKeys("12345");
        driver.findElement(By.xpath("//input[@value='Continue']")).click();
        driver.findElement(By.xpath("//input[@value='Register']")).click();
        String errorText = driver.findElement(By.xpath("//span[@class='error_message']")).getText();
        Assert.assertEquals(errorText, "Oops, error on page. Some of your fields have invalid data or email was previously used");
        //driver.quit(); - нельзя писать после hard Assert, т.к. в случае failed - код после асерта не выполняется
        //SoftAssert softAssert = new SoftAssert();
        //soft.Assert.assertEquals(errorText, "Oops, error on page")
        //softAssert.assertAll(); - проверяет и выводит, что из ассертов прошло, а что нет
    }

    @Test
    public void verifyConfirmPasswordValidation() {
        driver.get("https://sharelane.com/cgi-bin/register.py");
        driver.findElement(By.xpath("//input[@name='zip_code']")).sendKeys("12345");
        driver.findElement(By.xpath("//input[@value='Continue']")).click();
        driver.findElement(By.xpath("//input[@name='first_name']")).sendKeys("First");
        driver.findElement(By.xpath("//input[@name='email']")).sendKeys("mail.com");
        driver.findElement(By.xpath("//input[@name='password1']")).sendKeys("Password");
        driver.findElement(By.xpath("//input[@name='password2']")).sendKeys("Password");
        driver.findElement(By.xpath("//input[@value='Register']")).click();
        String errorText = driver.findElement(By.xpath("//span[@class='error_message']")).getText();
        Assert.assertEquals(errorText, "Oops, error on page. Some of your fields have invalid data or email was previously used");
    }

    @Test
    public void verifyValidLogin() {
        driver.get("https://sharelane.com/cgi-bin/register.py");
        driver.findElement(By.xpath("//input[@name='zip_code']")).sendKeys("12345");
        driver.findElement(By.xpath("//input[@value='Continue']")).click();
        driver.findElement(By.xpath("//input[@name='first_name']")).sendKeys("First");
        driver.findElement(By.xpath("//input[@name='email']")).sendKeys("test@mail.com");
        driver.findElement(By.xpath("//input[@name='password1']")).sendKeys("Password");
        driver.findElement(By.xpath("//input[@name='password2']")).sendKeys("Password");
        driver.findElement(By.xpath("//input[@value='Register']")).click();
        String email = driver.findElement(By.xpath("/html/body/center/table/tbody/tr[6]/td/table/tbody/tr[4]/td/table/tbody/tr[1]/td[2]/b")).getText();
        String password = driver.findElement(By.xpath("/html/body/center/table/tbody/tr[6]/td/table/tbody/tr[4]/td/table/tbody/tr[2]/td[2]")).getText();
        driver.findElement(By.xpath("//img")).click();

        driver.findElement(By.xpath("//input[@name='email']")).sendKeys(email);
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);
        driver.findElement(By.xpath("//input[@value='Login']")).click();
        String welcomeText = driver.findElement(By.xpath("//span[@class='user']")).getText();
        System.out.println(welcomeText);
        String[] partWelcome = welcomeText.split(" ");
        Assert.assertEquals(partWelcome[1], "Hello");    }

    @AfterClass //выполняется после каждого тестового метода
    public void closeBrowser(){

        //driver.quit();
    }
}
