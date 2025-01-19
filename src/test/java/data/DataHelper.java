package data;

import com.github.javafaker.Faker;
import io.qameta.allure.Owner;
import lombok.Value;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {

    private DataHelper() {
    }

    public static CardData getApprovedCard() {
        return new CardData("4444444444444441");
    }

    public static String getApprovedCardStatus() {
        return new String("APPROVED");
    }

    public static CardData getDeclinedCard() {
        return new CardData("4444444444444442");
    }

    public static String getDeclinedCardStatus() {
        return new String("DECLINED");
    }

    public static CardData getGenerateInvalidCardData(String locale) {
        Faker faker = new Faker(new Locale(locale));
        var card = faker.name().firstName();
        return new CardData(card);
    }

    public static CardData getSpecialCharactersCardData() {
        return new CardData("$%#@!*(&%$^&^#@@");
    }

    public static CardData getEmptyFieldCardData() {
        return new CardData("");
    }

    public static MonthData getGenerateMonth(int shift) {
        var month = LocalDate.now().plusMonths(shift).format(DateTimeFormatter.ofPattern("MM"));
        return new MonthData(month);
    }

    public static MonthData getGenerateInvalidMonthInfo(String locale) {
        Faker faker = new Faker(new Locale(locale));
        var month = faker.name().firstName();
        return new MonthData(month);
    }

    public static MonthData getGenerateInvalidMonthData(int shift) {
        var month = LocalDate.now().minusMonths(shift).format(DateTimeFormatter.ofPattern("MM"));
        return new MonthData(month);
    }

    public static MonthData getEmptyFieldMonth() {
        return new MonthData("");
    }

    public static MonthData getInvalidAmountNumbersMonths(int digits) {
        Faker faker = new Faker();
        var month = faker.number().digits(digits);
        return new MonthData(month);
    }

    public static MonthData getEnterMonth(String enter) {
        return new MonthData(enter);
    }

    public static MonthData getSpecialCharactersMonth() {
        return new MonthData("&%");
    }

    public static YearData generateYear(int shift) {
        var year = LocalDate.now().plusYears(shift).format(DateTimeFormatter.ofPattern("yy"));
        return new YearData(year);
    }

    public static YearData generateYearMinus(int shift) {
        var year = LocalDate.now().minusYears(shift).format(DateTimeFormatter.ofPattern("yy"));
        return new YearData(year);
    }

    public static YearData getEmptyFieldYear() {
        return new YearData("");
    }

    public static YearData getGenerateInvalidYearInfo(String locale) {
        Faker faker = new Faker(new Locale(locale));
        var year = faker.name().firstName();
        return new YearData(year);
    }

    public static YearData getSpecialCharactersYear() {
        return new YearData("*$");
    }

    public static YearData getInvalidAmountNumbersYear(int digits) {
        Faker faker = new Faker();
        var year = faker.number().digits(digits);
        return new YearData(year);
    }

    public static YearData getGenerateInvalidYearDate(int shift) {
        var year = LocalDate.now().minusYears(shift).format(DateTimeFormatter.ofPattern("yy"));
        return new YearData(year);
    }

    public static OwnerData generateOwner(String locale) {
        Faker faker = new Faker(new Locale(locale));
        var owner = faker.name().lastName() + " " + faker.name().firstName();
        return new OwnerData(owner);
    }

    public static OwnerData getEmptyFieldOwner() {
        return new OwnerData("");
    }

    public static OwnerData getGenerateNumberOwner(int digits) {
        Faker faker = new Faker();
        var owner = faker.number().digits(digits);
        return new OwnerData(owner);
    }

    public static OwnerData getSpecialCharactersOwner() {
        return new OwnerData("$%#)(_@!$%");
    }

    public static CvcData generateCvc(int digits) {
        Faker faker = new Faker();
        var cvc = faker.number().digits(digits);
        return new CvcData(cvc);
    }

    public static CvcData getEmptyFieldCvc() {
        return new CvcData("");
    }

    public static CvcData getGenerateInvalidCvc(String locale) {
        Faker faker = new Faker(new Locale(locale));
        var cvc = faker.name().firstName();
        return new CvcData(cvc);
    }

    public static CvcData getSpecialCharactersCvc() {
        return new CvcData("*$@");
    }

    public static CvcData getEnterCvc(String enter) {
        return new CvcData(enter);
    }

    public static CvcData getInvalidAmountNumbersCvc(int digits) {
        Faker faker = new Faker();
        var cvc = faker.number().digits(digits);
        return new CvcData(cvc);
    }

    @Value
    public static class CardData {
        String cardNumber;
    }

    @Value
    public static class MonthData {
        String month;
    }

    @Value
    public static class YearData {
        String year;
    }

    @Value
    public static class OwnerData {
        String owner;
    }

    @Value
    public static class CvcData {
        String cvc;
    }
}