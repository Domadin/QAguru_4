package pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static pages.PropsManager.testUserLogin;
import static pages.PropsManager.testUserPass;

public class Steps {

    @Step("Открываем главную страницу")
    public Steps openMainPage() {
        open("https://github.com/");
        return this;
    }

    @Step("Выполнить вход под пользователем")
    public Steps login() {
        $(byText("Sign in")).click();
        $("#login_field").setValue(testUserLogin());
        $("#password").setValue(testUserPass());
        $(byValue("Sign in")).click();
        return this;
    }

    @Step("Войти в репозиторий {repoName}")
    public Steps enterRepo(String repoName) {
        $(byText(repoName)).click();
        return this;
    }

    @Step("Создать Issue")
    public Steps createNewIssue(String title, String assignee, String... labels) {
        $(by("data-tab-item", "issues-tab")).click();
        $(".repository-content").$(byText("New issue")).click();

        $("#issue_title").val(title);

        //Добавление assignee
        $(byText("Assignees")).click();
        $$(".select-menu-item").findBy(text(assignee)).click();
        $(byText("Assignees")).click();

        //Добавление labels
        $(byText("Labels")).click();
        for (String label : labels) {
            $$(".label-select-menu-item").findBy(text(label)).click();
        }
        $(byText("Labels")).click();

        $(byText("Submit new issue")).click();
        return this;
    }

    @Step("Убедиться в наличии Issue {issueTitle}")
    public Steps assertIssueInfo(String issueTitle, String assignee, String... labels) {
        $(by("data-tab-item", "issues-tab")).click();

        $(byText(issueTitle)).click();
        $("#partial-discussion-header").shouldHave(text(issueTitle));
        $(".assignee").shouldHave(text(assignee));
        for (String label : labels) {
            $(".labels").shouldHave(text(label));
        }
        return this;
    }

    @Step("Удалить Issue {issueTitle}")
    public Steps deleteIssue(String issueTitle) {
        $(by("data-tab-item", "issues-tab")).click();
        $(byText(issueTitle)).click();
        $(byText("Delete issue")).click();
        $(byName("verify_delete")).click();
        return this;
    }
}