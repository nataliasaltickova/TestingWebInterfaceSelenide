import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class OrderingCardTest {


    @BeforeEach
    public void setUp() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @Test
    public void shouldSendForm() {

        $x("//*[@placeholder=\"Город\"]").setValue("Саратов");
        $x("// input [@placeholder ='Дата встречи']").setValue(generateDate(4));
        $("[data-test-id='name'] input").setValue("Наталия Владимировна Сухова");
        $("[data-test-id='phone'] input").setValue("+79271270335");
        $x("//label [contains (@class,\"checkbox_size_m\")]").click();
        $x("//span [text() =\"Забронировать\"]").click();

        $("[class= notification__title]").shouldBe(Condition.text("Успешно!"), Duration.ofSeconds(15));
        $("[class= notification__content]").shouldBe(Condition.text("Встреча успешно забронирована на "),Condition.text(generateDate(3)));




    }

    @Test
    public void shouldSendFormWhenInvalidCity() {

        $x("//*[@placeholder=\"Город\"]").setValue("Энгельс");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("// input [@placeholder ='Дата встречи']").setValue(generateDate(4));
        $("[data-test-id='name'] input").setValue("Наталия Владимировна Сухова");
        $("[data-test-id='phone'] input").setValue("+79271270335");
        $x("//label [contains (@class,\"checkbox_size_m\")]").click();
        $x("//span [text() =\"Забронировать\"]").click();
        $$x("//span [@class=\"input__sub\"]").get(0).shouldBe(Condition.text("Доставка в выбранный город недоступна "), Duration.ofSeconds(15));
    }

    @Test
    public void shouldSendFormWhenDataEqualsCurrent() {

        $x("//*[@placeholder=\"Город\"]").setValue("Саратов");
        $x("//input[@placeholder =\"Дата встречи\"]").doubleClick();
        $("[data-test-id=date] input").sendKeys(" ");
        $x("//input[@placeholder =\"Дата встречи\"]").setValue(generateDate(0));
        $("[data-test-id='name'] input").setValue("Наталия Владимировна Сухова");
        $("[data-test-id='phone'] input").setValue("+79271270335");
        $x("//label [contains (@class,\"checkbox_size_m\")]").click();
        $x("//span [text() =\"Забронировать\"]").click();
        $$x("//span [@class=\"input__sub\"]").get(1).shouldBe(Condition.text("Заказ на выбранную дату невозможен"), Duration.ofSeconds(15));
    }

    @Test
    public void shouldSendFormWhenInvalidName() {

        $x("//*[@placeholder=\"Город\"]").setValue("Саратов");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("// input [@placeholder ='Дата встречи']").setValue(generateDate(4));
        $("[data-test-id='name'] input").setValue("Natalia");
        $("[data-test-id='phone'] input").setValue("+79271270335");
        $x("//label [contains (@class,\"checkbox_size_m\")]").click();
        $x("//span [text() =\"Забронировать\"]").click();
        $$x("//span [@class=\"input__sub\"]").get(2).shouldBe(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."), Duration.ofSeconds(15));
    }

    @Test
    public void shouldSendFormWhenInvalidPhone() {

        $x("//*[@placeholder=\"Город\"]").setValue("Саратов");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("// input [@placeholder ='Дата встречи']").setValue(generateDate(4));
        $("[data-test-id='name'] input").setValue("Наталия Владимировна Сухова");
        $("[data-test-id='phone'] input").setValue("+0000000000");
        $x("//label [contains (@class,\"checkbox_size_m\")]").click();
        $x("//span [text() =\"Забронировать\"]").click();
        $$x("//span [@class=\"input__sub\"]").get(3).shouldBe(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."), Duration.ofSeconds(15));
    }

    @Test
    public void shouldSendFormWhenNotClickCheckBox() {

        $x("//*[@placeholder=\"Город\"]").setValue("Саратов");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("// input [@placeholder ='Дата встречи']").setValue(generateDate(4));
        $("[data-test-id='name'] input").setValue("Наталия Владимировна Сухова");
        $("[data-test-id='phone'] input").setValue("+79271270335");
        $x("//span [text() =\"Забронировать\"]").click();
        $x("//span [@class=\"checkbox__text\"]").shouldBe(Condition.text("Я соглашаюсь с условиями обработки и использования моих персональных данных"), Duration.ofSeconds(15));
    }

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}


