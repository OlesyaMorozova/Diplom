package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.DataHelperSQL;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.CreditService;
import page.Main;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTest {
    private Main main;
    private CreditService creditService;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener(("allure"));
    }

    @BeforeEach
    public void setup() {
        main = open("http://localhost:8080/", Main.class);
    }

    @AfterEach
    void clean() {
        DataHelperSQL.clear();
    }

    @Test
    public void shouldPaymentByActiveCard() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeSuccessfulNotification();
        var expected = DataHelper.getApprovedCardStatus();
        var actual = DataHelperSQL.getCreditStatus();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldRejectTheBlockedCard() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getDeclinedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeErrorNotification();
        var expected = DataHelper.getDeclinedCardStatus();
        var actual = DataHelperSQL.getCreditStatus();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldRejectedEmptyCardNumberField() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getEmptyFieldCardData();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeEmptyField();
    }

    @Test
    public void shouldRejectedEmptyMonthField() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getEmptyFieldMonth();
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeEmptyField();
    }

    @Test
    public void shouldRejectedEmptyYearField() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.getEmptyFieldYear();
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeEmptyField();
    }

    @Test
    public void shouldRejectedEmptyOwnerField() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.getEmptyFieldOwner();
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeEmptyField();
    }

    @Test
    public void shouldRejectedEmptyCvcField() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.getEmptyFieldCvc();
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeEmptyField();
    }

    @Test
    public void shouldBeRejectedLastYear() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYearMinus(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeWrongCardInfo();
    }

    @Test
    public void shouldAbandonTheCyrillicAlphabetInTheCardNumber() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getGenerateInvalidCardData("RU");
        creditService.fillCardField(cardNumber);
        creditService.emptyFieldCard();
    }

    @Test
    public void shouldBeExcludedFromTheCardNumberSpecialCharacters() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getSpecialCharactersCardData();
        creditService.fillCardField(cardNumber);
        creditService.emptyFieldCard();
    }

    @Test
    public void shouldAbandonTheCyrillicAlphabetInTheMonth() {
        creditService = main.payWithCreditCard();
        var month = DataHelper.getGenerateInvalidMonthInfo("RU");
        creditService.fillMonthField(month);
        creditService.emptyFieldMonth();
    }

    @Test
    public void shouldBeExcludedFromTheMonthSpecialCharacters() {
        creditService = main.payWithCreditCard();
        var month = DataHelper.getSpecialCharactersMonth();
        creditService.fillMonthField(month);
        creditService.emptyFieldMonth();
    }

    @Test
    public void shouldBeRejectedOneFigurePerMonth() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getInvalidAmountNumbersMonths(1);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeWrongFormat();
    }

    @Test
    public void shouldAbandonTheCyrillicAlphabetInTheYear() {
        creditService = main.payWithCreditCard();
        var year = DataHelper.getGenerateInvalidYearInfo("RU");
        creditService.fillYearField(year);
        creditService.emptyFieldYear();
    }

    @Test
    public  void shouldBeExcludedFromTheYearSpecialCharacters() {
        creditService = main.payWithCreditCard();
        var year = DataHelper.getSpecialCharactersYear();
        creditService.fillYearField(year);
        creditService.emptyFieldYear();
    }

    @Test
    public void shouldBeRejectedOneFigurePerYear() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.getInvalidAmountNumbersYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeWrongFormat();
    }

    @Test
    public void shouldBeAbandonedYearFromTheDistantFuture() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(6);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeWrongCardDate();
    }

    @Test
    public void shouldBeRejectedOwnerInCyrillic() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("RU");
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeWrongFormat();
    }

    @Test
    public void shouldBeRejectedOwnerWithSpecialCharacters() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.getSpecialCharactersOwner();
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeWrongFormat();
    }

    @Test
    public void shouldBeRejectedOwnerWithNumbers() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.getGenerateNumberOwner(7);
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeWrongFormat();
    }

    @Test
    public void shouldAbandonTheCyrillicAlphabetInTheCvc() {
        creditService = main.payWithCreditCard();
        var cvc = DataHelper.getGenerateInvalidCvc("RU");
        creditService.fillCvcField(cvc);
        creditService.emptyFieldCvc();
    }

    @Test
    public void shouldBeExcludedFromTheCvcSpecialCharacters() {
        creditService = main.payWithCreditCard();
        var cvc = DataHelper.getSpecialCharactersCvc();
        creditService.fillCvcField(cvc);
        creditService.emptyFieldCvc();
    }

    @Test
    public void shouldRejectZeroInTheMonthField() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getEnterMonth("00");
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeWrongCardDate();
    }

    @Test
    public void shouldBeRejectedNonExistentMonth() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getEnterMonth("13");
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeWrongCardDate();
    }

    @Test
    public void shouldRejectZeroInTheCvcField() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.getEnterCvc("000");
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeWrongFormat();
    }

    @Test
    public void shouldAddCreditInOrderEntry() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        var expected = DataHelperSQL.getCreditRequestReEntryId();
        var actual = DataHelperSQL.getCreditOrderEntryId();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldAddCreditInOrderEntryStatusApproved() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        var expected = DataHelperSQL.getCreditRequestReEntryId();
        var actual = DataHelperSQL.getCreditOrderEntryId();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldBeRejectedTwoDigitsInCvc() {
        creditService = main.payWithCreditCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.getInvalidAmountNumbersCvc(2);
        creditService.filledForm(cardNumber, month, year, owner, cvc);
        creditService.seeWrongFormat();
    }
}