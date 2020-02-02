import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.devicefarm.DeviceFarmClient;
import software.amazon.awssdk.services.devicefarm.model.CreateTestGridUrlRequest;
import software.amazon.awssdk.services.devicefarm.model.CreateTestGridUrlResponse;

import java.net.URL;

public class AwsDeviceFarm {
    private static RemoteWebDriver driver;

    @BeforeTest
    void setUp() {
        try {
            String myProjectARN = "YOUR PROJECT ARN";
            DeviceFarmClient client = DeviceFarmClient.builder()
                    .region(Region.US_WEST_2)
                    .build();
            CreateTestGridUrlRequest request = CreateTestGridUrlRequest.builder()
                    .expiresInSeconds(300)
                    .projectArn(myProjectARN)
                    .build();
            CreateTestGridUrlResponse response = client.createTestGridUrl(request);
            URL testGridUrl = new URL(response.url());
            driver = new RemoteWebDriver(testGridUrl, DesiredCapabilities.chrome());
        } catch (Exception ex ) {
            ex.printStackTrace();
        }
    }

    @Test
    public void userLogin() {
        driver.manage().window().maximize();
        driver.navigate().to("https://the-internet.herokuapp.com/login");
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.className("radius")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("secure"));
    }

    @AfterTest
    void tearDown() {
        driver.quit();
    }
}
