package page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class Main {

    private SelenideElement buyForm = $(byText("Купить"));
    private SelenideElement buyByCreditForm = $(byText("Купить в кредит"));

    public PaymentService payWithDebitCard() {
        buyForm.click();
        return new PaymentService();
    }

    public CreditService payWithCreditCard() {
        buyByCreditForm.click();
        return new CreditService();
    }
}