# Java Selenium Framework

A Java Selenium test automation framework (TestNG, Page Object Model) for the login and
ticket-creation flow of a web ticketing portal. It reads test data from Excel and writes
HTML reports, with a screenshot attached to every failure.

## Structure

```
Java_Selenium_Framework/
├── pom.xml                              # Maven build; `mvn test` runs testng.xml
├── testng.xml                           # TestNG suite
├── resources/test_data/
│   ├── Test_Data_Excel.sample.xlsx      # template; copy to Test_Data_Excel.xlsx
│   └── sample.pdf                       # file uploaded by the ticket form
└── src/
    ├── main/java/
    │   ├── pages/
    │   │   ├── locators.java            # all element locators (XPath)
    │   │   └── po_ticketing_tool.java   # page actions: launch, login, raise ticket
    │   └── reusables/
    │       ├── base.java                # driver setup, Excel reader, Extent report
    │       └── methods.java             # waits, click/type, dropdowns, dates, upload, asserts, screenshots
    └── test/
        ├── java/tests/test_ticketingTool.java
        └── resources/.env.example       # copy to .env
```

## What it does

- **Page Object Model.** `locators` holds the locators, `po_ticketing_tool` holds the page
  actions, and the test class only calls those actions.
- **Browser choice.** Chrome (default), Firefox or Edge, chosen with `-Dbrowser=...`.
  Selenium Manager downloads the matching driver automatically.
- **Excel test data** (Apache POI). Rows are looked up by `testcase_id`. The `test_data`
  column holds `key:value|key:value` pairs, which are merged with the row's other columns.
- **Dropdowns.** `selectOption` accepts either an option's value or its visible text, and
  ignores case and spacing differences. It waits for options that load late (dependent
  dropdowns), and when nothing matches it lists the options that are available.
- **Config in `.env`** (dotenv-java): portal URL, report name and report title.
- **Reporting.** An Extent HTML report is written to `test-output/Reports/TestReport.html`,
  with one entry per test method. A timestamped screenshot is saved to
  `test-output/screenshots/` and attached to the report on every failed check.

## Tests

| Test | Steps | Checks |
|------|-------|--------|
| `validCredential` | Open portal, log in | Dashboard appears |
| `Form_Creation` | Log in, then fill the "Schemes Updation – MT/EC" ticket: profit center, material group, material group description, material number, distribution channel, start date (today), end date (+8 days), PDF upload, remarks. Submit. | "Saved Successfully" appears |

## Setup

1. Java 11+ and Maven.
2. Copy `src/test/resources/.env.example` to `src/test/resources/.env` and set `URL` and the report names.
3. Copy `resources/test_data/Test_Data_Excel.sample.xlsx` to `resources/test_data/Test_Data_Excel.xlsx`.
   Fill in the `test_data` cell of `ck_tool_001` with a real login and form values.
   Dropdown values can be the option's value or its visible text.

`.env` and `Test_Data_Excel.xlsx` are git-ignored so credentials and real data stay local.

## Run

```bash
mvn test                      # Chrome
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

## Tech stack

Java, Selenium 4.27, TestNG 7.8, Maven, Apache POI 5.2.5, ExtentReports 5.1.1, dotenv-java.
