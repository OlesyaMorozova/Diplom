package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.DataHelperSQL;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.Main;
import page.PaymentService;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PaymentTest {
    private Main main;
    private PaymentService paymentService;

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
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeSuccessfulNotification();
        var expected = DataHelper.getApprovedCardStatus();
        var actual = DataHelperSQL.getDebitStatus();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldRejectTheBlockedCard() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getDeclinedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeErrorNotification();
        var expected = DataHelper.getDeclinedCardStatus();
        var actual = DataHelperSQL.getDebitStatus();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldRejectedEmptyCardNumberField() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getEmptyFieldCardData();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeEmptyField();
    }

    @Test
    public void shouldRejectedEmptyMonthField() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getEmptyFieldMonth();
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeEmptyField();
    }

    @Test
    public void shouldRejectedEmptyYearField() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.getEmptyFieldYear();
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeEmptyField();
    }

    @Test
    public void shouldRejectedEmptyOwnerField() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.getEmptyFieldOwner();
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeEmptyField();
    }

    @Test
    public void shouldRejectedEmptyCvcField() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.getEmptyFieldCvc();
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeEmptyField();
    }

    @Test
    public void shouldBeRejectedLastYear() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYearMinus(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeWrongCardInfo();
    }

    @Test
    public void shouldAbandonTheCyrillicAlphabetInTheCardNumber() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getGenerateInvalidCardData("RU");
        paymentService.fillCardField(cardNumber);
        paymentService.emptyFieldCard();
    }

    @Test
    public void shouldBeExcludedFromTheCardNumberSpecialCharacters() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getSpecialCharactersCardData();
        paymentService.fillCardField(cardNumber);
        paymentService.emptyFieldCard();
    }

    @Test
    public void shouldAbandonTheCyrillicAlphabetInTheMonth() {
        paymentService = main.payWithDebitCard();
        var month = DataHelper.getGenerateInvalidMonthInfo("RU");
        paymentService.fillMonthField(month);
        paymentService.emptyFieldMonth();
    }

    @Test
    public void shouldBeExcludedFromTheMonthSpecialCharacters() {
        paymentService = main.payWithDebitCard();
        var month = DataHelper.getSpecialCharactersMonth();
        paymentService.fillMonthField(month);
        paymentService.emptyFieldMonth();
    }

    @Test
    public void shouldBeRejectedOneFigurePerMonth() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getInvalidAmountNumbersMonths(1);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeWrongFormat();
    }

    @Test
    public void shouldAbandonTheCyrillicAlphabetInTheYear() {
        paymentService = main.payWithDebitCard();
        var year = DataHelper.getGenerateInvalidYearInfo("RU");
        paymentService.fillYearField(year);
        paymentService.emptyFieldYear();
    }

    @Test
    public  void shouldBeExcludedFromTheYearSpecialCharacters() {
        paymentService = main.payWithDebitCard();
        var year = DataHelper.getSpecialCharactersYear();
        paymentService.fillYearField(year);
        paymentService.emptyFieldYear();
    }

    @Test
    public void shouldBeRejectedOneFigurePerYear() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.getInvalidAmountNumbersYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeWrongFormat();
    }

    @Test
    public void shouldBeAbandonedYearFromTheDistantFuture() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(6);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeWrongCardDate();
    }

    @Test
    public void shouldBeRejectedOwnerInCyrillic() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("RU");
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeWrongFormat();
    }

    @Test
    public void shouldBeRejectedOwnerWithSpecialCharacters() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.getSpecialCharactersOwner();
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeWrongFormat();
    }

    @Test
    public void shouldBeRejectedOwnerWithNumbers() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.getGenerateNumberOwner(7);
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeWrongFormat();
    }

    @Test
    public void shouldAbandonTheCyrillicAlphabetInTheCvc() {
        paymentService = main.payWithDebitCard();
        var cvc = DataHelper.getGenerateInvalidCvc("RU");
        paymentService.fillCvcField(cvc);
        paymentService.emptyFieldCvc();
    }

    @Test
    public void shouldBeExcludedFromTheCvcSpecialCharacters() {
        paymentService = main.payWithDebitCard();
        var cvc = DataHelper.getSpecialCharactersCvc();
        paymentService.fillCvcField(cvc);
        paymentService.emptyFieldCvc();
    }

    @Test
    public void shouldRejectZeroInTheMonthField() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getEnterMonth("00");
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeWrongCardDate();
    }

    @Test
    public void shouldBeRejectedNonExistentMonth() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getEnterMonth("13");
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeWrongCardDate();
    }

    @Test
    public void shouldRejectZeroInTheCvcField() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.getEnterCvc("000");
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeWrongFormat();
    }

    @Test
    public void shouldAddCreditInOrderEntry() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        var expected = DataHelperSQL.getDebitPaymentID();
        var actual = DataHelperSQL.getDebitOrderEntryId();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldDonTAddCreditInOrderEntryStatusDeclined() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getDeclinedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCvc(3);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        var expected = DataHelperSQL.getDebitPaymentID();
        var actual = DataHelperSQL.getDebitOrderEntryId();
        assertNotEquals(expected, actual);
    }

    @Test
    public void shouldBeRejectedTwoDigitsInCvc() {
        paymentService = main.payWithDebitCard();
        var cardNumber = DataHelper.getApprovedCard();
        var month = DataHelper.getGenerateMonth(0);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.getInvalidAmountNumbersCvc(2);
        paymentService.filledForm(cardNumber, month, year, owner, cvc);
        paymentService.seeWrongFormat();
    }
}