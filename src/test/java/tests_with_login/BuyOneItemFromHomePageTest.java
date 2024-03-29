package tests_with_login;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BuyOneItemFromHomePage;
import utilities.PropertyManager;

public class BuyOneItemFromHomePageTest extends BaseTestWithLogin {

    @Test(groups= {"TestsWithLogin"})
    public void buyStuff() {
        BuyOneItemFromHomePage page = new BuyOneItemFromHomePage(driver);
        page.openPage(PropertyManager.getInstance().getHomePageUrl());
        page.selectArticleAndAddToCart();

        try {
            verifyPage.verifyOrderConfirmation("Your order on My Store is complete.");
            System.out.println("BuyOneItemFromHomePageTest: Item is ordered");
        } catch (Exception e) {
            Assert.fail("Order wasn't completed");
        }
    }

}
