package de.pschiessle.xlight.xlightserver;

import org.springframework.stereotype.Component;

@Component
public class BaseDatabaseTest {
  final TestDatabaseClearer testDatabaseClearer;

  public BaseDatabaseTest(TestDatabaseClearer testDatabaseClearer) {
    this.testDatabaseClearer = testDatabaseClearer;
  }

  public TestDatabaseClearer getTestDatabaseClearer(){
    return this.testDatabaseClearer;
  }
}
