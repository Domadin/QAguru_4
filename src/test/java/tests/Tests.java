package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Feature;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import pages.Steps;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static io.qameta.allure.Allure.step;

public class Tests {

    private final static String issueName = "TestIssue";
    private final static String assignee = "DomadinTest";
    private final static String label1 = "bug";
    private final static String label2 = "documentation";
    private final static String repoName = "TestRepo";

    @BeforeAll
    @DisplayName("Установить параметры и выполнить вход в систему")
    public static void setUp() {
        Configuration.timeout = 8000;
        SelenideLogger.addListener("allure", new AllureSelenide());
        Configuration.browserSize = "1920x1080";

        new Steps()
                .openMainPage()
                .login();
    }

    @BeforeEach
    @DisplayName("Вернуться на главную страницу")
    public void returnToMainPage() {
        new Steps().openMainPage();
    }

    @AfterAll
    @DisplayName("Удалине allure лисенера")
    public static void tearDown() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Тест с аннотациями")
    @Feature("Создать issue в репозитории " + repoName)
    public void githubCreateIssueTest() {
        new Steps().enterRepo(repoName)
                .createNewIssue(issueName, assignee, label1, label2)
                .assertIssueInfo(issueName, assignee, label1, label2)
                .deleteIssue(issueName);
    }

    @Test
    @DisplayName("Чистый selenide")
    public void cleanSelenideTest() {
        $(byText(repoName)).click();
        $(by("data-tab-item", "issues-tab")).click();
        $(".repository-content").$(byText("New issue")).click();

        $("#issue_title").val(issueName);
        //Добавление assignee
        $(byText("Assignees")).click();
        $$(".select-menu-item").findBy(text(assignee)).click();
        $(byText("Assignees")).click();
        //Добавление labels
        $(byText("Labels")).click();
        $$(".label-select-menu-item").findBy(text(label1)).click();
        $$(".label-select-menu-item").findBy(text(label2)).click();
        $(byText("Labels")).click();
        $(byText("Submit new issue")).click();

        $(by("data-tab-item", "issues-tab")).click();
        $(byText(issueName)).click();
        $("#partial-discussion-header").shouldHave(text(issueName));
        $(".assignee").shouldHave(text(assignee));
        $(".labels").shouldHave(text(label1));
        $(".labels").shouldHave(text(label2));

        $(by("data-tab-item", "issues-tab")).click();
        $(byText(issueName)).click();
        $(byText("Delete issue")).click();
        $(byName("verify_delete")).click();
    }

    @Test
    @DisplayName("Лямбда steps")
    public void lambdaTest() {
        step("Войти в репозиторий", () -> {
            $(byText(repoName)).click();
        });

        step("Создать Issue", () -> {
            $(by("data-tab-item", "issues-tab")).click();
            $(".repository-content").$(byText("New issue")).click();

            $("#issue_title").val(issueName);

            //Добавление assignee
            $(byText("Assignees")).click();
            $$(".select-menu-item").findBy(text(assignee)).click();
            $(byText("Assignees")).click();

            //Добавление labels
            $(byText("Labels")).click();
            $$(".label-select-menu-item").findBy(text(label1)).click();
            $$(".label-select-menu-item").findBy(text(label2)).click();
            $(byText("Labels")).click();

            $(byText("Submit new issue")).click();
        });

        step("Убедиться в наличии Issue", () -> {
            $(by("data-tab-item", "issues-tab")).click();

            $(byText(issueName)).click();
            $("#partial-discussion-header").shouldHave(text(issueName));
            $(".assignee").shouldHave(text(assignee));
            $(".labels").shouldHave(text(label1));
            $(".labels").shouldHave(text(label2));
        });


        step("Удалить Issue", () -> {
            $(by("data-tab-item", "issues-tab")).click();
            $(byText(issueName)).click();
            $(byText("Delete issue")).click();
            $(byName("verify_delete")).click();
        });
    }
}