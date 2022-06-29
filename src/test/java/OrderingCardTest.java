import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

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
        $x("// input [@placeholder ='Дата встречи']").setValue("02.07.2022");
        $("[data-test-id='name'] input").setValue("Наталия Владимировна Сухова");
        $("[data-test-id='phone'] input").setValue("+79271270335");
        $x("//label [@class =\"checkbox checkbox_size_m checkbox_theme_alfa-on-white\"]").click();
        $x("//span [text() =\"Забронировать\"]").click();

        $x("//div [@data-test-id=\"notification\"]").shouldBe(Condition.text("Встреча успешно забронирована на 02.07.2022"), Duration.ofSeconds(15));
    }

    @Test
    public void shouldSendFormWhenInvalidCity() {

        $x("//*[@placeholder=\"Город\"]").setValue("Энгельс");
        $x("// input [@placeholder ='Дата встречи']").setValue("01.07.2022");
        $("[data-test-id='name'] input").setValue("Наталия Владимировна Сухова");
        $("[data-test-id='phone'] input").setValue("+79271270335");
        $x("//label [@class =\"checkbox checkbox_size_m checkbox_theme_alfa-on-white\"]").click();
        $x("//span [text() =\"Забронировать\"]").click();
        $$x("//span [@class=\"input__sub\"]").get(0).shouldBe(Condition.text("Доставка в выбранный город недоступна "), Duration.ofSeconds(15));
    }

    @Test
    public void shouldSendFormWhenDataEqualsCurrent() {

        $x("//*[@placeholder=\"Город\"]").setValue("Саратов");
        $x("//input[@placeholder =\"Дата встречи\"]").doubleClick();
        $("[data-test-id=date] input").sendKeys(" ");
        $x("//input[@placeholder =\"Дата встречи\"]").setValue("28.06.2022");
        $("[data-test-id='name'] input").setValue("Наталия Владимировна Сухова");
        $("[data-test-id='phone'] input").setValue("+79271270335");
        $x("//label [@class =\"checkbox checkbox_size_m checkbox_theme_alfa-on-white\"]").click();
        $x("//span [text() =\"Забронировать\"]").click();
        $$x("//span [@class=\"input__sub\"]").get(1).shouldBe(Condition.text("Заказ на выбранную дату невозможен"), Duration.ofSeconds(15));
    }

    @Test
    public void shouldSendFormWhenInvalidName() {


        $x("//*[@placeholder=\"Город\"]").setValue("Саратов");
        $x("// input [@placeholder ='Дата встречи']").setValue("01.07.2022");
        $("[data-test-id='name'] input").setValue("Natalia");
        $("[data-test-id='phone'] input").setValue("+79271270335");
        $x("//label [@class =\"checkbox checkbox_size_m checkbox_theme_alfa-on-white\"]").click();
        $x("//span [text() =\"Забронировать\"]").click();
        $$x("//span [@class=\"input__sub\"]").get(2).shouldBe(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."), Duration.ofSeconds(15));
    }

    @Test
    public void shouldSendFormWhenInvalidPhone() {


        $x("//*[@placeholder=\"Город\"]").setValue("Саратов");
        $x("// input [@placeholder ='Дата встречи']").setValue("01.07.2022");
        $("[data-test-id='name'] input").setValue("Наталия Владимировна Сухова");
        $("[data-test-id='phone'] input").setValue("+0000000000");
        $x("//label [@class =\"checkbox checkbox_size_m checkbox_theme_alfa-on-white\"]").click();
        $x("//span [text() =\"Забронировать\"]").click();
        $$x("//span [@class=\"input__sub\"]").get(3).shouldBe(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."), Duration.ofSeconds(15));
    }
    @Test
    public void shouldSendFormWhenNotClickCheckBox() {

        $x("//*[@placeholder=\"Город\"]").setValue("Саратов");
        $x("// input [@placeholder ='Дата встречи']").setValue("01.07.2022");
        $("[data-test-id='name'] input").setValue("Наталия Владимировна Сухова");
        $("[data-test-id='phone'] input").setValue("+79271270335");
        $x("//span [text() =\"Забронировать\"]").click();
        $x("//span [@class=\"checkbox__text\"]").shouldBe(Condition.text("Я соглашаюсь с условиями обработки и использования моих персональных данных"), Duration.ofSeconds(15));
    }
}


