package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditService {

    private SelenideElement cardNumberForm = $("[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthForm = $("[placeholder='08']");
    private SelenideElement yearForm = $("[placeholder='22']");
    private SelenideElement ownerForm = $$("[class='input__control']").get(3);
    private SelenideElement cvcForm = $("[placeholder='999']");
    private SelenideElement continueButton = $(byText("Продолжить"));
    private SelenideElement successNotification = $(byText("Операция одобрена Банком."));
    private SelenideElement errorNotification = $(byText("Ошибка! Банк отказал в проведении операции."));
    private SelenideElement emptyField = $(byText("Поле обязательно для заполнения"));
    private SelenideElement wrongFormat = $(byText("Неверный формат"));
    private SelenideElement wrongCardDate = $(byText("Неверно указан срок действия карты"));
    private SelenideElement wrongCardInfo = $(byText("Истёк срок действия карты"));

    public void filledForm(DataHelper.CardData cardData, DataHelper.MonthData monthData, DataHelper.YearData yearData, DataHelper.OwnerData ownerData, DataHelper.CvcData cvcData) {
        cardNumberForm.setValue(cardData.getCardNumber());
        monthForm.setValue(monthData.getMonth());
        yearForm.setValue(yearData.getYear());
        ownerForm.setValue(ownerData.getOwner());
        cvcForm.setValue(cvcData.getCvc());
        continueButton.click();
    }

    public void seeSuccessfulNotification() {
        successNotification.should(visible, Duration.ofSeconds(15));
    }

    public void seeErrorNotification() {
        errorNotification.should(visible, Duration.ofSeconds(15));
    }

    public void seeEmptyField() {
        emptyField.should(visible);
    }

    public void seeWrongFormat() {
        wrongFormat.should(visible);
    }

    public void seeWrongCardDate() {
        wrongCardDate.should(visible);
    }

    public void seeWrongCardInfo() {
        wrongCardInfo.should(visible);
    }

    public void fillCardField(DataHelper.CardData cardData) {
        cardNumberForm.setValue(cardData.getCardNumber());
    }

    public void fillMonthField(DataHelper.MonthData monthData) {
        monthForm.setValue(monthData.getMonth());
    }

    public void fillYearField(DataHelper.YearData yearData) {
        yearForm.setValue(yearData.getYear());
    }

    public void fillCvcField(DataHelper.CvcData cvcData) {
        cvcForm.setValue(cvcData.getCvc());
    }

    public void emptyFieldCard() {
        cardNumberForm.should(Condition.empty);
    }

    public void emptyFieldMonth() {
        monthForm.should(empty);
    }

    public void emptyFieldYear() {
        yearForm.should(empty);
    }

    public void emptyFieldCvc() {
        cvcForm.should(empty);
    }
}