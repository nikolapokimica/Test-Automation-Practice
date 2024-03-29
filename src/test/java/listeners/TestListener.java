package listeners;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import tests.BaseTest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

//Interfejs ITEstListener sadrzi vise metoda koje rukovode dogadjajima za testiranje.
//Za nase potrebe dovoljno je samo odraditi @Override na onTestFailure() metodu sa kodom koji ce da uradi screenshot ekrana kada test padne.
public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {

        //uzima instancu objekta koji je izazvao pad testa
        Object currentClass = result.getInstance();

        //svaki objekat ima svoju instancu driver-a, pa se uzima driver za ovaj konkretan objekat
        WebDriver driver = ((BaseTest) currentClass).getDriver();

        if (driver != null) {

            //definisemo jedinstveno ime screenshot-a sa vremenom kada je napravljen
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

            //uzima se screenshot
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            //cuva se kao jpg format
            File destFile = new File("test-output\\errorScreenshots\\" + result.getName() + "_"
                    + formater.format(calendar.getTime()) + "-" + Arrays.toString(result.getParameters()) + ".jpg");
            try {
                FileUtils.copyFile(scrFile, destFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //ako test pripada grupi InDevelopment, nakon fail-a setuje mu se status na Skip
            if (Arrays.asList(result.getMethod().getGroups()).contains("InDevelopment")) {
                result.setStatus(ITestResult.SKIP);
            }

            //dodaje se screenshot u testng report
            Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");

        }
        else System.out.println("Screenshot failed due to driver lost in parallel execution");
    }

}